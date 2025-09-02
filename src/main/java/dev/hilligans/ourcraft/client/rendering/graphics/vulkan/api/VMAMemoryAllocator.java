package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vma.VmaAllocatorCreateInfo;
import org.lwjgl.util.vma.VmaVulkanFunctions;

import static org.lwjgl.util.vma.Vma.vmaCreateAllocator;
import static org.lwjgl.vulkan.VK10.VK_API_VERSION_1_0;

public class VMAMemoryAllocator implements IVulkanMemoryAllocator {



    @Override
    public void setup(LogicalDevice logicalDevice) {
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

            int allocator = vmaCreateAllocator(allocInfo, buffer);
        }
    }

    @Override
    public void free(LogicalDevice logicalDevice) {

    }

    @Override
    public VulkanBuffer allocate(LogicalDevice logicalDevice, long bits) {
        return null;
    }

    @Override
    public String getResourceName() {
        return "VMAMemoryAllocator";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
