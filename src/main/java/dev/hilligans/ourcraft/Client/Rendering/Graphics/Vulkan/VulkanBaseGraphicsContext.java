package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.CommandBuffer;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import org.lwjgl.vulkan.VkCommandBuffer;

public abstract class VulkanBaseGraphicsContext extends GraphicsContext {


    public abstract VkCommandBuffer getBuffer();

    public abstract CommandBuffer getCommandBuffer();

    public abstract LogicalDevice getDevice();

    public abstract VulkanWindow getWindow();
}
