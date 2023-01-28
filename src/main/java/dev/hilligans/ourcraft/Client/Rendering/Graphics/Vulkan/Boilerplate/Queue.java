package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import org.lwjgl.vulkan.VkQueue;

public class Queue {

    public VkQueue vkQueue;
    public VulkanInstance vulkanInstance;

    public Queue(VulkanInstance vulkanInstance, long handle) {
        this.vulkanInstance = vulkanInstance;
        vkQueue = new VkQueue(handle,vulkanInstance.getDefaultDevice().logicalDevice.device);
    }

    public void cleanup() {
    }




}
