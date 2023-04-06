package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkQueueSubmit;

public class Queue {

    public VkQueue vkQueue;
    public LogicalDevice device;
    public int queueIndex;
    public int queueFamilyIndex;

    //The count of how many different owners there are of this queue
    public AtomicInteger queueOwners = new AtomicInteger();

    public Queue(LogicalDevice device, long handle, int queueIndex, int queueFamilyIndex) {
        this.device = device;
        this.queueFamilyIndex = queueFamilyIndex;
        this.queueIndex = queueIndex;
        System.out.println("device " + device.device + " handle " + handle);
        this.vkQueue = new VkQueue(handle, device.device);
    }

    public CommandPool allocateCommandPool() {
        return new CommandPool(device, this);
    }

    public synchronized boolean submitQueue(VkSubmitInfo submitInfo, long fence) {
        //return false;
        return vkQueueSubmit(vkQueue, submitInfo, fence) == VK_SUCCESS;
    }

    public void cleanup() {

    }
}
