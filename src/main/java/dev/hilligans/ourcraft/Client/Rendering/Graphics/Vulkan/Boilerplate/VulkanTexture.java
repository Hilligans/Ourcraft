package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkImageCreateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanTexture {

    public LogicalDevice device;
    public ByteBuffer texture;

    public long image;

    public VulkanTexture(LogicalDevice device, Image img) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VulkanBuffer buffer = new VulkanBuffer(device, img.getSize(), VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);
            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, buffer.memory, 0, img.getSize(), 0, pos);
            this.texture = MemoryUtil.memByteBuffer(pos.get(0), img.getSize()).put(img.buffer);
            vkUnmapMemory(device.device, buffer.memory);
            VkImageCreateInfo createInfo = VkImageCreateInfo.calloc(memoryStack);
            createInfo.sType(VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO);
            createInfo.imageType(VK_IMAGE_TYPE_2D);
            createInfo.extent().width(img.width);
            createInfo.extent().height(img.height);
            createInfo.extent().depth(1);
            createInfo.mipLevels(1);
            createInfo.arrayLayers(1);

            createInfo.format(VK_FORMAT_R8G8B8A8_SRGB);
            createInfo.tiling(VK_IMAGE_TILING_OPTIMAL);
            createInfo.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED);
            createInfo.usage(VK_IMAGE_USAGE_TRANSFER_DST_BIT | VK_IMAGE_USAGE_SAMPLED_BIT);
            createInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);
            createInfo.samples(VK_SAMPLE_COUNT_1_BIT);

            LongBuffer loc = memoryStack.callocLong(1);

            if(vkCreateImage(device.device, createInfo, null, loc) != VK_SUCCESS) {
                throw new RuntimeException("Failed to create image");
            }
            this.image = loc.get(0);

            VkMemoryRequirements requirements = VkMemoryRequirements.calloc(memoryStack);
            vkGetImageMemoryRequirements(device.device, image, requirements);

        }
    }

    public VulkanTexture(ByteBuffer byteBuffer, int width, int height, int format) {

    }
}
