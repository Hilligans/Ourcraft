package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import org.lwjgl.vulkan.VkCommandBuffer;

import java.util.ArrayList;

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
}
