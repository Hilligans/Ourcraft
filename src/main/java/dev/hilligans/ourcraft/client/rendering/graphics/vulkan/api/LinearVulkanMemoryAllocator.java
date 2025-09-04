package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.VulkanEngineException;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class LinearVulkanMemoryAllocator implements IVulkanMemoryAllocator {

    public LogicalDevice device;
    public long[] memoryHandles;
    public long[] memoryPointers;
    public long heapSize;

    public long heapPointer;

    public int currentIndex;
    public int segmentCount;

    public LinearVulkanMemoryAllocator(LogicalDevice device, int segmentCount, long size, int memoryType) {
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

        this(device, segmentCount, size, result, memoryPointers);
    }

    public LinearVulkanMemoryAllocator(LogicalDevice device, int segmentCount, long size, long[] memoryHandles, long[] memoryPointers) {
        this.device = device;
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
    public VulkanMemoryAllocation allocate(long size, long bits, long alignment) {

        long heapPointer = this.heapPointer % alignment;
        long offset = heapPointer;
        heapPointer += size + size % alignment;

        if(heapPointer > heapSize) {
            return null;
        }

        return new VulkanMemoryAllocation(memoryHandles[currentIndex], size, offset, memoryPointers[currentIndex] + offset);
    }

    @Override
    public void cleanup() {
        for(int x = 0; x < segmentCount; x++) {
            vkUnmapMemory(device.device, memoryHandles[x]);
            vkFreeMemory(device.device, memoryHandles[x], null);
        }
    }
}
