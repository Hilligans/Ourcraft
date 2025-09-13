package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.*;

public class SingleUseCommandBuffer {

    public LogicalDevice device;
    public long commandPool;
    public Queue queue;
    public VkCommandBuffer buffer;

    public SingleUseCommandBuffer(LogicalDevice device, Queue queue) {
        System.out.println(queue.queueFamily);
        this.device = device;
        this.queue = queue;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo.calloc(memoryStack);
            commandPoolCreateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO);
            commandPoolCreateInfo.queueFamilyIndex(queue.queueFamilyIndex);
            LongBuffer longBuffer = memoryStack.callocLong(1);
            if (vkCreateCommandPool(device.device, commandPoolCreateInfo, null, longBuffer) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to create command pool");
            }
            commandPool = longBuffer.get(0);

            PointerBuffer commandBuffers = memoryStack.mallocPointer(1);

            VkCommandBufferAllocateInfo commandBufferAllocateInfo = VkCommandBufferAllocateInfo.calloc(memoryStack);
            commandBufferAllocateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO);
            commandBufferAllocateInfo.commandPool(commandPool);
            commandBufferAllocateInfo.level(VK_COMMAND_BUFFER_LEVEL_PRIMARY);
            commandBufferAllocateInfo.commandBufferCount(1);
            if (vkAllocateCommandBuffers(device.device, commandBufferAllocateInfo, commandBuffers) != VK_SUCCESS) {
                device.vulkanInstance.exit("Failed to allocate command buffers");
            }
            buffer = new VkCommandBuffer(commandBuffers.get(0), device.device);

            VkCommandBufferBeginInfo vkCommandBufferBeginInfo = VkCommandBufferBeginInfo.calloc(memoryStack);
            vkCommandBufferBeginInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
            vkCommandBufferBeginInfo.flags(VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT);
            if (vkBeginCommandBuffer(buffer, vkCommandBufferBeginInfo) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to begin recording command buffer");
            }
        }
    }

    public VkCommandBuffer getBuffer() {
        return buffer;
    }

    public void endAndSubmit(ArrayList<Runnable> runnables) {
        vkEndCommandBuffer(buffer);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkSubmitInfo submitInfo = VkSubmitInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);
            submitInfo.pCommandBuffers(memoryStack.pointers(buffer));

            VkFenceCreateInfo createInfo = VkFenceCreateInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO).flags(VK_FENCE_CREATE_SIGNALED_BIT);
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreateFence(device.device, createInfo, null, longBuffer) != VK_SUCCESS) {
                throw new RuntimeException();
            }
            vkResetFences(device.device, longBuffer.get(0));

            queue.submitQueue(longBuffer.get(0), buffer);

            long fence = longBuffer.get(0);
            device.submitResourceForCleanup(() -> {
                vkWaitForFences(device.device, fence, true, Long.MAX_VALUE);
                vkDestroyCommandPool(device.device,commandPool,null);
                vkDestroyFence(device.device, fence, null);
                queue.queueOwners.decrementAndGet();
                if(runnables != null) {
                    for (Runnable runnable : runnables) {
                        runnable.run();
                    }
                }
            });
        }
    }
}

