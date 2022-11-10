package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.CommandBuffer;
import org.lwjgl.vulkan.VkCommandBuffer;

public class VulkanGraphicsContext extends GraphicsContext {

    public CommandBuffer commandBuffer;
    public LogicalDevice device;

    public long program;
    public long texture;



    public LogicalDevice getDevice() {
        return device;
    }

    public VkCommandBuffer getBuffer() {
        return commandBuffer.commandBufferList.get(0);
    }
}
