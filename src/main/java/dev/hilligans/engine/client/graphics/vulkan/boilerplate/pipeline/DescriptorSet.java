package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanTexture;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;


public class DescriptorSet {

    public long descriptorSetLayout;
    public LongBuffer descriptorSets;
    public LogicalDevice device;


    public DescriptorSet(LogicalDevice device) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkDescriptorSetLayoutBinding samplerLayoutBinding = VkDescriptorSetLayoutBinding.calloc(memoryStack);
            samplerLayoutBinding.binding(0);
            samplerLayoutBinding.descriptorCount(1);
            samplerLayoutBinding.descriptorType(VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER);
            samplerLayoutBinding.pImmutableSamplers(null);
            samplerLayoutBinding.stageFlags(VK_SHADER_STAGE_FRAGMENT_BIT);

            VkDescriptorSetLayoutBinding.Buffer buffer = VkDescriptorSetLayoutBinding.malloc(1, memoryStack);
            buffer.put(0, samplerLayoutBinding);

            VkDescriptorSetLayoutCreateInfo layoutInfo = VkDescriptorSetLayoutCreateInfo.calloc(memoryStack);
            layoutInfo.sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO);
            layoutInfo.pBindings(buffer);

            LongBuffer buf = memoryStack.callocLong(1);

            device.executeMethod(() -> vkCreateDescriptorSetLayout(device.device, layoutInfo, null, buf), "Failed to create descriptor set layout, %s");

            this.descriptorSetLayout = buf.get(0);
        }
    }

    public void createDescriptorSet(DescriptorPool descriptorPool, VulkanTexture texture) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkDescriptorSetAllocateInfo allocInfo = VkDescriptorSetAllocateInfo.calloc(memoryStack);
            allocInfo.sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO);
            allocInfo.descriptorPool(descriptorPool.descriptorPool);
            LongBuffer buf = memoryStack.mallocLong(descriptorPool.count);
            for(int x = 0; x < descriptorPool.count; x++) {
                buf.put(x, descriptorSetLayout);
            }
            allocInfo.pSetLayouts(buf);

            descriptorSets = MemoryUtil.memAllocLong(descriptorPool.count);

            device.executeMethod(() -> vkAllocateDescriptorSets(device.device, allocInfo, descriptorSets), "Failed to allocate descriptor sets, %s");

            VkWriteDescriptorSet.Buffer buffer = VkWriteDescriptorSet.malloc(descriptorPool.count, memoryStack);
            for (int x = 0; x < descriptorPool.count; x++) {
                VkDescriptorImageInfo imageInfo = VkDescriptorImageInfo.calloc(memoryStack);
                imageInfo.imageLayout(VK_IMAGE_LAYOUT_SHADER_READ_ONLY_OPTIMAL);
                imageInfo.imageView(texture.imageView);
                imageInfo.sampler(texture.sampler);
                VkWriteDescriptorSet descriptorWrite = VkWriteDescriptorSet.calloc(memoryStack);
                descriptorWrite.sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET);
                descriptorWrite.dstSet(descriptorSets.get(x));
                descriptorWrite.dstBinding(0);
                descriptorWrite.dstArrayElement(0);
                descriptorWrite.descriptorType(VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER);
                descriptorWrite.descriptorCount(1);
                descriptorWrite.pImageInfo(VkDescriptorImageInfo.malloc(1, memoryStack).put(imageInfo));

                buffer.put(x, descriptorWrite);
            }
            vkUpdateDescriptorSets(device.device, buffer, null);
        }
    }

    public void cleanup() {
        vkDestroyDescriptorSetLayout(device.device, descriptorSetLayout, null);
    }
}
