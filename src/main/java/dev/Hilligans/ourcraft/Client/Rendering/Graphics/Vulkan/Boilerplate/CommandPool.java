package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class CommandPool {

    public LogicalDevice device;
    public long commandPool;

    public CommandPool(LogicalDevice device) {
        this.device = device;
        VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo.calloc();
        commandPoolCreateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO);
        commandPoolCreateInfo.queueFamilyIndex(device.defaultVulkanWindow.graphicsFamily.index);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.callocLong(1);
            if (vkCreateCommandPool(device.device, commandPoolCreateInfo, null, longBuffer) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to create command pool");
            }
            commandPool = longBuffer.get(0);
        }
    }

    public void cleanup() {
        vkDestroyCommandPool(device.device,commandPool,null);
    }



}
