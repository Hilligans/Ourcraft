package dev.hilligans.engine.client.graphics.vulkan.api;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;

public class TransferAlwaysVulkanMemoryManager implements IVulkanMemoryManager {

    @Override
    public boolean canUse(LogicalDevice device) {
        return device.getMemoryManager().hasStagingHeap();
    }

    @Override
    public VulkanMemoryAllocation allocate(GraphicsContext graphicsContext, VulkanBuffer vulkanBuffer) {
        return null;
    }

    @Override
    public VulkanMemoryAllocation allocateSingleUse(GraphicsContext graphicsContext, VulkanBuffer vulkanBuffer) {
        return null;
    }

    @Override
    public void free(VulkanMemoryAllocation allocation) {

    }
}
