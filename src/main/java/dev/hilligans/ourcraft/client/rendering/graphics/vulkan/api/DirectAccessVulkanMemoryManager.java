package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;

public class DirectAccessVulkanMemoryManager implements IVulkanMemoryManager {

    @Override
    public boolean canUse(LogicalDevice device) {
        return false;
    }
}
