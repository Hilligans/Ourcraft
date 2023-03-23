package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.CommandBuffer;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.GraphicsPipeline;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.RenderPass;
import org.lwjgl.vulkan.VkCommandBuffer;

import static org.lwjgl.vulkan.VK10.vkEndCommandBuffer;

public class VulkanGraphicsContext extends GraphicsContext {

    public CommandBuffer commandBuffer;
    public LogicalDevice device;
    public VulkanWindow window;
    public RenderPass renderPass;

    public GraphicsPipeline boundPipeline;

    public long program;
    public long texture;

    public int bufferIndex;

    public VulkanGraphicsContext(CommandBuffer commandBuffer, LogicalDevice device, VulkanWindow window) {
        this.commandBuffer = commandBuffer;
        this.device = device;
        this.window = window;
        this.renderPass = window.renderPass;
    }

    public LogicalDevice getDevice() {
        return device;
    }

    public VkCommandBuffer getBuffer() {
        return commandBuffer.commandBufferList.get(bufferIndex);
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
        commandBuffer.beginRecording(bufferIndex);
        renderPass.startRenderPass(window, window.frameBuffers.get(bufferIndex), getBuffer());
    }

    public void endRecording() {
        renderPass.endRenderPass(getBuffer());
        vkEndCommandBuffer(getBuffer());
    }
}
