package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IVulkanMemoryAllocator extends IRegistryElement {

    void setup(LogicalDevice logicalDevice);

    void free(LogicalDevice logicalDevice);

    VulkanBuffer allocate(LogicalDevice logicalDevice, long bits);

    default String getResourceType() {
        return "vulkanMemoryAllocator";
    }
}
