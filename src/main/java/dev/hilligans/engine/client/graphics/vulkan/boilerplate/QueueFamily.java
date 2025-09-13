package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.vulkan.VK10.*;

public class QueueFamily {

    public VkQueueFamilyProperties properties;
    public LogicalDevice device;
    public int index;
    public int queueIndex;
    public boolean supportsPresent = false;
    public Queue[] queues;

    public QueueFamily(VkQueueFamilyProperties properties, LogicalDevice device, int index) {
        this.index = index;
        this.properties = properties;
        this.device = device;
        queues = new Queue[getQueueCount()];
    }

    public VkDeviceQueueCreateInfo createInfo(MemoryStack memoryStack) {
        VkDeviceQueueCreateInfo queueCreateInfo = VkDeviceQueueCreateInfo.calloc(memoryStack);
        queueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO);
        queueCreateInfo.queueFamilyIndex(index);
        FloatBuffer floatBuffer = memoryStack.floats(1f / getQueueCount());
        queueCreateInfo.pQueuePriorities(floatBuffer);
        return queueCreateInfo;
    }

    public void testPresent(long surface) {
        supportsPresent = VkInterface.vkGetPhysicalDeviceSurfaceSupportKHR(device.physicalDevice.physicalDevice,index,surface);
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

    public synchronized Queue findQueue() {
        Queue queue;
        if(queueIndex < getQueueCount()) {
            queue = getQueue(queueIndex);
        } else {
            queue = findSmallestQueueOwner();
        }
        queue.queueOwners.incrementAndGet();
        queueIndex++;
        return queue;
    }

    public synchronized Queue findSmallestQueueOwner() {
        Queue smallest = getQueue(0);
        int count = smallest.queueOwners.get();
        for(Queue queue : queues) {
            if(queue != null) {
                if(queue.queueOwners.get() < count) {
                    smallest = queue;
                    count = queue.queueOwners.get();
                }
            }
        }
        return smallest;
    }

    public synchronized Queue getQueue(int index) {
        if(queues[index] == null) {
            try(MemoryStack memoryStack = MemoryStack.stackPush()) {
                PointerBuffer pointerBuffer = memoryStack.mallocPointer(1);
                vkGetDeviceQueue(device.device, this.index, index, pointerBuffer);
                queues[index] = new Queue(device, pointerBuffer.get(0), index, this.index, this);
            }
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

    @Override
    public String toString() {
        return "QueueFamily{" +
                "compute=" + hasCompute() +
                ", graphics=" + hasGraphics() +
                ", present=" + hasPresent() +
                ", transfer=" + hasTransfer() +
                ", device=" + device +
                ", index=" + index +
                ", queueIndex=" + queueIndex +
                ", supportsPresent=" + supportsPresent +
                ", queues=" + Arrays.toString(queues) +
                '}';
    }

    public void cleanup() {
        //vkDestroy
        properties.free();
    }


}
