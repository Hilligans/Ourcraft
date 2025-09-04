package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.api;

import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;

import static org.lwjgl.vulkan.VK10.vkBindBufferMemory;

public record VulkanMemoryAllocation(long memory, long size, long offset, long pointer) {

    public void bindToBuffer(VulkanBuffer buffer) {
        buffer.memory = memory;
        vkBindBufferMemory(buffer.device.device, buffer.buffer, memory, offset);
    }
}
