package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.Semaphore;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanFrameInfo;
import org.lwjgl.vulkan.VkCommandBuffer;

public abstract class VulkanBaseGraphicsContext extends GraphicsContext {


    public abstract VkCommandBuffer getBuffer();

    public abstract CommandBuffer getCommandBuffer();

    public abstract LogicalDevice getDevice();

    public abstract VulkanWindow getWindow();

    public abstract VulkanFrameInfo frameInfo();

    public abstract void addWaitSemaphore(Semaphore semaphore);

    public abstract void addSignalSemaphore(Semaphore semaphore);
}
