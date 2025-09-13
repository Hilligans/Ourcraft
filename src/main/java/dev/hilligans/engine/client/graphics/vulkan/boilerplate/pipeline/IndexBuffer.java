package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class IndexBuffer {


    public LogicalDevice device;
    public VulkanBuffer buffer;
    public IntBuffer indices;


    public IndexBuffer(LogicalDevice device, IntBuffer data, CommandBuffer commandBuffer) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            int bufferSize = data.capacity() * 4;
            VulkanBuffer stagingBuffer = device.allocateBuffer(bufferSize,  VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);

            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, stagingBuffer.memory, 0, bufferSize, 0, pos);
            this.indices = MemoryUtil.memIntBuffer(pos.get(0), bufferSize).put(data);
            vkUnmapMemory(device.device, stagingBuffer.memory);

            buffer = device.allocateBuffer(bufferSize, VK_BUFFER_USAGE_TRANSFER_DST_BIT | VK_BUFFER_USAGE_INDEX_BUFFER_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);

            stagingBuffer.copyTo(commandBuffer.getCommandBuffer(), buffer);
            commandBuffer.add(stagingBuffer::free);
        }
    }

    public void cleanup() {
        buffer.free();
    }

}
