package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.VulkanBaseGraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VkInterface;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.vulkan.VkInstance;

import java.nio.ByteBuffer;

public interface IVulkanMemoryManager {

    boolean canUse(LogicalDevice device);

    VulkanMemoryAllocation allocate(GraphicsContext graphicsContext, VulkanBuffer vulkanBuffer);

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

    VulkanMemoryAllocation allocateSingleUse(GraphicsContext graphicsContext, VulkanBuffer vulkanBuffer);

    void free(VulkanMemoryAllocation allocation);
}
