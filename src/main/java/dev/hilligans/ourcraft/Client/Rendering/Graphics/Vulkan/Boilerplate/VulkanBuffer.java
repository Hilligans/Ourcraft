package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngineException;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanBuffer {

    public LogicalDevice device;
    public long buffer;
    public long memory;
    public int size;

    public VulkanBuffer(LogicalDevice device, int size, int usage, int properties) {
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
            allocInfo.memoryTypeIndex(device.findMemoryType(memRequirements.memoryTypeBits(), properties));

            LongBuffer mem = MemoryStack.stackCallocLong(1);

            if (vkAllocateMemory(device.device, allocInfo, null, mem) != VK_SUCCESS){
                throw new VulkanEngineException("Failed to allocate memory");
            }
            memory = mem.get(0);
            vkBindBufferMemory(device.device, buffer, memory, 0);
        }
    }

    public void copyTo(VkCommandBuffer commandBuffer, VulkanBuffer destBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCopy copyRegion = VkBufferCopy.calloc(memoryStack);
            copyRegion.size(size);
            VkBufferCopy.Buffer buf = VkBufferCopy.calloc(1, memoryStack);
            buf.put(0, copyRegion);
            vkCmdCopyBuffer(commandBuffer, buffer, destBuffer.buffer, buf);
        }
    }

    public void free() {
        vkDestroyBuffer(device.device, buffer, null);
        vkFreeMemory(device.device, memory, null);
    }
}
