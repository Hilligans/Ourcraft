package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Semaphore;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkFenceCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class FrameManager {

    public int MAX_FRAMES_IN_FLIGHT;
    public int currentFrame = 0;
    public long[] vkFences;
    public long[] imagesInFlight;
    public Semaphore[] imageAvailableSemaphores;
    public Semaphore[] renderFinishedSemaphores;
    public VulkanWindow vulkanWindow;

    public FrameManager(VulkanWindow vulkanWindow, int maxFrames, int swapChainSize) {
        this.MAX_FRAMES_IN_FLIGHT = maxFrames;
        this.vulkanWindow = vulkanWindow;
        this.vkFences = new long[maxFrames];
        this.imagesInFlight = new long[swapChainSize];
        this.imageAvailableSemaphores = new Semaphore[maxFrames];
        this.renderFinishedSemaphores = new Semaphore[maxFrames];
        createSyncData();
    }

    public FrameManager(VulkanWindow vulkanWindow) {
        this(vulkanWindow,2, vulkanWindow.swapChain.size);
    }

    public LongBuffer getFence() {
        return MemoryUtil.memAllocLong(1).put(0,vkFences[currentFrame]);
    }

    public Semaphore getImageSemaphore() {
        return imageAvailableSemaphores[currentFrame];
    }

    public Semaphore getRenderSemaphore() {
        return renderFinishedSemaphores[currentFrame];
    }

    public void advanceFrame() {
        currentFrame = (currentFrame + 1) % MAX_FRAMES_IN_FLIGHT;
    }

    public void startDrawing(int image) {
        imagesInFlight[image] = vkFences[currentFrame];
    }

    public boolean canDrawToImage(int image) {
        if(imagesInFlight[image] == 0) {
            return true;
        }
        return vkGetFenceStatus(vulkanWindow.device.device,imagesInFlight[image]) == VK_NULL_HANDLE;
    }

    private void createSyncData() {
        try {
            try (MemoryStack memoryStack = MemoryStack.stackPush()) {
                VkFenceCreateInfo createInfo = VkFenceCreateInfo.callocStack(memoryStack).sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO).flags(VK_FENCE_CREATE_SIGNALED_BIT);
                LongBuffer longBuffer = memoryStack.mallocLong(1);
                for (int x = 0; x < MAX_FRAMES_IN_FLIGHT; x++) {
                    if (vkCreateFence(vulkanWindow.device.device, createInfo, null, longBuffer) != VK_SUCCESS) {
                        vulkanWindow.device.physicalDevice.vulkanInstance.exit("failed to create vk fence");
                    }
                    vkFences[x] = longBuffer.get(0);
                    imageAvailableSemaphores[x] = new Semaphore(vulkanWindow.device);
                    renderFinishedSemaphores[x] = new Semaphore(vulkanWindow.device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        vkDeviceWaitIdle(vulkanWindow.device.device);
        for(Semaphore semaphore : renderFinishedSemaphores) {
            semaphore.cleanup();
        }
        for(Semaphore semaphore : imageAvailableSemaphores) {
            semaphore.cleanup();
        }
        for(long fence : vkFences) {
            vkDestroyFence(vulkanWindow.device.device,fence,null);
        }
    }

}
