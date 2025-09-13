package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkFenceCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class Fence {

    public LogicalDevice device;
    public long handle;

    public Fence(LogicalDevice device) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkFenceCreateInfo createInfo = VkFenceCreateInfo.calloc(memoryStack)
                    .sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO)
                    .flags(VK_FENCE_CREATE_SIGNALED_BIT);
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            VkInterface.check(vkCreateFence(device.device, createInfo, null, longBuffer),
                    "Failed to allocate fence");

            handle = longBuffer.get(0);
        }
    }

    public void reset() {
        VkInterface.check(vkResetFences(device.device, handle),
                "Failed to reset fence");
    }

    public void await() {
        vkWaitForFences(device.device, handle, true, Long.MAX_VALUE);
    }

    public void cleanup() {
        vkDestroyFence(device.device, handle, null);
    }

    public long handle() {
        return handle;
    }
}
