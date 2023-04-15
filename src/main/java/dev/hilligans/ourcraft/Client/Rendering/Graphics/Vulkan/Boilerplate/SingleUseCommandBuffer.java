package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

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
    public VkCommandBuffer commandBuffer;

    public SingleUseCommandBuffer(LogicalDevice device, Queue queue) {
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
            commandBuffer = new VkCommandBuffer(commandBuffers.get(0), device.device);

            VkCommandBufferBeginInfo vkCommandBufferBeginInfo = VkCommandBufferBeginInfo.calloc(memoryStack);
            vkCommandBufferBeginInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
            vkCommandBufferBeginInfo.flags(VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT);
            if (vkBeginCommandBuffer(commandBuffer, vkCommandBufferBeginInfo) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to begin recording command buffer");
            }
        }
    }

    public VkCommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public void endAndSubmit() {
        vkEndCommandBuffer(commandBuffer);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkSubmitInfo submitInfo = VkSubmitInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);
            submitInfo.pCommandBuffers(memoryStack.pointers(commandBuffer));

            VkFenceCreateInfo createInfo = VkFenceCreateInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO).flags(VK_FENCE_CREATE_SIGNALED_BIT);
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreateFence(device.device, createInfo, null, longBuffer) != VK_SUCCESS) {
                throw new RuntimeException();
            }
            vkResetFences(device.device, longBuffer.get(0));
            queue.submitQueue(submitInfo, longBuffer.get(0));

            long fence = longBuffer.get(0);
            device.submitResourceForCleanup(() -> {
                vkWaitForFences(device.device, fence, true, Long.MAX_VALUE);
                vkDestroyCommandPool(device.device,commandPool,null);
                vkDestroyFence(device.device, fence, null);
                queue.queueOwners.decrementAndGet();
            });
        }
    }

    public void endAndSubmit(ArrayList<Runnable> runnables) {
        vkEndCommandBuffer(commandBuffer);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkSubmitInfo submitInfo = VkSubmitInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);
            submitInfo.pCommandBuffers(memoryStack.pointers(commandBuffer));

            VkFenceCreateInfo createInfo = VkFenceCreateInfo.calloc(memoryStack).sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO).flags(VK_FENCE_CREATE_SIGNALED_BIT);
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreateFence(device.device, createInfo, null, longBuffer) != VK_SUCCESS) {
                throw new RuntimeException();
            }
            vkResetFences(device.device, longBuffer.get(0));
            queue.submitQueue(submitInfo, longBuffer.get(0));

            long fence = longBuffer.get(0);
            device.submitResourceForCleanup(() -> {
                vkWaitForFences(device.device, fence, true, Long.MAX_VALUE);
                vkDestroyCommandPool(device.device,commandPool,null);
                vkDestroyFence(device.device, fence, null);
                queue.queueOwners.decrementAndGet();
                for(Runnable runnable : runnables) {
                    runnable.run();
                }
            });
        }
    }
}

