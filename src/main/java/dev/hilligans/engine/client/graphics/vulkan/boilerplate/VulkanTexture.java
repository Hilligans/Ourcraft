package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import dev.hilligans.engine.client.graphics.resource.Image;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanTexture {

    public LogicalDevice device;
    public ByteBuffer texture;

    public long image;
    public long imageMemory;
    public long imageView;
    public long sampler;

    public VulkanTexture(LogicalDevice device, Image img) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VulkanBuffer buffer = new VulkanBuffer(device, img.getSize(), VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT, false);
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

            device.executeMethod(() -> vkCreateImage(device.device, createInfo, null, loc), "Failed to create image, %s");
            this.image = loc.get(0);

            VkMemoryRequirements requirements = VkMemoryRequirements.calloc(memoryStack);
            vkGetImageMemoryRequirements(device.device, image, requirements);
        }
    }

    public VulkanTexture(CommandBuffer commandBuffer, LogicalDevice device, ByteBuffer byteBuffer, int width, int height, int format) {
        this.device = device;
        int size = width * height * format;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VulkanBuffer stagingBuffer = new VulkanBuffer(device, size, VK_BUFFER_USAGE_TRANSFER_SRC_BIT, VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT, false);
            PointerBuffer pos = memoryStack.mallocPointer(1);
            vkMapMemory(device.device, stagingBuffer.memory, 0, size, 0, pos);
            this.texture = MemoryUtil.memByteBuffer(pos.get(0), size).put(byteBuffer);
            vkUnmapMemory(device.device, stagingBuffer.memory);
            VkImageCreateInfo createInfo = VkImageCreateInfo.calloc(memoryStack);
            createInfo.sType(VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO);
            createInfo.imageType(VK_IMAGE_TYPE_2D);
            createInfo.extent().width(width);
            createInfo.extent().height(height);
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

            device.executeMethod(() -> vkCreateImage(device.device, createInfo, null, loc), "Failed to create image, %s");
            this.image = loc.get(0);

            VkMemoryRequirements requirements = VkMemoryRequirements.calloc(memoryStack);
            vkGetImageMemoryRequirements(device.device, image, requirements);

            VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.calloc(memoryStack);
            allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
            allocInfo.allocationSize(requirements.size());
            allocInfo.memoryTypeIndex(device.findMemoryType(requirements.memoryTypeBits(), VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT));

            LongBuffer mem = memoryStack.mallocLong(1);

            device.executeMethod(() -> vkAllocateMemory(device.device, allocInfo, null, mem), "Failed to allocate memory for image, %s");

            this.imageMemory = mem.get(0);

            vkBindImageMemory(device.device, this.image, this.imageMemory, 0);

            transitionImageLayout(this.image, VK_FORMAT_R8G8B8A8_SRGB, VK_IMAGE_LAYOUT_UNDEFINED, VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL, commandBuffer.getCommandBuffer());
            copyBufferToImage(stagingBuffer, this.image, width, height, commandBuffer.getCommandBuffer());
            transitionImageLayout(this.image, VK_FORMAT_R8G8B8A8_SRGB, VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL, VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL, commandBuffer.getCommandBuffer());
            this.imageView = createImageView(VK_FORMAT_R8G8B8A8_SRGB);
            this.sampler = createSampler();
            commandBuffer.add(stagingBuffer::free);
        }
    }

    void transitionImageLayout(long image, long format, int oldLayout, int newLayout, VkCommandBuffer commandBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkImageMemoryBarrier barrier = VkImageMemoryBarrier.calloc(memoryStack);
            barrier.sType(VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER);
            barrier.oldLayout(oldLayout);
            barrier.newLayout(newLayout);
            barrier.srcQueueFamilyIndex(VK_QUEUE_FAMILY_IGNORED);
            barrier.dstQueueFamilyIndex(VK_QUEUE_FAMILY_IGNORED);
            barrier.image(image);
            barrier.subresourceRange().aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
            barrier.subresourceRange().baseMipLevel(0);
            barrier.subresourceRange().levelCount(1);
            barrier.subresourceRange().baseArrayLayer(0);
            barrier.subresourceRange().layerCount(1);

            int sourceStage;
            int destinationStage;

            if (oldLayout == VK_IMAGE_LAYOUT_UNDEFINED && newLayout == VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL) {
                barrier.srcAccessMask(0);
                barrier.dstAccessMask(VK_ACCESS_TRANSFER_WRITE_BIT);

                sourceStage = VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT;
                destinationStage = VK_PIPELINE_STAGE_TRANSFER_BIT;
            } else if (oldLayout == VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL && newLayout == VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL) {
                barrier.srcAccessMask(VK_ACCESS_TRANSFER_WRITE_BIT);
                barrier.dstAccessMask(VK_ACCESS_SHADER_READ_BIT);

                sourceStage = VK_PIPELINE_STAGE_TRANSFER_BIT;
                destinationStage = VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT;
            } else {
                throw new VulkanEngineException("unsupported layout transition!");
            }

            VkImageMemoryBarrier.Buffer buf = VkImageMemoryBarrier.calloc(1, memoryStack);
            buf.put(0, barrier);
            vkCmdPipelineBarrier(commandBuffer, sourceStage, destinationStage, 0, null, null, buf);
        }
    }

    void copyBufferToImage(VulkanBuffer buffer, long image, int width, int height, VkCommandBuffer commandBuffer) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkBufferImageCopy region = VkBufferImageCopy.calloc(memoryStack);
            region.bufferOffset(0);
            region.bufferRowLength(0);
            region.bufferImageHeight(0);
            region.imageSubresource().aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
            region.imageSubresource().mipLevel(0);
            region.imageSubresource().baseArrayLayer(0);
            region.imageSubresource().layerCount(1);
            region.imageOffset(VkOffset3D.malloc(memoryStack).set(0, 0, 0));
            region.imageExtent(VkExtent3D.malloc(memoryStack).set(width, height, 1));

            VkBufferImageCopy.Buffer buf = VkBufferImageCopy.calloc(1, memoryStack);
            buf.put(0, region);

            vkCmdCopyBufferToImage(commandBuffer, buffer.buffer, image, VK_IMAGE_LAYOUT_TRANSFER_DST_OPTIMAL, buf);
        }
    }

    public long createImageView(int format) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkImageViewCreateInfo viewInfo = VkImageViewCreateInfo.calloc(memoryStack);
            viewInfo.sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO);
            viewInfo.image(image);
            viewInfo.viewType(VK_IMAGE_VIEW_TYPE_2D);
            viewInfo.format(format);
            viewInfo.subresourceRange().aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
            viewInfo.subresourceRange().baseMipLevel(0);
            viewInfo.subresourceRange().levelCount(1);
            viewInfo.subresourceRange().baseArrayLayer(0);
            viewInfo.subresourceRange().layerCount(1);

            LongBuffer buffer = memoryStack.callocLong(1);

            device.executeMethod(() -> vkCreateImageView(device.device, viewInfo, null, buffer), "Failed to create texture image view, %s");
            return buffer.get(0);
        }
    }

    public long createSampler() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkSamplerCreateInfo samplerInfo = VkSamplerCreateInfo.calloc(memoryStack);
            samplerInfo.sType(VK_STRUCTURE_TYPE_SAMPLER_CREATE_INFO);
            samplerInfo.magFilter(VK_FILTER_NEAREST);
            samplerInfo.minFilter(VK_FILTER_NEAREST);
            samplerInfo.addressModeU(VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER);
            samplerInfo.addressModeV(VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER);
            samplerInfo.addressModeW(VK_SAMPLER_ADDRESS_MODE_CLAMP_TO_BORDER);
            samplerInfo.anisotropyEnable(true);
            samplerInfo.maxAnisotropy(device.physicalDevice.properties.limits().maxSamplerAnisotropy());
            samplerInfo.borderColor(VK_BORDER_COLOR_INT_OPAQUE_WHITE);
            samplerInfo.unnormalizedCoordinates(false);
            samplerInfo.compareEnable(false);
            samplerInfo.compareOp(VK_COMPARE_OP_ALWAYS);

            samplerInfo.mipmapMode(VK_SAMPLER_MIPMAP_MODE_LINEAR);
            samplerInfo.mipLodBias(0.0f);
            samplerInfo.minLod(0.0f);
            samplerInfo.maxLod(0.0f);

            LongBuffer buffer = memoryStack.callocLong(1);

            device.executeMethod(() -> vkCreateSampler(device.device, samplerInfo, null, buffer), "Failed to create texture sampler, %s");
            return buffer.get(0);
        }
    }

    public void cleanup() {
        vkDestroyImageView(device.device, imageView, null);
        vkDestroySampler(device.device, sampler, null);
        vkDestroyImage(device.device, image, null);
        vkFreeMemory(device.device, imageMemory, null);
    }
}
