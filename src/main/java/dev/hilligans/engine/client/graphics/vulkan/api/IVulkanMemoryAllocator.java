package dev.hilligans.engine.client.graphics.vulkan.api;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import org.jetbrains.annotations.Nullable;

public interface IVulkanMemoryAllocator {

    void free(VulkanMemoryAllocation buffer);

    @Nullable
    VulkanMemoryAllocation allocateForBuffer(VulkanBuffer buffer);

    void cleanup();

    IVulkanMemoryManager getMemoryManager();
}
