package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

public class VulkanImage {

    public long image;
    public long offset;
    public long memory;

    public LogicalDevice device;

    public long getVkImage() {
        return image;
    }

    public int getVkImageLayout() {
        return 0;
    }
}
