package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VulkanFrameInfo {

    public ArrayList<CommandBuffer> commandBuffers = new ArrayList<>();

    public Fence submitFence;
    public Semaphore renderFinishSemaphore;
    public Semaphore imageAvailableSemaphore;
    public ConcurrentLinkedQueue<Runnable> cleanupResources = new ConcurrentLinkedQueue<>();


    public VulkanFrameInfo(LogicalDevice device) {
        submitFence = new Fence(device);
        renderFinishSemaphore = new Semaphore(device);
        imageAvailableSemaphore = new Semaphore(device);
        cleanupResources.add(() -> commandBuffers.forEach(CommandBuffer::reset));
    }

    public void addCleanupResource(Runnable runnable) {
        cleanupResources.add(runnable);
    }

    public void reset() {
        cleanupResources.forEach(Runnable::run);
        submitFence.reset();
    }

    public void cleanup() {
        cleanupResources.forEach(Runnable::run);
        submitFence.cleanup();
        renderFinishSemaphore.cleanup();
        imageAvailableSemaphore.cleanup();
        commandBuffers.forEach(CommandBuffer::cleanup);
    }
}
