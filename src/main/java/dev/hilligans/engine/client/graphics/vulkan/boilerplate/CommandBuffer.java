package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.vulkan.VkCommandBuffer;

import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.vkResetCommandBuffer;

public class CommandBuffer {

    public VkCommandBuffer commandBuffer;
    public ArrayList<Runnable> onCompletion = new ArrayList<>();

    public CommandBuffer(VkCommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    public VkCommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public void add(Runnable runnable) {
        this.onCompletion.add(runnable);
    }

    public void reset() {
        this.onCompletion.forEach(Runnable::run);
        this.onCompletion.clear();
        VkInterface.check(vkResetCommandBuffer(commandBuffer, 0),
                "Failed to reset command buffer");
    }

    public void cleanup() {
        this.onCompletion.forEach(Runnable::run);
    }
}
