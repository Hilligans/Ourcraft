package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.VulkanMemoryHeap;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vma.*;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.util.List;

import static org.lwjgl.util.vma.Vma.*;
import static org.lwjgl.vulkan.VK10.VK_API_VERSION_1_0;

public class VMAMemoryAllocator implements IVulkanMemoryAllocator {

    public LogicalDevice logicalDevice;
    public int allocator;

    public int allowedMemoryTypes = 0;
    public List<VulkanMemoryHeap> managedHeaps;

    public VMAMemoryAllocator(LogicalDevice logicalDevice) {
        this.logicalDevice = logicalDevice;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            VmaVulkanFunctions functions = VmaVulkanFunctions.malloc(stack);
            //functions.vkGetInstanceProcAddr()

            VmaAllocatorCreateInfo allocInfo = VmaAllocatorCreateInfo.calloc(stack);
            allocInfo.physicalDevice(logicalDevice.physicalDevice.physicalDevice);
            allocInfo.device(logicalDevice.device);
            allocInfo.instance(logicalDevice.vulkanInstance.vkInstance);
            allocInfo.vulkanApiVersion(VK_API_VERSION_1_0);
            allocInfo.pVulkanFunctions(functions);

            PointerBuffer buffer = stack.mallocPointer(1);

            allocator = vmaCreateAllocator(allocInfo, buffer);
        }
    }

    public VMAMemoryAllocator(LogicalDevice device, List<VulkanMemoryHeap> managedHeaps) {
        this(device);
        this.managedHeaps = managedHeaps;

        for(VulkanMemoryHeap heap : managedHeaps) {
            for(VulkanMemoryHeap.VulkanMemoryType type : heap.types) {
                this.allowedMemoryTypes |= 1 << type.memoryTypeIndex();
            }
        }
    }

    @Override
    public void free(VulkanMemoryAllocation buffer) {
        //vmaFreeMemory(allocators.get(logicalDevice), buffer.buffer);
    }

    @Override
    public VulkanMemoryAllocation allocateForBuffer(VulkanBuffer vulkanBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {

            VkBufferCreateInfo bufferCreateInfo = VkBufferCreateInfo.calloc(memoryStack);
            bufferCreateInfo.size(vulkanBuffer.size);
            bufferCreateInfo.usage();
            bufferCreateInfo.sharingMode();
            bufferCreateInfo.flags();



            VmaAllocationInfo info = VmaAllocationInfo.calloc(memoryStack);
            VkMemoryRequirements requirements = VkMemoryRequirements.calloc(memoryStack);
           // requirements.
            VmaAllocationCreateInfo createInfo = VmaAllocationCreateInfo.calloc(memoryStack);
            createInfo.memoryTypeBits(allowedMemoryTypes);

            //vmaAllocateMem



            //vmaAllocateMemoryForBuffer(allocators.get(logicalDevice),
            //)

        }

        return null;
    }

    @Override
    public void cleanup() {
        vmaDestroyAllocator(allocator);
    }
}
