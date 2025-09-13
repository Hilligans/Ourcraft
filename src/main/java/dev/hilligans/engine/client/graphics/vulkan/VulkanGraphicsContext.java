package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.*;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline.GraphicsPipeline;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline.RenderPass;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.LongBuffer;
import java.util.HashSet;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SUBMIT_INFO;
import static org.lwjgl.vulkan.VK10.vkQueueSubmit;

public class VulkanGraphicsContext extends VulkanBaseGraphicsContext {

    public CommandPool commandPool;
    public LogicalDevice device;
    public VulkanWindow window;
    public RenderPass renderPass;

    public VulkanFrameInfo frameInfo;
    public HashSet<Semaphore> signalSemaphores = new HashSet<>();
    public HashSet<Semaphore> waitSemaphores = new HashSet<>();
    public VkCommandBuffer commandBuffer;
    public Queue graphicsQueue;


    public GraphicsPipeline boundPipeline;

    public long program;
    public long texture;

    public int bufferIndex;

    public VulkanGraphicsContext(CommandPool commandPool, LogicalDevice device, VulkanWindow window) {
        this.commandPool = commandPool;
        this.device = device;
        this.window = window;
        this.renderPass = window.renderPass;
    }

    public LogicalDevice getDevice() {
        return device;
    }

    @Override
    public VulkanWindow getWindow() {
        return window;
    }

    @Override
    public VulkanFrameInfo frameInfo() {
        return frameInfo;
    }

    @Override
    public void addWaitSemaphore(Semaphore semaphore) {
        waitSemaphores.add(semaphore);
    }

    @Override
    public void addSignalSemaphore(Semaphore semaphore) {
        signalSemaphores.add(semaphore);
    }

    public VkCommandBuffer getBuffer() {
        return commandBuffer;
    }

    @Override
    public CommandBuffer getCommandBuffer() {
        return null;
    }

    /*public void advanceBufferInUse() {
        bufferIndex++;
        if(bufferIndex == commandBuffer.commandBufferList.size()) {
            bufferIndex = 0;
        }
    }

     */
  //  public void setBufferInUse(int index) {
  //      this.bufferIndex = index;
   // }

    public void bindPipeline(GraphicsPipeline pipeline) {
        this.boundPipeline = pipeline;
    }

  //  public void startRecording() {
  //      commandPool.beginRecording(bufferIndex);
  //      renderPass.startRenderPass(window, window.frameBuffers.get(bufferIndex), getBuffer());
  //  }

  //  public void endRecording() {
  //      renderPass.endRenderPass(getBuffer());
  //      vkEndCommandBuffer(getBuffer());
  //  }

    public void submit(long fence) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkSubmitInfo submitInfo = VkSubmitInfo.calloc(memoryStack);
            submitInfo.sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);

            int x = 0;

            if(!signalSemaphores.isEmpty()) {
                LongBuffer signalBuffer = memoryStack.mallocLong(signalSemaphores.size());
                for (Semaphore semaphore : signalSemaphores) {
                    signalBuffer.put(x++, semaphore.handle());
                }
                submitInfo.pSignalSemaphores(signalBuffer);
            }

            if(!waitSemaphores.isEmpty()) {
                LongBuffer waitBuffer = memoryStack.mallocLong(waitSemaphores.size());
                x = 0;
                for (Semaphore semaphore : waitSemaphores) {
                    waitBuffer.put(x++, semaphore.handle());
                }
                submitInfo.pWaitSemaphores(waitBuffer);
            }

            PointerBuffer buffer = memoryStack.pointers(commandBuffer);
            submitInfo.pCommandBuffers(buffer);

            vkQueueSubmit(graphicsQueue.handle(), submitInfo, fence);
        }
    }

    public VulkanFrameInfo getFrameInfo() {
        return frameInfo;
    }
}
