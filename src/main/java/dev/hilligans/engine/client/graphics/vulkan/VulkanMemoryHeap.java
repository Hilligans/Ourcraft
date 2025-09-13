package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.vulkan.VK10.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT;
import static org.lwjgl.vulkan.VK10.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT;

public class VulkanMemoryHeap {

    public int heapIndex;
    public ArrayList<VulkanMemoryType> types = new ArrayList<>(4);
    public long size;

    public AtomicLong allocatedSize = new AtomicLong(0);

    public boolean deviceLocal;
    public boolean cpuAccessible;

    public LogicalDevice device;
    public VulkanMemoryManager memoryManager;

    public VulkanMemoryHeap(LogicalDevice device, VulkanMemoryManager memoryManager, int heapIndex, VkPhysicalDeviceMemoryProperties memoryProperties) {
        this.device = device;
        this.memoryManager = memoryManager;
        this.heapIndex = heapIndex;
        this.size = memoryProperties.memoryHeaps(heapIndex).size();
        for (int x = 0; x < memoryProperties.memoryTypeCount(); x++) {
            if (memoryProperties.memoryTypes(x).heapIndex() == heapIndex) {
                int flags = memoryProperties.memoryTypes(x).propertyFlags();
                types.add(new VulkanMemoryType(this, flags, x));
                if ((flags & VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT) != 0) {
                    cpuAccessible = true;
                }
                if ((flags & VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT) != 0) {
                    deviceLocal = true;
                }
            }
        }
    }

    public int getMemoryType(int filter, int flags) {
        for (VulkanMemoryType type : types) {
            if ((type.flags() & flags) == flags && (filter & (1 << type.memoryTypeIndex())) != 0) {
                return type.memoryTypeIndex();
            }
        }
        return -1;
    }

    public ArrayList<VulkanMemoryType> getMemoryTypes() {
        return types;
    }

    public record VulkanMemoryType(VulkanMemoryHeap heap, int flags, int memoryTypeIndex) {}
}

