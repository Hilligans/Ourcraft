package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.CommandPool;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.pipeline.GraphicsPipeline;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.pipeline.RenderPass;
import org.lwjgl.vulkan.VkCommandBuffer;

import static org.lwjgl.vulkan.VK10.vkEndCommandBuffer;

public class VulkanGraphicsContext extends VulkanBaseGraphicsContext {

    public CommandPool commandPool;
    public LogicalDevice device;
    public VulkanWindow window;
    public RenderPass renderPass;

    public GraphicsPipeline boundPipeline;

    public long program;
    public long texture;

    public int bufferIndex;

    public VulkanGraphicsContext(CommandPool commandPool, LogicalDevice device, VulkanWindow window) {
        this.commandPool = commandPool;
        this.device = device;
        this.window = window;
        this.renderPass = window.renderPass;
    }

    public LogicalDevice getDevice() {
        return device;
    }

    @Override
    public VulkanWindow getWindow() {
        return window;
    }

    public VkCommandBuffer getBuffer() {
        return commandPool.commandBuffers.get(bufferIndex);
    }

    @Override
    public CommandBuffer getCommandBuffer() {
        return null;
    }

    /*public void advanceBufferInUse() {
        bufferIndex++;
        if(bufferIndex == commandBuffer.commandBufferList.size()) {
            bufferIndex = 0;
        }
    }

     */
    public void setBufferInUse(int index) {
        this.bufferIndex = index;
    }

    public void bindPipeline(GraphicsPipeline pipeline) {
        this.boundPipeline = pipeline;
    }

    public void startRecording() {
        commandPool.beginRecording(bufferIndex);
        renderPass.startRenderPass(window, window.frameBuffers.get(bufferIndex), getBuffer());
    }

    public void endRecording() {
        renderPass.endRenderPass(getBuffer());
        vkEndCommandBuffer(getBuffer());
    }
}
