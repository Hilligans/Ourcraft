package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vma.VmaAllocationCreateInfo;
import org.lwjgl.util.vma.VmaAllocationInfo;
import org.lwjgl.util.vma.VmaAllocatorCreateInfo;
import org.lwjgl.util.vma.VmaVulkanFunctions;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.util.vma.Vma.*;
import static org.lwjgl.vulkan.VK10.VK_API_VERSION_1_0;

public class VMAMemoryAllocator implements IVulkanMemoryAllocator {

    public ConcurrentHashMap<LogicalDevice, Long> allocators = new ConcurrentHashMap<>();
    public LogicalDevice logicalDevice;

    public void setup() {
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

            int res = vmaCreateAllocator(allocInfo, buffer);

            allocators.put(logicalDevice, buffer.get(0));
        }
    }

    @Override
    public void free(VulkanMemoryAllocation buffer) {
        //vmaFreeMemory(allocators.get(logicalDevice), buffer.buffer);
    }

    @Override
    public VulkanMemoryAllocation allocate(long size, long bits, long alignment) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {

            VkBufferCreateInfo bufferCreateInfo = VkBufferCreateInfo.calloc(memoryStack);
            bufferCreateInfo.size(size);
            bufferCreateInfo.usage();
            bufferCreateInfo.sharingMode();
            bufferCreateInfo.flags();


            VmaAllocationInfo info = VmaAllocationInfo.calloc(memoryStack);
            VkMemoryRequirements requirements = VkMemoryRequirements.calloc(memoryStack);
            VmaAllocationCreateInfo createInfo = VmaAllocationCreateInfo.calloc(memoryStack);


            //createInfo.pool();


            //vmaAllocateMemoryForBuffer(allocators.get(logicalDevice),
            //)

        }

        return null;
    }

    @Override
    public void cleanup() {

    }
}
