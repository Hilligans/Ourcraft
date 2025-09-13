package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.Semaphore;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanFrameInfo;
import org.lwjgl.vulkan.VkCommandBuffer;

public class TransferVulkanContext extends VulkanBaseGraphicsContext {

    public CommandBuffer commandBuffer;
    public LogicalDevice logicalDevice;
    public VulkanWindow window;
    public VulkanFrameInfo frameInfo;

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

    @Override
    public VulkanFrameInfo frameInfo() {
        return null;
    }

    @Override
    public void addWaitSemaphore(Semaphore semaphore) {

    }

    @Override
    public void addSignalSemaphore(Semaphore semaphore) {

    }
}
