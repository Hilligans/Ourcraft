package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.CommandBuffer;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VertexBuffer {

    public LogicalDevice device;
    public VulkanBuffer buffer;
    public FloatBuffer vertices;
    public boolean available;

    public VertexBuffer(LogicalDevice device, float[] data, CommandBuffer commandBuffer) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            int bufferSize = data.length * 4;
            VulkanBuffer stagingBuffer = device.allocateBuffer(bufferSize, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);

            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, stagingBuffer.memory, 0, bufferSize, 0, pos);
            this.vertices = MemoryUtil.memFloatBuffer(pos.get(0), bufferSize).put(data);
            vkUnmapMemory(device.device, stagingBuffer.memory);

            buffer = device.allocateBuffer(bufferSize, VK_BUFFER_USAGE_TRANSFER_DST_BIT | VK_BUFFER_USAGE_VERTEX_BUFFER_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);

            stagingBuffer.copyTo(commandBuffer.getCommandBuffer(), buffer);

            commandBuffer.add(() -> available = true);
            commandBuffer.add(stagingBuffer::free);
        }
    }

    public VertexBuffer(LogicalDevice device, FloatBuffer vertices, CommandBuffer commandBuffer) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            int bufferSize = vertices.capacity() * 4;
            VulkanBuffer stagingBuffer = device.allocateBuffer(bufferSize, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);

            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, stagingBuffer.memory, 0, bufferSize, 0, pos);
            this.vertices = MemoryUtil.memFloatBuffer(pos.get(0), bufferSize).put(vertices);
            vkUnmapMemory(device.device, stagingBuffer.memory);

            buffer = device.allocateBuffer(bufferSize, VK_BUFFER_USAGE_TRANSFER_DST_BIT | VK_BUFFER_USAGE_VERTEX_BUFFER_BIT, VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);

            stagingBuffer.copyTo(commandBuffer.getCommandBuffer(), buffer);

            commandBuffer.add(() -> available = true);
            commandBuffer.add(stagingBuffer::free);
        }
    }

    /*
    public VertexBuffer(LogicalDevice device) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCreateInfo createInfo = VkBufferCreateInfo.callocStack(memoryStack);
            createInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
            createInfo.size(6 * 3 * 4L);
            createInfo.usage(VK_BUFFER_USAGE_VERTEX_BUFFER_BIT);
            createInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);
            LongBuffer pos = memoryStack.mallocLong(1);
            if(vkCreateBuffer(device.device, createInfo,null, pos) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to create vertex buffer");
            }
            this.buffer = pos.get(0);
            VkMemoryRequirements memoryRequirements = VkMemoryRequirements.callocStack(memoryStack);
            vkGetBufferMemoryRequirements(device.device,buffer,memoryRequirements);

            VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.callocStack(memoryStack);
            allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
            allocInfo.allocationSize(memoryRequirements.size());
            allocInfo.memoryTypeIndex(findMemoryTypes(memoryRequirements.memoryTypeBits(), VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT));

            if(vkAllocateMemory(device.device,allocInfo,null,pos) != VK_SUCCESS) {
                device.vulkanInstance.exit("failed to allocate memory");
            }
            memory = pos.get(0);
            vkBindBufferMemory(device.device,buffer, memory,0);
        }
    }
     */

    public void cleanup() {
        buffer.free();
    }
}