package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.data.Tuple;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SUBMIT_INFO;
import static org.lwjgl.vulkan.VK10.vkQueueSubmit;

public class Queue {

    public VkQueue vkQueue;
    public LogicalDevice device;
    public int queueIndex;
    public int queueFamilyIndex;
    public QueueFamily queueFamily;

    public ConcurrentLinkedQueue<Tuple<VkCommandBuffer, Runnable>> asyncSubmitQueue = new ConcurrentLinkedQueue<>();

    //The count of how many different owners there are of this queue
    public AtomicInteger queueOwners = new AtomicInteger();

    public Queue(LogicalDevice device, long handle, int queueIndex, int queueFamilyIndex, QueueFamily queueFamily) {
        this.device = device;
        this.queueFamilyIndex = queueFamilyIndex;
        this.queueIndex = queueIndex;
        this.queueFamily = queueFamily;
        System.out.println("device " + device.device + " handle " + handle);
        this.vkQueue = new VkQueue(handle, device.device);
    }

    public CommandPool allocateCommandPool() {
        return new CommandPool(device, this);
    }

    public synchronized QueueSubmitResult submitQueue(long fence,  VkCommandBuffer... commandBuffers) {
        return this.submitQueue(fence, null, null, null, commandBuffers);
    }

    public synchronized QueueSubmitResult submitQueue(long fence, @Nullable LongBuffer signalSemaphores, @Nullable LongBuffer waitSemaphores, @Nullable IntBuffer waitMask, VkCommandBuffer... commandBuffers) {
        int count = asyncSubmitQueue.size() + commandBuffers.length;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer buffers = memoryStack.mallocPointer(count);
            ArrayList<Runnable> runnables = new ArrayList<>();
            for(int x = 0; x < count - 1; x++) {
                Tuple<VkCommandBuffer, Runnable> val = asyncSubmitQueue.poll();
                if(val == null) {
                    throw new RuntimeException();
                }
                buffers.put(val.typeA);
                if(val.typeB != null) {
                    runnables.add(val.typeB);
                }
            }

            for(VkCommandBuffer commandBuffer : commandBuffers) {
                buffers.put(commandBuffer);
            }

            VkSubmitInfo submitInfo = VkSubmitInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);
            submitInfo.pCommandBuffers(buffers.flip());
            if(signalSemaphores != null) {
                submitInfo.pSignalSemaphores(signalSemaphores);
            }
            if(waitSemaphores != null) {
                submitInfo.waitSemaphoreCount(waitSemaphores.capacity());
                submitInfo.pWaitSemaphores(waitSemaphores);
            }
            if(waitMask != null) {
                submitInfo.pWaitDstStageMask(waitMask);
            }

            return submitQueue(submitInfo, fence).withRunnables(runnables);
        }
    }

    public synchronized QueueSubmitResult submitQueue(VkSubmitInfo submitInfo, long fence) {
        return new QueueSubmitResult(vkQueueSubmit(vkQueue, submitInfo, fence));
    }

    public void submitQueueAsync(VkCommandBuffer commandBuffer, Runnable onComplete) {
        asyncSubmitQueue.add(new Tuple<>(commandBuffer, onComplete));
    }

    public void submitQueueAsync(VkCommandBuffer commandBuffer) {
        asyncSubmitQueue.add(new Tuple<>(commandBuffer, null));
    }

    public final VkQueue handle() {
        return vkQueue;
    }

    public void cleanup() {
        //vkDestroyQ
    }
}
