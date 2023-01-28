package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.FloatBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class QueueFamily {

    public VkQueueFamilyProperties properties;
    public PhysicalDevice physicalDevice;
    public int index;
    public boolean supportsPresent = false;
    public Queue[] queues;

    public QueueFamily(VkQueueFamilyProperties properties, PhysicalDevice physicalDevice, int index) {
        this.index = index;
        this.properties = properties;
        this.physicalDevice = physicalDevice;
        queues = new Queue[getQueueCount()];
    }

    public VkDeviceQueueCreateInfo createInfo(MemoryStack memoryStack) {
        VkDeviceQueueCreateInfo queueCreateInfo = VkDeviceQueueCreateInfo.callocStack(memoryStack);
        queueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO);
        queueCreateInfo.queueFamilyIndex(index);
        FloatBuffer floatBuffer = memoryStack.floats(1f / getQueueCount());
        queueCreateInfo.pQueuePriorities(floatBuffer);
        return queueCreateInfo;
    }

    public void testPresent() {
        supportsPresent = VkInterface.vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice.physicalDevice,index,physicalDevice.logicalDevice.getDefaultWindow().surface);
    }

    public boolean hasCompute() {
        return (properties.queueFlags() & VK_QUEUE_COMPUTE_BIT) != 0;
    }

    public boolean hasGraphics() {
        return (properties.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0;
    }

    public boolean hasTransfer() {
        return (properties.queueFlags() & VK_QUEUE_TRANSFER_BIT) != 0;
    }

    public boolean hasBinding() {
        return (properties.queueFlags() & VK_QUEUE_SPARSE_BINDING_BIT) != 0;
    }

    public boolean hasPresent() {
        return supportsPresent;
    }

    public int getQueueCount() {
        return properties.queueCount();
    }

    public Queue getQueue(int index) {
        if(queues[index] == null) {
            PointerBuffer pointerBuffer = VkInterface.vkGetDeviceQueue(physicalDevice.vulkanInstance.logicalDevice.device, this.index, index);
            queues[index] = new Queue(physicalDevice.vulkanInstance, pointerBuffer.get(0));
        }
        return queues[index];
    }

    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        if(hasGraphics()) {
            stringBuilder.append("Graphics, ");
        }
        if(hasCompute()) {
            stringBuilder.append("Compute, ");
        }
        if(hasTransfer()) {
            stringBuilder.append("Transfer");
        }


        return stringBuilder.toString();
    }

    public int getPriority() {
        return (hasTransfer() ? 1 : 0) + (hasCompute() ? 1 : 0) + (hasGraphics() ? 1 : 0);
    }

    public void cleanup() {
        properties.free();
    }

}
