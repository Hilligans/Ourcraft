package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class IndexBuffer {


    public LogicalDevice device;
    public long buffer;
    public long memory;
    public IntBuffer indices;

    public IndexBuffer(LogicalDevice device) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferCreateInfo createInfo = VkBufferCreateInfo.callocStack(memoryStack);
            createInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
            createInfo.size(6 * 3 * 4L);
            createInfo.usage(VK_BUFFER_USAGE_VERTEX_BUFFER_BIT);
            createInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);
            LongBuffer pos = memoryStack.mallocLong(1);
            if(vkCreateBuffer(device.device,createInfo,null,pos) != VK_SUCCESS) {
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
            vkBindBufferMemory(device.device,buffer,memory,0);
        }
    }

    public IndexBuffer putData(int[] indices) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, memory, 0, indices.length, 0, pos);
            this.indices = MemoryUtil.memIntBuffer(pos.get(0),indices.length).put(indices);
            vkUnmapMemory(device.device,memory);
        }
        return this;
    }

    public IndexBuffer putData(IntBuffer indices) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, memory, 0, indices.remaining(), 0, pos);
            this.indices = MemoryUtil.memIntBuffer(pos.get(0),indices.remaining()).put(indices);
            vkUnmapMemory(device.device,memory);
        }
        return this;
    }

    public int findMemoryTypes(int filter, int properties) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPhysicalDeviceMemoryProperties memProperties = VkPhysicalDeviceMemoryProperties.calloc(memoryStack);
            vkGetPhysicalDeviceMemoryProperties(device.physicalDevice.physicalDevice, memProperties);
            //  System.out.println("count:" + memProperties.memoryTypeCount());
            //vkGetDeviceMemoryCommitment(device.physicalDevice.physicalDevice,);

            for (int i = 0; i < memProperties.memoryTypeCount(); i++) {
                long[] longs = new long[10];
                //vkGetDeviceMemoryCommitment(device.device,i,longs);
                // System.out.println(i);
                if ((filter & (1 << i)) == 1 && (memProperties.memoryTypes(i).propertyFlags() & properties) == properties) {
                    return i;
                }
            }
            if (1 == 1) {
                return 1;
            }
            device.vulkanInstance.exit("failed to find memory");
        }
        return -1;
    }

    public void update(float[] vertices) {

    }

    public void updateAsync(float[] vertices) {

    }

    public void cleanup() {
        vkDestroyBuffer(device.device,buffer,null);
        vkFreeMemory(device.device, memory, null);
    }

}
