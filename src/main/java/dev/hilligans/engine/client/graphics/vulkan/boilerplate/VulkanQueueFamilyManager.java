package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceQueueFamilyProperties;

public class VulkanQueueFamilyManager {

    public LogicalDevice device;

    public ArrayList<QueueFamily> graphics_queues = new ArrayList<>();
    public ArrayList<QueueFamily> compute_queues = new ArrayList<>();
    public ArrayList<QueueFamily> transfer_queues = new ArrayList<>();
    public ArrayList<QueueFamily> queueFamilies = new ArrayList<>();

    public VulkanQueueFamilyManager(LogicalDevice device) {
        System.out.println("Initializing Vulkan Queue Family Manager");
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkGetPhysicalDeviceProperties(device.physicalDevice.physicalDevice, device.physicalDevice.properties);

            IntBuffer size = memoryStack.mallocInt(1);
            vkGetPhysicalDeviceQueueFamilyProperties(device.physicalDevice.physicalDevice, size, null);
            VkQueueFamilyProperties.Buffer buffer = VkQueueFamilyProperties.calloc(size.get(0));
            vkGetPhysicalDeviceQueueFamilyProperties(device.physicalDevice.physicalDevice, size, buffer);
            AtomicInteger x = new AtomicInteger();
            buffer.forEach(t -> {
                QueueFamily queueFamily = new QueueFamily(t, device, x.getAndIncrement());
                this.addQueue(queueFamily);
            });
        }
    }

    public void addQueue(QueueFamily queueFamily) {
        if(queueFamily.hasGraphics()) {
            graphics_queues.add(queueFamily);
        }
        if(queueFamily.hasCompute()) {
            compute_queues.add(queueFamily);
        }
        if(queueFamily.hasTransfer()) {
            transfer_queues.add(queueFamily);
        }
        queueFamilies.add(queueFamily);
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
        return findSupported(graphics, compute, transfer, present);
    }

    public void cleanup() {
        //vkDestroyQue
    }

    public QueueFamily getTransferQueue() {
        int bestScore = Integer.MAX_VALUE;
        QueueFamily bestQueue = null;
        for(int x = 0; x < queueFamilies.size(); x++) {
            QueueFamily queueFamily = queueFamilies.get(x);

            if(!queueFamily.hasTransfer()) {
                continue;
            }

            int score = queueFamily.hasGraphics() ? 2 : 0;
            score += queueFamily.hasCompute() ? 1 : 0;

            if(score < bestScore) {
                bestScore = score;
                bestQueue = queueFamily;
            }
        }
        return bestQueue;
    }

    public SingleUseCommandBuffer getTransferBuffer() {
        return new SingleUseCommandBuffer(device, getTransferQueue().findQueue());
    }

    public QueueFamily findSupported(boolean graphics, boolean compute, boolean transfer, boolean present) {
        int best = -1;
        int bestIndex = -1;
        for (int x = 0; x < queueFamilies.size(); x++) {
            QueueFamily queueFamily = queueFamilies.get(x);
            int score = 0;
            if ((graphics && !queueFamily.hasGraphics()) || (compute && !queueFamily.hasCompute()) || (transfer && !queueFamily.hasTransfer())) {
                //System.out.println("graphics=" + queueFamily.hasGraphics());
                //System.out.println("compute=" + queueFamily.hasCompute());
                //System.out.println("transfer=" + queueFamily.hasTransfer());
                continue;
            }
            score += graphics == queueFamily.hasGraphics() ? 1 : 0;
            score += compute == queueFamily.hasCompute() ? 1 : 0;
            score += transfer == queueFamily.hasTransfer() ? 1 : 0;
            score += present == queueFamily.hasPresent() ? 1 : 0;
            if (score > best) {
                best = score;
                bestIndex = x;
            }
        }
        if (bestIndex == -1) {
            throw new VulkanEngineException("Failed to find queue family graphics=" + graphics + " compute=" + compute + " transfer=" + transfer + " present=" + present);
        }
        System.out.println(queueFamilies.get(bestIndex));
        return queueFamilies.get(bestIndex);
    }
}
