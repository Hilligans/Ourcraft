package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import dev.hilligans.engine.client.graphics.vulkan.api.VulkanMemoryAllocation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanBuffer {

    public LogicalDevice device;
    public VulkanMemoryAllocation allocation;

    public long buffer;
    public long memory;
    public long offset;
    public int size;

    public int properties;

    public long address = 0;

    public ByteBuffer writeableBuffer;

    @Deprecated
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

            System.out.println("Vulkan buffer memory requirements: " + Integer.toBinaryString(memRequirements.memoryTypeBits()));

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

    public VulkanBuffer(LogicalDevice device, long size, int usage, int sharingMode) {
        this.size = (int) size;
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCreateInfo bufferInfo = VkBufferCreateInfo.calloc(memoryStack);
            bufferInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
            bufferInfo.size(size);
            bufferInfo.usage(usage);
            bufferInfo.sharingMode(sharingMode);

            LongBuffer buf = memoryStack.mallocLong(1);

            int res;
            if ((res = vkCreateBuffer(device.device, bufferInfo, null, buf)) != VK_SUCCESS){
                throw new VulkanEngineException(res, "Failed to allocate buffer");
            }
            buffer = buf.get(0);
        }
    }

    public VulkanBuffer(long size, long buffer, long memoryRegion, long offset) {
        this.size = (int) size;
        this.buffer = buffer;
        this.memory = memoryRegion;
        this.offset = offset;
    }

    public void bind(ByteBuffer buffer) {
        this.writeableBuffer = buffer;
    }

    public void setAllocation(VulkanMemoryAllocation allocation) {
        this.allocation = allocation;
    }

    public void copyTo(VkCommandBuffer commandBuffer, VulkanBuffer destBuffer) {
        if(destBuffer == this) {
            return;
        }
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCopy copyRegion = VkBufferCopy.calloc(memoryStack);
            copyRegion.size(size);
            copyRegion.srcOffset(offset);
            copyRegion.dstOffset(destBuffer.offset);
            VkBufferCopy.Buffer buf = VkBufferCopy.calloc(1, memoryStack);
            buf.put(0, copyRegion);
            vkCmdCopyBuffer(commandBuffer, buffer, destBuffer.getVkBuffer(), buf);
        }
    }

    public void copyTo(VkCommandBuffer commandBuffer, VulkanImage destImage) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferImageCopy copyRegion = VkBufferImageCopy.calloc(memoryStack);
            copyRegion.bufferOffset(offset);

            //copyRegion.size(size);
            //copyRegion.srcOffset(offset);
            //copyRegion.dstOffset(destBuffer.offset);
            VkBufferImageCopy.Buffer buf = VkBufferImageCopy.calloc(1, memoryStack);
            buf.put(0, copyRegion);

            vkCmdCopyBufferToImage(commandBuffer, buffer, destImage.getVkImage(), destImage.getVkImageLayout(), buf);
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

    public long getVkBuffer() {
        return buffer;
    }

    public int getAlignment() {
        return 16;
    }

    public int getSize() {
        return size;
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

    public void free() {
        vkDestroyBuffer(device.device, buffer, null);
        allocation.free();
    }

    public void write(ByteBuffer buffer) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.put(buffer).flip();
    }

    public void write(byte[] data, int length) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.put(0, data, 0, length).flip();
    }

    public void write(byte[] data) {
        write(data, data.length);
    }

    public void write(short[] data, int length) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.asShortBuffer().put(0, data, 0, length).flip();
    }

    public void write(short[] data) {
        write(data, data.length);
    }

    public void write(float[] data, int length) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.asFloatBuffer().put(0, data, 0, length).flip();
    }

    public void write(float[] data) {
        write(data, data.length);
    }

    public void write(int[] data, int length) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.asIntBuffer().put(0, data, 0, length).flip();
    }

    public void write(int[] data) {
        write(data, data.length);
    }

    public void write(long[] data, int length) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.asLongBuffer().put(0, data, 0, length).flip();
    }

    public void write(long[] data) {
        write(data, data.length);
    }

    public void write(double[] data, int length) {
        if(writeableBuffer == null) {
            throw new VulkanEngineException("Buffer not bound to memory!");
        }
        writeableBuffer.asDoubleBuffer().put(0, data, 0, length).flip();
    }

    public void write(double[] data) {
        write(data, data.length);
    }

    public static VulkanBuffer newIndexBuffer(LogicalDevice device, long size) {
        return new VulkanBuffer(device, size, VK_BUFFER_USAGE_INDEX_BUFFER_BIT, VK_SHARING_MODE_EXCLUSIVE);
    }

    public static VulkanBuffer newIndexBuffer(GraphicsContext graphicsContext, long size) {
        return newIndexBuffer(VkInterface.getDevice(graphicsContext), size);
    }

    public static VulkanBuffer newVertexBuffer(LogicalDevice device, long size) {
        return new VulkanBuffer(device, size, VK_BUFFER_USAGE_VERTEX_BUFFER_BIT, VK_SHARING_MODE_EXCLUSIVE);
    }

    public static VulkanBuffer newVertexBuffer(GraphicsContext graphicsContext, long size) {
        return newVertexBuffer(VkInterface.getDevice(graphicsContext), size);
    }
}
