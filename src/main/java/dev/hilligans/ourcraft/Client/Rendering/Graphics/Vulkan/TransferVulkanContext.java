package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.CommandBuffer;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.CommandPool;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.SingleUseCommandBuffer;
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
