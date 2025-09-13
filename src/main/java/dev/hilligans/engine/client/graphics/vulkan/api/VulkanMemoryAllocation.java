package dev.hilligans.engine.client.graphics.vulkan.api;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanImage;

import static org.lwjgl.vulkan.VK10.*;

public record VulkanMemoryAllocation(long memory, long size, long offset, long pointer, long allocation, VulkanMemoryAllocation transferDestination, IVulkanMemoryAllocator allocator) {

    public void bindToBuffer(VulkanBuffer buffer) {
        buffer.memory = memory;
        buffer.setAllocation(this);
        vkBindBufferMemory(buffer.device.device, buffer.buffer, memory, offset);
    }

    public void bindToImage(VulkanImage image) {
        image.memory = memory;
        vkBindImageMemory(image.device.device, image.image, memory, offset);
    }

    /**
     * @param buffer The vulkan buffer to bind to the destination
     * @return A mappable buffer, which is the same buffer as the one passed in if the memory is host visible
     */
    public VulkanBuffer bindAndAllocateBuffer(VulkanBuffer buffer) {
        if(transferDestination != null) {
            transferDestination.bindToBuffer(buffer);

            VulkanBuffer srcBuffer = new VulkanBuffer(buffer.device, size, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_SHARING_MODE_EXCLUSIVE);
            this.bindToBuffer(srcBuffer);
            return srcBuffer;

        } else {
            this.bindToBuffer(buffer);
            return buffer;
        }
    }

    public void bindAndAllocateBuffer(GraphicsContext graphicsContext, VulkanImage image) {

    }

    public boolean requiresCopy() {
        return transferDestination != null;
    }

    public void free() {
        allocator.getMemoryManager().free(this);
    }
}
