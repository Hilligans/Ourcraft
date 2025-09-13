package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

import static org.lwjgl.vulkan.KHRSurface.VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public class SwapChain {

    public VkSurfaceFormatKHR surfaceFormat;
    public VulkanWindow vulkanWindow;
    public VkSwapchainCreateInfoKHR swapchainCreateInfo;
    public int presentMode;
    public int imageCount;
    public long swapChain;
    public LongBuffer images;
    public int size;

    public ArrayList<Long> imagesVals = new ArrayList<>();

    public SwapChain(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            this.surfaceFormat = vulkanWindow.device.physicalDevice.chooseFormat(vulkanWindow.device.vulkanInstance.vulkanProperties.colorFormat, vulkanWindow.device.vulkanInstance.vulkanProperties.colorSpace);
            this.presentMode = vulkanWindow.device.physicalDevice.chooseSwapPresentMode(vulkanWindow.device.vulkanInstance.vulkanProperties.presentMode);
            this.imageCount = vulkanWindow.device.physicalDevice.surfaceCapabilities.minImageCount() + 1;
            if (vulkanWindow.device.physicalDevice.surfaceCapabilities.maxImageCount() != 0 && vulkanWindow.device.physicalDevice.surfaceCapabilities.maxImageCount() < imageCount) {
                this.imageCount = vulkanWindow.device.physicalDevice.surfaceCapabilities.maxImageCount();
            }
            swapchainCreateInfo = VkSwapchainCreateInfoKHR.calloc();
            swapchainCreateInfo.sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR);
            swapchainCreateInfo.surface(vulkanWindow.surface);
            swapchainCreateInfo.minImageCount(imageCount);
            swapchainCreateInfo.imageFormat(surfaceFormat.format());
            swapchainCreateInfo.imageColorSpace(surfaceFormat.colorSpace());
            swapchainCreateInfo.imageExtent(vulkanWindow.extent2D);
            swapchainCreateInfo.imageArrayLayers(1);
            swapchainCreateInfo.imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT);
            swapchainCreateInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE);//add support for concurrent if needed
            swapchainCreateInfo.preTransform(vulkanWindow.device.physicalDevice.surfaceCapabilities.currentTransform());
            swapchainCreateInfo.compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);
            swapchainCreateInfo.presentMode(presentMode);
            swapchainCreateInfo.clipped(true);
            swapchainCreateInfo.oldSwapchain(VK_NULL_HANDLE);


            LongBuffer pointer = memoryStack.callocLong(1);
            if (vkCreateSwapchainKHR(vulkanWindow.device.device, swapchainCreateInfo, null, pointer) != VK_SUCCESS) {
                vulkanWindow.device.vulkanInstance.exit("Failed to create swap chain");
            }
            swapChain = pointer.get(0);

            IntBuffer size = memoryStack.callocInt(1);
            vkGetSwapchainImagesKHR(vulkanWindow.device.device, swapChain, size, null);
            images = memoryStack.callocLong(size.get(0));
            this.size = size.get(0);
            vkGetSwapchainImagesKHR(vulkanWindow.device.device, swapChain, size, images);

            for (int x = 0; x < this.size; x++) {
                imagesVals.add(images.get(x));
            }
        }

    }

    public void cleanup() {
        swapchainCreateInfo.free();
        vkDestroySwapchainKHR(vulkanWindow.device.device, swapChain,null);
    }

}
