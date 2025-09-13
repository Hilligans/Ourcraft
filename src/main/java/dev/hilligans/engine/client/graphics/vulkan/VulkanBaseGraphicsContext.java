package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.Semaphore;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanFrameInfo;
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
