package dev.hilligans.engine.client.graphics.vulkan.api;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VkInterface;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;

import java.nio.ByteBuffer;

public interface IVulkanMemoryManager {

    boolean canUse(LogicalDevice device);

    VulkanMemoryAllocation allocate(GraphicsContext graphicsContext, VulkanBuffer vulkanBuffer);

    default VulkanMemoryAllocation allocateSingleUse(GraphicsContext graphicsContext, VulkanBuffer vulkanBuffer) {
        return allocate(graphicsContext, vulkanBuffer);
    }

    default void upload(GraphicsContext graphicsContext, VulkanBuffer buffer, ByteBuffer data) {
        CommandBuffer cmdBuffer = VkInterface.getCommandBuffer(graphicsContext);

        VulkanMemoryAllocation allocation = allocate(graphicsContext, buffer);

        VulkanBuffer writeBuffer = allocation.bindAndAllocateBuffer(buffer);
        writeBuffer.write(data);
        writeBuffer.copyTo(cmdBuffer.getCommandBuffer(), buffer);

        if(allocation.requiresCopy()) {
            cmdBuffer.add(() -> free(allocation));
        }
    }

    void free(VulkanMemoryAllocation allocation);
}
