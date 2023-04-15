package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkFenceCreateInfo;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK10.vkDestroyFence;

public class Queue {

    public VkQueue vkQueue;
    public LogicalDevice device;
    public int queueIndex;
    public int queueFamilyIndex;

    public ConcurrentLinkedQueue<Tuple<VkCommandBuffer, Runnable>> asyncSubmitQueue = new ConcurrentLinkedQueue<>();

    //The count of how many different owners there are of this queue
    public AtomicInteger queueOwners = new AtomicInteger();

    public Queue(LogicalDevice device, long handle, int queueIndex, int queueFamilyIndex) {
        this.device = device;
        this.queueFamilyIndex = queueFamilyIndex;
        this.queueIndex = queueIndex;
        System.out.println("device " + device.device + " handle " + handle);
        this.vkQueue = new VkQueue(handle, device.device);
    }

    public CommandPool allocateCommandPool() {
        return new CommandPool(device, this);
    }

    public synchronized QueueSubmitResult submitQueue(VkSubmitInfo submitInfo, long fence) {
        return new QueueSubmitResult(vkQueueSubmit(vkQueue, submitInfo, fence));
    }

    public synchronized QueueSubmitResult submitQueue(VkCommandBuffer commandBuffer, long fence) {
        int count = asyncSubmitQueue.size() + 1;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer buffers = memoryStack.mallocPointer(count);
            ArrayList<Runnable> runnables = new ArrayList<>();
            for(int x = 0; x < count - 1; x++) {
                Tuple<VkCommandBuffer, Runnable> val = asyncSubmitQueue.poll();
                if(val == null) {
                    throw new RuntimeException();
                }
                buffers.put(x, val.typeA);
                if(val.typeB != null) {
                    runnables.add(val.typeB);
                }
            }
            buffers.put(count - 1, commandBuffer);

            VkSubmitInfo submitInfo = VkSubmitInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);
            submitInfo.pCommandBuffers(buffers);

            return submitQueue(submitInfo, fence).withRunnables(runnables);
        }
    }

    public void submitQueueAsync(VkCommandBuffer commandBuffer, Runnable onComplete) {
        asyncSubmitQueue.add(new Tuple<>(commandBuffer, onComplete));
    }

    public void submitQueueAsync(VkCommandBuffer commandBuffer) {
        asyncSubmitQueue.add(new Tuple<>(commandBuffer, null));
    }

    public void cleanup() {
        //vkDestroyQ
    }
}
