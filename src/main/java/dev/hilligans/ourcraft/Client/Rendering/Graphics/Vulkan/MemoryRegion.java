package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanBuffer;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngineException;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanMemoryManager;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkBufferCreateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;

public class MemoryRegion {

    public ArrayList<Tuple<Long, Long>> sizeOffsets;

    public VulkanMemoryManager.MemoryHeap memoryHeap;
    public long memory;
    public long size;
    public long biggestRegion;
    public int regionWidth;

    public MemoryRegion(VulkanMemoryManager.MemoryHeap memoryHeap, long memory, long size, int regionWidth) {
        this.memoryHeap = memoryHeap;
        this.memory = memory;
        this.size = size;
        this.regionWidth = regionWidth;
    }

    public VulkanBuffer createBuffer(long size, int usage) {
        size += size % regionWidth;
        if(size > biggestRegion) {
            return null;
        }

        //long offset =

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCreateInfo bufferInfo = VkBufferCreateInfo.calloc(memoryStack);
            bufferInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
            bufferInfo.size(size);
            bufferInfo.usage(usage);
            bufferInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);

            LongBuffer buf = MemoryStack.stackCallocLong(1);

            if (vkCreateBuffer(memoryHeap.device.device, bufferInfo, null, buf) != VK_SUCCESS) {
                throw new VulkanEngineException("Failed to allocate buffer");
            }
            vkBindBufferMemory(memoryHeap.device.device, buf.get(0), memory, 0);

            return new VulkanBuffer(size, buf.get(0), memory, 0);
        }
    }


    public long buildBuffer(int offset) {
        Tuple<Long, Long> buf = sizeOffsets.get(offset);

        long remaining = buf.typeA - offset;
        sizeOffsets.remove(buf);
        if(remaining != 0) {
            add(new Tuple<>(remaining, buf.typeB + offset));
        }
        return 0;
    }

    public void add(Tuple<Long, Long> toAdd) {
        for(int x = sizeOffsets.size() - 1; x >= 0; x--) {
            Tuple<Long, Long> buf = sizeOffsets.get(x);
            if(buf.typeA < toAdd.typeA) {
                sizeOffsets.add(x + 1, toAdd);
            }
        }
    }

    public int findSmallestIndex(long size) {
        for(int x = sizeOffsets.size() - 1; x >= 0; x--) {
            Tuple<Long, Long> buf = sizeOffsets.get(x);
            if(buf.typeA > size) {
                return x;
            }
        }
        return -1;
    }

    public void free() {
        VK10.vkFreeMemory(memoryHeap.device.device, memory, null);
        memoryHeap.allocatedSize.addAndGet(-size);
    }
}
