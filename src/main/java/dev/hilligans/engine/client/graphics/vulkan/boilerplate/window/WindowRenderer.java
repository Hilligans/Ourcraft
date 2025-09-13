package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.Queue;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.Semaphore;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkPresentInfoKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public class WindowRenderer {

    public VulkanWindow vulkanWindow;

    public WindowRenderer(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;
    }

    /*
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
            VulkanGraphicsContext context = vulkanWindow.context;
            context.setBufferInUse(imageIndex.get(0));
            vulkanWindow.frameManager.startDrawing(imageIndex.get(0));

            VkSubmitInfo submitInfo = VkSubmitInfo.calloc().sType(VK_STRUCTURE_TYPE_SUBMIT_INFO).waitSemaphoreCount(1).pWaitSemaphores(imageSemaphore.get(memoryStack));
            submitInfo.pWaitDstStageMask(memoryStack.ints(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT));

            VkCommandBuffer buffer = vulkanWindow.commandBuffer.commandBufferList.get(imageIndex.get(0));

            submitInfo.pCommandBuffers(memoryStack.mallocPointer(1).put(0, vulkanWindow.commandBuffer.commandBuffers.get(imageIndex.get(0))));
            submitInfo.pSignalSemaphores(renderSemaphore.get(memoryStack));

            vkResetFences(vulkanWindow.device.device, vulkanWindow.frameManager.getFence());
            vkResetCommandBuffer(buffer, 0);

            //draw to buffer

         //   if(vkQueueSubmit(vulkanWindow.graphicsFamily.getQueue(0).vkQueue, submitInfo, vulkanWindow.frameManager.imagesInFlight[vulkanWindow.frameManager.currentFrame]) != VK_SUCCESS) {
            if(vkQueueSubmit(vulkanWindow.graphicsFamily.getQueue(0).vkQueue, submitInfo, vulkanWindow.frameManager.getFencePointer()) != VK_SUCCESS) {
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
     */

    public int waitForNextFrame() {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer imageIndex = memoryStack.mallocInt(1);
            Semaphore imageSemaphore = vulkanWindow.frameManager.getImageSemaphore();
            vkWaitForFences(vulkanWindow.device.device, vulkanWindow.frameManager.getFence(), true, Long.MAX_VALUE);
            vkAcquireNextImageKHR(vulkanWindow.device.device, vulkanWindow.swapChain.swapChain, Long.MAX_VALUE, imageSemaphore.semaphore, VK_NULL_HANDLE, imageIndex);
            if (!vulkanWindow.frameManager.canDrawToImage(imageIndex.get(0))) {
                vkWaitForFences(vulkanWindow.device.device, vulkanWindow.frameManager.imagesInFlight[imageIndex.get(0)], true, Integer.MAX_VALUE);
            }
            vulkanWindow.frameManager.startDrawing(imageIndex.get(0));
            return imageIndex.get(0);
        }
    }

    public void render(Queue queue, VkCommandBuffer... commandBuffers) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {

            Semaphore renderSemaphore = vulkanWindow.frameManager.getRenderSemaphore();
            Semaphore imageSemaphore = vulkanWindow.frameManager.getImageSemaphore();
            /*VkSubmitInfo submitInfo = VkSubmitInfo.calloc().sType(VK_STRUCTURE_TYPE_SUBMIT_INFO).waitSemaphoreCount(1).pWaitSemaphores(imageSemaphore.get(memoryStack));
            submitInfo.pWaitDstStageMask(memoryStack.ints(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT));
            PointerBuffer bufferPointers = memoryStack.mallocPointer(commandBuffers.length);
            for(int x = 0; x < commandBuffers.length; x++) {
                bufferPointers.put(x, commandBuffers[x]);
            }
            submitInfo.pCommandBuffers(bufferPointers);
            submitInfo.pSignalSemaphores(renderSemaphore.get(memoryStack));

             */

            vkResetFences(vulkanWindow.device.device, vulkanWindow.frameManager.getFencePointer());

            queue.submitQueue(vulkanWindow.frameManager.getFencePointer(), renderSemaphore.get(memoryStack), imageSemaphore.get(memoryStack), memoryStack.ints(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT), commandBuffers);

        //    if(vkQueueSubmit(vulkanWindow.graphicsFamily.getQueue(0).vkQueue, submitInfo, vulkanWindow.frameManager.getFencePointer()) != VK_SUCCESS) {
        //        vulkanWindow.device.vulkanInstance.exit("Failed to submit to queue");
        //    }
        }
    }

    public void present(int imageIndex) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            Semaphore renderSemaphore = vulkanWindow.frameManager.getRenderSemaphore();

            VkPresentInfoKHR presentInfoKHR = VkPresentInfoKHR.calloc(memoryStack);
            presentInfoKHR.sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR);
            presentInfoKHR.pWaitSemaphores(renderSemaphore.get(memoryStack));

            LongBuffer swapChain = memoryStack.callocLong(1);
            swapChain.put(0, vulkanWindow.swapChain.swapChain);
            presentInfoKHR.swapchainCount(1);
            presentInfoKHR.pSwapchains(swapChain);

            presentInfoKHR.pImageIndices(memoryStack.ints(imageIndex));

            vkQueuePresentKHR(vulkanWindow.graphicsFamily.getQueue(0).vkQueue, presentInfoKHR);
            vulkanWindow.frameManager.advanceFrame();
        }
    }

    /*
    public void submitDraws(VkCommandBuffer commandBuffer, VertexBuffer vertexBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindPipeline(commandBuffer, VK_PIPELINE_BIND_POINT_GRAPHICS, vulkanWindow.graphicsPipeline.pipeline);
            vkCmdBindVertexBuffers(commandBuffer, 0, memoryStack.longs(vertexBuffer.buffer), memoryStack.longs(0));
            vkCmdDraw(commandBuffer, 3, 1, 0, 0);
        }
    }

     */
}
