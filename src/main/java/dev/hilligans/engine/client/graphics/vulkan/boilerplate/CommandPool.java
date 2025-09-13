package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.vulkan.EXTDebugMarker.VK_STRUCTURE_TYPE_DEBUG_MARKER_OBJECT_NAME_INFO_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.VK_DEBUG_REPORT_OBJECT_TYPE_COMMAND_BUFFER_EXT;
import static org.lwjgl.vulkan.VK10.*;

public class CommandPool {

    public LogicalDevice device;
    public long commandPool;
    public Queue queue;
    public ArrayList<VkCommandBuffer> commandBuffers = new ArrayList<>();

    public CommandPool(LogicalDevice device, Queue queue) {
        this.device = device;
        this.queue = queue;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo.calloc(memoryStack);
            commandPoolCreateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO);
            commandPoolCreateInfo.flags(VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT);
            commandPoolCreateInfo.queueFamilyIndex(queue.queueFamilyIndex);
            LongBuffer longBuffer = memoryStack.callocLong(1);
            if (vkCreateCommandPool(device.device, commandPoolCreateInfo, null, longBuffer) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to create command pool");
            }
            commandPool = longBuffer.get(0);
        }
    }

    public void allocCommandBuffers(int size) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer commandBuffers = memoryStack.mallocPointer(size);

            VkCommandBufferAllocateInfo commandBufferAllocateInfo = VkCommandBufferAllocateInfo.calloc(memoryStack);
            commandBufferAllocateInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO);
            commandBufferAllocateInfo.commandPool(commandPool);
            commandBufferAllocateInfo.level(VK_COMMAND_BUFFER_LEVEL_PRIMARY);
            commandBufferAllocateInfo.commandBufferCount(size);
            if (vkAllocateCommandBuffers(device.device, commandBufferAllocateInfo, commandBuffers) != VK_SUCCESS) {
                device.vulkanInstance.exit("Failed to allocate command buffers");
            }

            for (int x = 0; x < commandBuffers.capacity(); x++) {
                VkCommandBuffer vkCommandBuffer = new VkCommandBuffer(commandBuffers.get(x), device.device);
                VkDebugMarkerObjectNameInfoEXT nameInfo = VkDebugMarkerObjectNameInfoEXT.calloc(memoryStack);
                nameInfo.sType(VK_STRUCTURE_TYPE_DEBUG_MARKER_OBJECT_NAME_INFO_EXT);
                nameInfo.objectType(VK_DEBUG_REPORT_OBJECT_TYPE_COMMAND_BUFFER_EXT);
                nameInfo.object(commandBuffers.get(x));
                nameInfo.pObjectName(MemoryStack.stackASCII("Primary Command Buffer " + x));

                //long pfnDebugMarkerSetObjectName = vkGetDeviceProcAddr(device.device, memoryStack.ASCII("vkDebugMarkerSetObjectNameEXT"));
                //JNI.invokePPI(device.device.address(), memAddress(memoryStack.ASCII("name")), pfnDebugMarkerSetObjectName);


               // pfnDebugMarkerSetObjectName(device, &nameInfo);
                this.commandBuffers.add(vkCommandBuffer);
            }
        }
    }

    public void beginRecording(int index) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkCommandBufferBeginInfo vkCommandBufferBeginInfo = VkCommandBufferBeginInfo.calloc(memoryStack);
            vkCommandBufferBeginInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
            if (vkBeginCommandBuffer(commandBuffers.get(index), vkCommandBufferBeginInfo) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to begin recording command buffer");
            }
        }
    }

    public void endRecording(int index) {
        vkEndCommandBuffer(commandBuffers.get(index));
    }

    public void cleanup() {
        vkDestroyCommandPool(device.device,commandPool,null);
    }

}
