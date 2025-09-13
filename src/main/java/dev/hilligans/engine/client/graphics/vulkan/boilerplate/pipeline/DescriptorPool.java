package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorPoolCreateInfo;
import org.lwjgl.vulkan.VkDescriptorPoolSize;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class DescriptorPool {

    public LogicalDevice device;
    public int count;
    public long descriptorPool;

    public DescriptorPool(LogicalDevice device, int count) {
        this.device = device;
        this.count = count;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkDescriptorPoolCreateInfo poolInfo = VkDescriptorPoolCreateInfo.calloc(memoryStack);
            poolInfo.sType(VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO);

            VkDescriptorPoolSize poolSize = VkDescriptorPoolSize.calloc(memoryStack);
            poolSize.type(VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER);
            poolSize.descriptorCount(count);

            VkDescriptorPoolSize.Buffer buffer = VkDescriptorPoolSize.calloc(1, memoryStack);
            poolInfo.pPoolSizes(buffer);
            poolInfo.maxSets(count);

            LongBuffer longBuffer = memoryStack.mallocLong(1);

            device.executeMethod(() -> vkCreateDescriptorPool(device.device, poolInfo, null, longBuffer), "Failed to create descriptor pool, %s");
            this.descriptorPool = longBuffer.get(0);


        }
    }

    public void cleanup() {
        vkDestroyDescriptorPool(device.device, descriptorPool, null);
    }
}
