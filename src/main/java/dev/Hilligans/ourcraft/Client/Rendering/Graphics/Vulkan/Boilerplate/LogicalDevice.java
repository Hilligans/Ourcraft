package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VertexBufferManager;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;

import static org.lwjgl.vulkan.VK10.*;

public class LogicalDevice {

    public PhysicalDevice physicalDevice;
    public VulkanInstance vulkanInstance;
    public VkDeviceCreateInfo info;
    public VkDevice device;
    public QueueGroup queueGroup = new QueueGroup();
    public VkDeviceQueueCreateInfo.Buffer buffer;
    public VulkanWindow defaultVulkanWindow;
    public VertexBufferManager bufferManager = new VertexBufferManager(this);

    public boolean acquiredDefaultWindow = false;

    public LogicalDevice(PhysicalDevice physicalDevice) {
        this.vulkanInstance = physicalDevice.vulkanInstance;
        this.physicalDevice = physicalDevice;
        this.defaultVulkanWindow = new VulkanWindow(physicalDevice.vulkanInstance,500,500, vulkanInstance.engine).addDevice(this);
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {

            PointerBuffer requiredExtensions = memoryStack.mallocPointer(1);
            requiredExtensions.put(0, memoryStack.ASCII(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME));

            this.info = VkDeviceCreateInfo.calloc();
            this.info.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);
            buffer = VkDeviceQueueCreateInfo.calloc(physicalDevice.queueFamilies.size());
            int x = 0;
            for (QueueFamily queueFamily : physicalDevice.queueFamilies) {
                buffer.get(x).set(queueFamily.createInfo(memoryStack));
                queueGroup.addQueue(queueFamily);
                x++;
            }
            info.pQueueCreateInfos(buffer);
            info.pEnabledFeatures(physicalDevice.deviceFeatures);
            info.ppEnabledExtensionNames(requiredExtensions);

            PointerBuffer pp = memoryStack.callocPointer(1);

            vkCreateDevice(physicalDevice.physicalDevice, info, null, pp);
            device = new VkDevice(pp.get(0), physicalDevice.physicalDevice, info);
        }
    }

    public VulkanWindow getDefaultWindow() {
        return defaultVulkanWindow;
    }

    public VulkanWindow createNewWindow() {
        return new VulkanWindow(physicalDevice.vulkanInstance,500,500, null).addDevice(this).addData();
    }

    public VulkanWindow getWindow() {
        return createNewWindow();
    }

    public void destroy() {
        bufferManager.cleanup();
        vkDestroyDevice(device,null);
        buffer.free();
        info.free();
    }

}
