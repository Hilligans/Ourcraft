package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class Semaphore {


    public VulkanInstance vulkanInstance;
    public LogicalDevice device;
    public long semaphore = -1;

    public Semaphore(LogicalDevice device) {
        this.vulkanInstance = device.vulkanInstance;
        this.device = device;
        VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo.calloc();
        semaphoreCreateInfo.sType(VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if(vkCreateSemaphore(vulkanInstance.logicalDevice.device,semaphoreCreateInfo,null,longBuffer) != VK_SUCCESS) {
                vulkanInstance.exit("Failed to create semaphore");
            }
            semaphore = longBuffer.get(0);
        }
    }

    public void cleanup() {
        vkDestroySemaphore(device.device, semaphore, null);
    }

    public LongBuffer get(MemoryStack memoryStack) {
        return memoryStack.mallocLong(1).put(0, semaphore);
    }

    public final long handle() {
        return semaphore;
    }
}
