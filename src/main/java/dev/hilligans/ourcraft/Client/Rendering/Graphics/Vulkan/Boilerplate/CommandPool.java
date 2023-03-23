package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.*;

public class CommandPool {

    public LogicalDevice device;
    public long commandPool;

    public CommandPool(LogicalDevice device) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo.calloc(memoryStack);
            commandPoolCreateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO);
            commandPoolCreateInfo.flags(VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT);
            commandPoolCreateInfo.queueFamilyIndex(device.defaultVulkanWindow.graphicsFamily.index);
            LongBuffer longBuffer = memoryStack.callocLong(1);
            if (vkCreateCommandPool(device.device, commandPoolCreateInfo, null, longBuffer) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to create command pool");
            }
            commandPool = longBuffer.get(0);
        }
    }

    public Tuple<PointerBuffer, ArrayList<VkCommandBuffer>> allocCommandBuffers(int size) {
        PointerBuffer commandBuffers = MemoryUtil.memAllocPointer(size);
        ArrayList<VkCommandBuffer> commandBufferList = new ArrayList<>();


        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkCommandBufferAllocateInfo commandBufferAllocateInfo = VkCommandBufferAllocateInfo.calloc(memoryStack);
            commandBufferAllocateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO);
            commandBufferAllocateInfo.commandPool(commandPool);
            commandBufferAllocateInfo.level(VK_COMMAND_BUFFER_LEVEL_PRIMARY);
            commandBufferAllocateInfo.commandBufferCount(size);
            if (vkAllocateCommandBuffers(device.device, commandBufferAllocateInfo, commandBuffers) != VK_SUCCESS) {
                device.vulkanInstance.exit("Failed to allocate command buffers");
            }
        }

        for (int x = 0; x < commandBuffers.capacity(); x++) {
            VkCommandBuffer vkCommandBuffer = new VkCommandBuffer(commandBuffers.get(x), device.device);
            commandBufferList.add(vkCommandBuffer);
        }

        return new Tuple<>(commandBuffers, commandBufferList);
    }

    public void cleanup() {
        vkDestroyCommandPool(device.device,commandPool,null);
    }

}
