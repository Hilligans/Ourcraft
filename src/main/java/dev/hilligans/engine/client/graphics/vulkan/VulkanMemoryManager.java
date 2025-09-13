package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanMemoryManager {

    public VulkanMemoryHeap primaryHeap;
    public VulkanMemoryHeap barHeap;
    public VulkanMemoryHeap stagingHeap;
    public VulkanMemoryHeap[] memoryHeaps;
    public LogicalDevice device;

    public int maxHeapAllocations;
    public int allocations = 0;
    public long regionWidth;



    public VulkanMemoryManager(LogicalDevice device) {
        this.device = device;
        this.maxHeapAllocations = device.physicalDevice.deviceLimits.maxMemoryAllocationCount();
        regionWidth = device.physicalDevice.deviceLimits.nonCoherentAtomSize();
        if(regionWidth > 16) {
             regionWidth = 16;
        }

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPhysicalDeviceMemoryProperties memProperties = VkPhysicalDeviceMemoryProperties.calloc(memoryStack);
            vkGetPhysicalDeviceMemoryProperties(device.physicalDevice.physicalDevice, memProperties);

            memoryHeaps = new VulkanMemoryHeap[memProperties.memoryHeapCount()];
            long largestHeapSize = 0;
            for (int x = 0; x < memProperties.memoryHeapCount(); x++) {
                VulkanMemoryHeap heap = new VulkanMemoryHeap(device, this, x, memProperties);
                memoryHeaps[x] = heap;
                if(heap.deviceLocal && heap.size > largestHeapSize) {
                    largestHeapSize = heap.size;
                    primaryHeap = heap;
                }
                if(heap.cpuAccessible && heap.deviceLocal) {
                    barHeap = heap;
                }
                if(heap.cpuAccessible && !heap.deviceLocal) {
                    stagingHeap = heap;
                }
            }

        }
    }

    public void createFramebufferStagingBuffers(int frameBufferCount, int frameBufferMemorySize) {
        if(barHeap == null || (long) frameBufferCount * frameBufferMemorySize > barHeap.size / 2) {

        } else {

        }
    }

    public void allocateMemoryRegion(long size, int properties) {
        size += size % regionWidth;

    }

    public VulkanBuffer allocateBuffer(int size, int usage, int flags, boolean singleUse, boolean allowSharing, boolean allowDirectWriting) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            if(allowDirectWriting && barHeap != null) {
                if(size < barHeap.size / 2) {
                    if (barHeap == primaryHeap || singleUse) {
                        VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.calloc(memoryStack);
                        allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
                        //allocInfo.allocationSize(memRequirements.size());
                        //allocInfo.memoryTypeIndex(findMemoryType(memRequirements.memoryTypeBits(), properties));

                        LongBuffer mem = MemoryStack.stackCallocLong(1);

                        if (vkAllocateMemory(device.device, allocInfo, null, mem) != VK_SUCCESS){
                            throw new VulkanEngineException("Failed to allocate memory");
                        }
                    }
                }
            }


            VkBufferCreateInfo bufferInfo = VkBufferCreateInfo.calloc(memoryStack);
            bufferInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
            bufferInfo.size(size);
            bufferInfo.usage(usage);
            bufferInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);

            LongBuffer buf = MemoryStack.stackCallocLong(1);

            if (vkCreateBuffer(device.device, bufferInfo, null, buf) != VK_SUCCESS) {
                throw new VulkanEngineException("Failed to allocate buffer");
            }
            long buffer = buf.get(0);

            VkMemoryRequirements memRequirements = VkMemoryRequirements.calloc(memoryStack);
            vkGetBufferMemoryRequirements(device.device, buf.get(0), memRequirements);

            VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.calloc(memoryStack);
            allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
            allocInfo.allocationSize(memRequirements.size());
            allocInfo.memoryTypeIndex(findMemoryType(memRequirements.memoryTypeBits(), flags));

            LongBuffer mem = MemoryStack.stackCallocLong(1);

            if (vkAllocateMemory(device.device, allocInfo, null, mem) != VK_SUCCESS) {
                throw new VulkanEngineException("Failed to allocate memory");
            }
            long memory = mem.get(0);
            vkBindBufferMemory(device.device, buffer, memory, 0);
        }
        return null;
    }


    public int findMemoryType(int filter, int properties) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPhysicalDeviceMemoryProperties memProperties = VkPhysicalDeviceMemoryProperties.calloc(memoryStack);
            vkGetPhysicalDeviceMemoryProperties(device.physicalDevice.physicalDevice, memProperties);

            for (int i = 0; i < memProperties.memoryTypeCount(); i++) {
                if ((filter & (1 << i)) != 0 && (memProperties.memoryTypes(i).propertyFlags() & properties) == properties) {
                   // this.properties = memProperties.memoryTypes(i).propertyFlags();
                    return i;
                }
            }
            device.vulkanInstance.exit("Failed to find memory");
        }
        return -1;
    }

    public boolean hasRebar() {
        for(VulkanMemoryHeap heap : memoryHeaps) {
            if(heap.deviceLocal && !heap.cpuAccessible) {
                return false;
            }
        }
        return true;
    }

    public boolean hasStagingHeap() {
        for(VulkanMemoryHeap heap : memoryHeaps) {
            if(!heap.deviceLocal && heap.cpuAccessible) {
                return true;
            }
        }
        return false;
    }
}
