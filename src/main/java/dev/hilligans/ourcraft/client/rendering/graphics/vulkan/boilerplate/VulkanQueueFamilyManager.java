package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceQueueFamilyProperties;

public class VulkanQueueFamilyManager {

    public ArrayList<QueueFamily> queueFamilies = new ArrayList<>();
    public LogicalDevice device;
    public QueueGroup queueGroup;

    public VulkanQueueFamilyManager(LogicalDevice device) {
        this.device = device;
        this.queueGroup = new QueueGroup();
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkGetPhysicalDeviceProperties(device.physicalDevice.physicalDevice, device.physicalDevice.properties);
            IntBuffer size = memoryStack.mallocInt(1);
            vkGetPhysicalDeviceQueueFamilyProperties(device.physicalDevice.physicalDevice, size, null);
            VkQueueFamilyProperties.Buffer buffer = VkQueueFamilyProperties.calloc(size.get(0));
            vkGetPhysicalDeviceQueueFamilyProperties(device.physicalDevice.physicalDevice, size, buffer);
            AtomicInteger x = new AtomicInteger();
            buffer.forEach(t -> {
                QueueFamily queueFamily = new QueueFamily(t, device, x.getAndIncrement());
                queueFamilies.add(queueFamily);
                queueGroup.addQueue(queueFamily);
            });
        }
    }

    public SingleUseCommandBuffer getSingleCommandPool(boolean graphics, boolean compute, boolean transfer, boolean present) {
        return new SingleUseCommandBuffer(device, getQueue(graphics, compute, transfer, present));
    }

    public void testSurface(long surface) {
        for(QueueFamily queueFamily : queueFamilies) {
            queueFamily.testPresent(surface);
        }
    }

    public Queue getQueue(boolean graphics, boolean compute, boolean transfer, boolean present) {
        return getQueueFamily(graphics, compute, transfer, present).findQueue();
    }

    public QueueFamily getQueueFamily(boolean graphics, boolean compute, boolean transfer, boolean present) {
        return queueGroup.findSupported(graphics, compute, transfer, present);
    }

    public void cleanup() {
        //vkDestroyQue
    }
}
