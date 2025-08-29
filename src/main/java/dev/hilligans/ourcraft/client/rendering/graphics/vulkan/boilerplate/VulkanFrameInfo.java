package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate;

import java.util.concurrent.ConcurrentLinkedQueue;

public class VulkanFrameInfo {

    public Fence submitFence;
    public ConcurrentLinkedQueue<Runnable> cleanupResources = new ConcurrentLinkedQueue<>();

    public VulkanFrameInfo(LogicalDevice device) {
        submitFence = new Fence(device);
    }

    public void addCleanupResource(Runnable runnable) {
        cleanupResources.add(runnable);
    }
}
