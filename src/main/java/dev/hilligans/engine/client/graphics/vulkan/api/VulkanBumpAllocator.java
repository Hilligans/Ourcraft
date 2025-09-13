package dev.hilligans.engine.client.graphics.vulkan.api;

import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanBumpAllocator implements IVulkanMemoryAllocator {

    public LogicalDevice device;
    public IVulkanMemoryManager memoryManager;
    public long[] memoryHandles;
    public long[] memoryPointers;
    public long heapSize;

    public long heapPointer;

    public int currentIndex;
    public int segmentCount;

    public VulkanBumpAllocator(LogicalDevice device, IVulkanMemoryManager memoryManager, int segmentCount, long size, int memoryType) {
        long[] result = new long[segmentCount];
        long[] memoryPointers = new long[segmentCount];

        try (MemoryStack stack = MemoryStack.stackPush()) {
            VkMemoryAllocateInfo memoryAllocateInfo = VkMemoryAllocateInfo.calloc(stack);
            memoryAllocateInfo.allocationSize(size);
            memoryAllocateInfo.memoryTypeIndex(memoryType);

            LongBuffer buffer = stack.mallocLong(1);
            PointerBuffer mappedBuffer = stack.mallocPointer(1);

            for (int x = 0; x < segmentCount; x++) {
                int res = vkAllocateMemory(device.device, memoryAllocateInfo, null, buffer);
                if (res != VK_SUCCESS) {
                    throw new VulkanEngineException(res);
                }

                result[x] = buffer.get(0);


                res = vkMapMemory(device.device, result[x], 0, size, 0, mappedBuffer);
                if (res != VK_SUCCESS) {
                    throw new VulkanEngineException(res);
                }

                memoryPointers[x] = mappedBuffer.get(0);
            }
        }

        this(device, memoryManager, segmentCount, size, result, memoryPointers);
    }

    public VulkanBumpAllocator(LogicalDevice device, IVulkanMemoryManager memoryManager, int segmentCount, long size, long[] memoryHandles, long[] memoryPointers) {
        this.device = device;
        this.memoryManager = memoryManager;
        this.segmentCount = segmentCount;
        this.currentIndex = 0;
        this.memoryHandles = memoryHandles;
        this.memoryPointers = memoryPointers;
        this.heapSize = size;
    }

    public void next() {
        currentIndex = (currentIndex + 1) % segmentCount;
        heapPointer = 0;
    }

    @Override
    public void free(VulkanMemoryAllocation buffer) {
    }

    @Override
    public @Nullable VulkanMemoryAllocation allocateForBuffer(VulkanBuffer buffer) {
        long heapPointer = this.heapPointer % buffer.getAlignment();
        long offset = heapPointer;
        heapPointer += buffer.getSize() + buffer.getSize() % buffer.getAlignment();

        if(heapPointer > heapSize) {
            return null;
        }

        return new VulkanMemoryAllocation(memoryHandles[currentIndex], buffer.getSize(), offset, memoryPointers[currentIndex] + offset, 0, null, this);
    }

    @Override
    public void cleanup() {
        for(int x = 0; x < segmentCount; x++) {
            vkUnmapMemory(device.device, memoryHandles[x]);
            vkFreeMemory(device.device, memoryHandles[x], null);
        }
    }

    @Override
    public IVulkanMemoryManager getMemoryManager() {
        return memoryManager;
    }
}
