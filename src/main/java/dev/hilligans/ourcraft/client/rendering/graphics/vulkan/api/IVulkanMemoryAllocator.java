package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import org.jetbrains.annotations.Nullable;

public interface IVulkanMemoryAllocator {

    void free(VulkanMemoryAllocation buffer);

    @Nullable
    VulkanMemoryAllocation allocateForBuffer(VulkanBuffer buffer);

    void cleanup();

    IVulkanMemoryManager getMemoryManager();
}
