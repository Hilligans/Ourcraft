package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.CommandPool;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;

import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.*;

public class CommandBuffer {

    public LogicalDevice device;
    public CommandPool commandPool;
    public PointerBuffer commandBuffers;
    public ArrayList<VkCommandBuffer> commandBufferList;

    public CommandBuffer(LogicalDevice device, int size) {
        this.device = device;
        this.commandPool = new CommandPool(device);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            Tuple<PointerBuffer, ArrayList<VkCommandBuffer>> buffers = commandPool.allocCommandBuffers(size);
            commandBuffers = buffers.getTypeA();
            commandBufferList = buffers.getTypeB();

            //beginRecording();
        }
    }

    public VkCommandBuffer get(int index) {
        return commandBufferList.get(index);
    }

   /* public void beginRecording() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            for (int x = 0; x < commandBuffers.capacity(); x++) {
                VkCommandBufferBeginInfo vkCommandBufferBeginInfo = VkCommandBufferBeginInfo.calloc(memoryStack);
                vkCommandBufferBeginInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
                VkCommandBuffer vkCommandBuffer = new VkCommandBuffer(commandBuffers.get(x), device.device);
                commandBufferList.add(vkCommandBuffer);
                if (vkBeginCommandBuffer(vkCommandBuffer, vkCommandBufferBeginInfo) != VK_SUCCESS) {
                    device.vulkanInstance.exit("failed to begin recording command buffer");
                }
            }
        }
    }

    */

    public void beginRecording(int index) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkCommandBufferBeginInfo vkCommandBufferBeginInfo = VkCommandBufferBeginInfo.calloc(memoryStack);
            vkCommandBufferBeginInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
            VkCommandBuffer vkCommandBuffer = new VkCommandBuffer(commandBuffers.get(index), device.device);
            commandBufferList.add(vkCommandBuffer);
            if (vkBeginCommandBuffer(vkCommandBuffer, vkCommandBufferBeginInfo) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to begin recording command buffer");
            }
        }
    }

    public void createPass(RenderPass renderPass) {
        for(int x = 0; x < commandBufferList.size(); x++) {
           // renderPass.createRenderPass(x,renderPass.vulkanWindow.buffer);
        }
    }

    public void cleanup() {
        commandPool.cleanup();
        MemoryUtil.memFree(commandBuffers);
    }
}
