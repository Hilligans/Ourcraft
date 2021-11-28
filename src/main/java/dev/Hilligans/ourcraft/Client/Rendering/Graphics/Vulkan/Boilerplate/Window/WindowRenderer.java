package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Semaphore;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public class WindowRenderer {

    public VulkanWindow vulkanWindow;

    public WindowRenderer(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;
    }

    public void render() {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer imageIndex = memoryStack.mallocInt(1);
            Semaphore imageSemaphore = vulkanWindow.frameManager.getImageSemaphore();
            Semaphore renderSemaphore = vulkanWindow.frameManager.getRenderSemaphore();
            vkWaitForFences(vulkanWindow.device.device, vulkanWindow.frameManager.getFence(), true, Long.MAX_VALUE);
            vkAcquireNextImageKHR(vulkanWindow.device.device, vulkanWindow.swapChain.swapChain, Long.MAX_VALUE, imageSemaphore.semaphore, VK_NULL_HANDLE, imageIndex);
            if(!vulkanWindow.frameManager.canDrawToImage(imageIndex.get(0))) {
                vkWaitForFences(vulkanWindow.device.device, vulkanWindow.frameManager.imagesInFlight[imageIndex.get(0)],true,Integer.MAX_VALUE);
            }
            vulkanWindow.frameManager.startDrawing(imageIndex.get(0));

            VkSubmitInfo submitInfo = VkSubmitInfo.callocStack(memoryStack).sType(VK_STRUCTURE_TYPE_SUBMIT_INFO).waitSemaphoreCount(1).pWaitSemaphores(imageSemaphore.get(memoryStack));
            submitInfo.pWaitDstStageMask(memoryStack.ints(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT));
            submitInfo.pCommandBuffers(memoryStack.mallocPointer(1).put(0, vulkanWindow.commandBuffer.commandBuffers.get(imageIndex.get(0))));
            submitInfo.pSignalSemaphores(renderSemaphore.get(memoryStack));

            vkResetFences(vulkanWindow.device.device, vulkanWindow.frameManager.getFence());

            if(vkQueueSubmit(vulkanWindow.graphicsFamily.getQueue(0).vkQueue,submitInfo, vulkanWindow.frameManager.imagesInFlight[vulkanWindow.frameManager.currentFrame]) != VK_SUCCESS) {
                vulkanWindow.device.vulkanInstance.exit("Failed to submit to queue");
            }

            VkPresentInfoKHR presentInfoKHR = VkPresentInfoKHR.calloc();
            presentInfoKHR.sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR);
            presentInfoKHR.pWaitSemaphores(renderSemaphore.get(memoryStack));

            LongBuffer swapChain = memoryStack.callocLong(1);
            swapChain.put(0, vulkanWindow.swapChain.swapChain);
            presentInfoKHR.swapchainCount(1);
            presentInfoKHR.pSwapchains(swapChain);
            presentInfoKHR.pImageIndices(imageIndex);

            vkQueuePresentKHR(vulkanWindow.graphicsFamily.getQueue(0).vkQueue,presentInfoKHR);
            vulkanWindow.frameManager.advanceFrame();
        }
    }
}
