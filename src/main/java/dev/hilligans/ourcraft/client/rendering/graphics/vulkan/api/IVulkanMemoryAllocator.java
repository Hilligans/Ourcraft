package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import org.jetbrains.annotations.Nullable;

public interface IVulkanMemoryAllocator {

    void free(VulkanMemoryAllocation buffer);

    @Nullable
    VulkanMemoryAllocation allocate(long size, long bits, long alignment);

    void cleanup();

}
