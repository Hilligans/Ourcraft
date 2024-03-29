package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import org.lwjgl.vulkan.VkCommandBuffer;

public class TransferVulkanContext extends VulkanBaseGraphicsContext {

    public CommandBuffer commandBuffer;
    public LogicalDevice logicalDevice;
    public VulkanWindow window;

    public TransferVulkanContext(VkCommandBuffer commandBuffer, LogicalDevice logicalDevice, VulkanWindow window) {
        this.commandBuffer = new CommandBuffer(commandBuffer);
        this.logicalDevice = logicalDevice;
    }

    @Override
    public VkCommandBuffer getBuffer() {
        return commandBuffer.commandBuffer;
    }

    @Override
    public CommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public LogicalDevice getDevice() {
        return logicalDevice;
    }

    @Override
    public VulkanWindow getWindow() {
        return window;
    }
}
