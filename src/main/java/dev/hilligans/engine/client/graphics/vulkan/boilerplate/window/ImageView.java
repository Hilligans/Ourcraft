package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkComponentMapping;
import org.lwjgl.vulkan.VkImageSubresourceRange;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.vulkan.KHRSwapchain.vkGetSwapchainImagesKHR;
import static org.lwjgl.vulkan.VK10.*;

public class ImageView {

    public VulkanWindow vulkanWindow;
    public int viewCount;
    public ArrayList<Long> imageViews = new ArrayList<>();

    public ImageView(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer intBuffer = memoryStack.callocInt(1);
            vkGetSwapchainImagesKHR(vulkanWindow.device.device, vulkanWindow.swapChain.swapChain,intBuffer,null);
            LongBuffer longBuffer = memoryStack.callocLong(intBuffer.get(0));
            vkGetSwapchainImagesKHR(vulkanWindow.device.device, vulkanWindow.swapChain.swapChain,intBuffer,longBuffer);
            viewCount = intBuffer.get(0);

            for(int x = 0; x < viewCount; x++) {
                VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.calloc(memoryStack);
                createInfo.sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO);
                createInfo.image(longBuffer.get(x));
                createInfo.viewType(VK_IMAGE_VIEW_TYPE_2D);
                createInfo.format(vulkanWindow.swapChain.surfaceFormat.format());
                VkComponentMapping componentMapping = VkComponentMapping.calloc(memoryStack);
                componentMapping.r(VK_COMPONENT_SWIZZLE_IDENTITY);
                componentMapping.g(VK_COMPONENT_SWIZZLE_IDENTITY);
                componentMapping.b(VK_COMPONENT_SWIZZLE_IDENTITY);
                componentMapping.a(VK_COMPONENT_SWIZZLE_IDENTITY);
                createInfo.components(componentMapping);
                VkImageSubresourceRange subresourceRange = VkImageSubresourceRange.calloc(memoryStack);
                subresourceRange.aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
                subresourceRange.baseMipLevel(0);
                subresourceRange.levelCount(1);
                subresourceRange.baseArrayLayer(0);
                subresourceRange.layerCount(1);
                createInfo.subresourceRange(subresourceRange);
                LongBuffer temp = memoryStack.callocLong(1);
                if (vkCreateImageView(vulkanWindow.device.device, createInfo, null, temp) != VK_SUCCESS) {
                    vulkanWindow.device.vulkanInstance.exit("failed to create image views");
                }
                imageViews.add(x, temp.get(0));
            }
        }
    }

    public void cleanup() {
        for(int x = 0; x < viewCount; x++) {
            vkDestroyImageView(vulkanWindow.device.device,imageViews.get(x),null);
        }
    }

}
