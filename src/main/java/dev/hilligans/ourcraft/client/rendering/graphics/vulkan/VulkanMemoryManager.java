package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanMemoryManager {

    public MemoryHeap primaryHeap;
    public MemoryHeap barHeap;
    public MemoryHeap stagingHeap;
    public MemoryHeap[] memoryHeaps;
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

            memoryHeaps = new MemoryHeap[memProperties.memoryHeapCount()];
            long largestHeapSize = 0;
            for (int x = 0; x < memProperties.memoryHeapCount(); x++) {
                MemoryHeap heap = new MemoryHeap(device, this, x, memProperties);
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


    static class MemoryHeap {
        public int heapIndex;
        public ArrayList<Tuple<Integer, Integer>> types = new ArrayList<>(4);
        public long size;

        public AtomicLong allocatedSize = new AtomicLong(0);

        public boolean deviceLocal;
        public boolean cpuAccessible;

        public LogicalDevice device;
        public VulkanMemoryManager memoryManager;

        public MemoryHeap(LogicalDevice device, VulkanMemoryManager memoryManager, int heapIndex, VkPhysicalDeviceMemoryProperties memoryProperties) {
            this.device = device;
            this.memoryManager = memoryManager;
            this.heapIndex = heapIndex;
            this.size = memoryProperties.memoryHeaps(heapIndex).size();
            for(int x = 0; x < memoryProperties.memoryTypeCount(); x++) {
                if(memoryProperties.memoryTypes(x).heapIndex() == heapIndex) {
                    int flags = memoryProperties.memoryTypes(x).propertyFlags();
                    types.add(new Tuple<>(flags, x));
                    if((flags & VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT) != 0) {
                        cpuAccessible = true;
                    }
                    if((flags & VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT) != 0) {
                        deviceLocal = true;
                    }
                }
            }
        }

        public int getMemoryType(int filter, int flags) {
            for (Tuple<Integer, Integer> type : types) {
                if ((type.typeA & flags) == flags && (filter & (1 << type.typeB)) != 0) {
                    return type.typeB;
                }
            }
            return -1;
        }

        public MemoryRegion allocateRegion(long size, int type) {
            try(MemoryStack memoryStack = MemoryStack.stackPush()) {
                VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.calloc(memoryStack);
                allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
                allocInfo.allocationSize(size);
                allocInfo.memoryTypeIndex(type);

                LongBuffer mem = MemoryStack.stackCallocLong(1);

                int result = vkAllocateMemory(device.device, allocInfo, null, mem);
                if(result == VK_SUCCESS) {
                    allocatedSize.addAndGet(size);
                    return new MemoryRegion(this, mem.get(0), size, (int)memoryManager.regionWidth);
                } else if((result & VK_ERROR_OUT_OF_DEVICE_MEMORY) == VK_ERROR_OUT_OF_DEVICE_MEMORY || (result & VK_ERROR_OUT_OF_HOST_MEMORY) == VK_ERROR_OUT_OF_HOST_MEMORY) {
                    return null;
                } else {
                    throw new VulkanEngineException("Failed to allocate memory");
                }
            }
        }
    }
}
