package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;

import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.*;

public class PhysicalDevice {

    public VkPhysicalDevice physicalDevice;
    public VulkanInstance vulkanInstance;
    public VkPhysicalDeviceProperties properties = VkPhysicalDeviceProperties.calloc();
    public VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc();
    public VkPhysicalDeviceLimits deviceLimits;
    public VkSurfaceCapabilitiesKHR surfaceCapabilities;
    public IntBuffer presentModes;
    public VkSurfaceFormatKHR.Buffer surfaceFormats;
    public LogicalDevice logicalDevice;
    public String deviceName;

    public PhysicalDevice(VkPhysicalDevice physicalDevice, VulkanInstance vulkanInstance) {
        this.physicalDevice = physicalDevice;
        this.vulkanInstance = vulkanInstance;
        vkGetPhysicalDeviceProperties(physicalDevice, properties);
        this.deviceLimits = properties.limits();
        this.logicalDevice = createDevice();
    }

    public LogicalDevice createDevice() {
        return new LogicalDevice(this);
    }

    public void buildForSurface(long surface) {
        getSurfaceCapabilities(surface);
        logicalDevice.queueFamilyManager.testSurface(surface);
    }

    public boolean supportsSwapChain() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer size = memoryStack.mallocInt(1);
            vkEnumerateDeviceExtensionProperties(physicalDevice, (String) null, size, null);
            VkExtensionProperties.Buffer device_extensions = VkExtensionProperties.mallocStack(size.get(0), memoryStack);
            vkEnumerateDeviceExtensionProperties(physicalDevice, (String) null, size, device_extensions);
            for (int x = 0; x < device_extensions.capacity(); x++) {
                if (device_extensions.get(x).extensionNameString().equals(VK_KHR_SWAPCHAIN_EXTENSION_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDiscreteGPU() {
        return properties.deviceType() == VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU;
    }

    public boolean isIntegratedGPU() {
        return properties.deviceType() == VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU;
    }

    public boolean isVirtualGPU() {
        return properties.deviceType() == VK_PHYSICAL_DEVICE_TYPE_VIRTUAL_GPU;
    }

    private void getSurfaceCapabilities(long surface) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            surfaceCapabilities = VkSurfaceCapabilitiesKHR.calloc();
            vkGetPhysicalDeviceSurfaceCapabilitiesKHR(physicalDevice, surface, surfaceCapabilities);

            IntBuffer size = memoryStack.mallocInt(1);
            vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface, size, null);
            surfaceFormats = VkSurfaceFormatKHR.calloc(size.get(0));
            vkGetPhysicalDeviceSurfaceFormatsKHR(physicalDevice, surface, size, surfaceFormats);

            vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface, size, null);
            presentModes = memoryStack.callocInt(size.get(0));
            vkGetPhysicalDeviceSurfacePresentModesKHR(physicalDevice, surface, size, presentModes);
        }
    }

    public VkSurfaceFormatKHR chooseFormat(int idealFormat, int idealColorSpace) {
        for(VkSurfaceFormatKHR vkSurfaceFormatKHR : surfaceFormats) {
            if(vkSurfaceFormatKHR.format() == idealFormat && vkSurfaceFormatKHR.colorSpace() == idealColorSpace) {
                return vkSurfaceFormatKHR;
            }
        }
        return surfaceFormats.get(0);
    }

    public int chooseSwapPresentMode(int idealMode) {
        for(int x = 0; x < presentModes.capacity(); x++) {
            if(presentModes.get(x) == idealMode) {
                return idealMode;
            }
        }
        return VK_PRESENT_MODE_FIFO_KHR;
    }

    public void cleanup() {
        deviceFeatures.free();
        properties.free();
        surfaceCapabilities.free();
    }

}
