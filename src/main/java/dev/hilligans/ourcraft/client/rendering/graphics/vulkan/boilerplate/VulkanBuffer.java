package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.VulkanEngineException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK10.vkUnmapMemory;

public class VulkanBuffer {

    public LogicalDevice device;
    public long buffer;
    public long memory;
    public long offset;
    public int size;

    public int properties;

    public long address = 0;

    public VulkanBuffer(LogicalDevice device, int size, int usage, int properties, boolean singleUseResource) {
        this.size = size;
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCreateInfo bufferInfo = VkBufferCreateInfo.calloc(memoryStack);
            bufferInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
            bufferInfo.size(size);
            bufferInfo.usage(usage);
            bufferInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);

            LongBuffer buf = MemoryStack.stackCallocLong(1);

            if (vkCreateBuffer(device.device, bufferInfo, null, buf) != VK_SUCCESS){
                throw new VulkanEngineException("Failed to allocate buffer");
            }
            buffer = buf.get(0);

            VkMemoryRequirements memRequirements = VkMemoryRequirements.calloc(memoryStack);
            vkGetBufferMemoryRequirements(device.device, buf.get(0), memRequirements);

            VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.calloc(memoryStack);
            allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
            allocInfo.allocationSize(memRequirements.size());
            allocInfo.memoryTypeIndex(findMemoryType(memRequirements.memoryTypeBits(), properties));

            LongBuffer mem = MemoryStack.stackCallocLong(1);

            if (vkAllocateMemory(device.device, allocInfo, null, mem) != VK_SUCCESS){
                throw new VulkanEngineException("Failed to allocate memory");
            }
            memory = mem.get(0);
            vkBindBufferMemory(device.device, buffer, memory, 0);

        }
    }

    public VulkanBuffer(long size, long buffer, long memoryRegion, long offset) {
        this.size = (int) size;
        this.buffer = buffer;
        this.memory = memoryRegion;
        this.offset = offset;
    }


    public void copyTo(VkCommandBuffer commandBuffer, VulkanBuffer destBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCopy copyRegion = VkBufferCopy.calloc(memoryStack);
            copyRegion.size(size);
            copyRegion.srcOffset(offset);
            copyRegion.dstOffset(destBuffer.offset);
            VkBufferCopy.Buffer buf = VkBufferCopy.calloc(1, memoryStack);
            buf.put(0, copyRegion);
            vkCmdCopyBuffer(commandBuffer, buffer, destBuffer.buffer, buf);
        }
    }

    public int findMemoryType(int filter, int properties) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPhysicalDeviceMemoryProperties memProperties = VkPhysicalDeviceMemoryProperties.calloc(memoryStack);
            vkGetPhysicalDeviceMemoryProperties(device.physicalDevice.physicalDevice, memProperties);

            for (int i = 0; i < memProperties.memoryTypeCount(); i++) {
                if ((filter & (1 << i)) != 0 && (memProperties.memoryTypes(i).propertyFlags() & properties) == properties) {
                    this.properties = memProperties.memoryTypes(i).propertyFlags();
                    return i;
                }
            }
            device.vulkanInstance.exit("Failed to find memory");
        }
        return -1;
    }

    public boolean canMap() {
        return (properties & VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT) != 0;
    }

    public boolean requiresStaging() {
        return (properties & VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT) == 0;
    }

    public ByteBuffer map() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, memory, 0, size, 0, pos);
            this.address = pos.get(0);
        }

        return null;
    }

    public void unmap() {
        vkUnmapMemory(device.device, memory);
        this.address = 0;
    }

    public void write(ByteBuffer buffer) {
        MemoryUtil.memByteBuffer(address, size).put(buffer);
    }

    public void free() {
        if(address != 0) {
            unmap();
        }
        vkDestroyBuffer(device.device, buffer, null);
        vkFreeMemory(device.device, memory, null);
    }
}
