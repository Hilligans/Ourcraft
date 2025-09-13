package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.Semaphore;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanFrameInfo;
import org.lwjgl.system.MemoryUtil;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class FrameManager {

    public int MAX_FRAMES_IN_FLIGHT;
    public int currentFrame = 0;
    public long[] imagesInFlight;
    public VulkanWindow vulkanWindow;

    VulkanFrameInfo[] frames;

    public FrameManager(VulkanWindow vulkanWindow, int maxFrames, int swapChainSize) {
        this.MAX_FRAMES_IN_FLIGHT = maxFrames;
        this.vulkanWindow = vulkanWindow;
        this.imagesInFlight = new long[swapChainSize];
        this.frames = new VulkanFrameInfo[maxFrames];

        for(int x = 0; x < frames.length; x++) {
            frames[x] = new VulkanFrameInfo(vulkanWindow.device);
        }
    }

    public FrameManager(VulkanWindow vulkanWindow) {
        this(vulkanWindow,2, vulkanWindow.swapChain.size);
    }

    public VulkanFrameInfo getCurrentFrameInfo() {
        return frames[currentFrame];
    }

    public LongBuffer getFence() {
        return MemoryUtil.memAllocLong(1).put(0, getFencePointer());
    }

    public long getFencePointer() {
        return getCurrentFrameInfo().submitFence.handle();
    }

    public Semaphore getImageSemaphore() {
        return getCurrentFrameInfo().imageAvailableSemaphore;
    }

    public Semaphore getRenderSemaphore() {
        return getCurrentFrameInfo().renderFinishSemaphore;
    }

    public void advanceFrame() {
        currentFrame = (currentFrame + 1) % MAX_FRAMES_IN_FLIGHT;
    }

    public void startDrawing(int image) {
        imagesInFlight[image] = getFencePointer();
    }

    public boolean canDrawToImage(int image) {
        if(imagesInFlight[image] == 0) {
            return true;
        }
        return vkGetFenceStatus(vulkanWindow.device.device,imagesInFlight[image]) == VK_NULL_HANDLE;
    }

    public void cleanup() {
        vkDeviceWaitIdle(vulkanWindow.device.device);
        for(VulkanFrameInfo frameInfo : frames) {
            frameInfo.cleanup();
        }
    }
}
