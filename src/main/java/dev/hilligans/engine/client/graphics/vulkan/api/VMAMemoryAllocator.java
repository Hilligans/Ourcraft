package dev.hilligans.engine.client.graphics.vulkan.api;

import dev.hilligans.engine.client.graphics.vulkan.VulkanMemoryHeap;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VkInterface;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vma.VmaAllocationCreateInfo;
import org.lwjgl.util.vma.VmaAllocationInfo;
import org.lwjgl.util.vma.VmaAllocatorCreateInfo;
import org.lwjgl.util.vma.VmaVulkanFunctions;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.util.List;

import static org.lwjgl.util.vma.Vma.*;
import static org.lwjgl.vulkan.VK10.VK_API_VERSION_1_0;
import static org.lwjgl.vulkan.VK10.vkGetBufferMemoryRequirements;

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
        vmaFreeMemory(allocator, buffer.allocation());
    }

    @Override
    public VulkanMemoryAllocation allocateForBuffer(VulkanBuffer vulkanBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {


            VkMemoryRequirements requirements = VkMemoryRequirements.calloc(memoryStack);
            vkGetBufferMemoryRequirements(logicalDevice.device, vulkanBuffer.getVkBuffer(), requirements);


            PointerBuffer buffer = memoryStack.mallocPointer(1);

            VmaAllocationCreateInfo createInfo = VmaAllocationCreateInfo.calloc(memoryStack);
            createInfo.memoryTypeBits(allowedMemoryTypes);



            VmaAllocationInfo info = VmaAllocationInfo.calloc(memoryStack);

            VkInterface.check(vmaAllocateMemory(allocator, requirements, createInfo, buffer, info),
                    "Failed to allocate vmaMemory");

            //VulkanMemoryAllocation allocation = new VulkanMemoryAllocation(info.deviceMemory(), info.size(), info.offset(), info.)

            PointerBuffer buf = memoryStack.mallocPointer(1);
            //vmaMapMemory(allocator, );

            //vmaFreeMemory();
        }

        return null;
    }

    @Override
    public void cleanup() {
        vmaDestroyAllocator(allocator);
    }

    @Override
    public IVulkanMemoryManager getMemoryManager() {
        return null;
    }
}
