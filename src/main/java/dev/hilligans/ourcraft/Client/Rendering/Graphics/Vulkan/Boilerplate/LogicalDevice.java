package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VertexBufferManager;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngineException;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanWindow;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static dev.hilligans.ourcraft.Client.Rendering.Widgets.FolderWidget.size;
import static org.lwjgl.vulkan.VK10.*;

public class LogicalDevice {

    public PhysicalDevice physicalDevice;
    public VulkanInstance vulkanInstance;
    public VkDevice device;
    public VulkanQueueFamilyManager queueFamilyManager;
    public VkDeviceQueueCreateInfo.Buffer buffer;
    public VertexBufferManager bufferManager = new VertexBufferManager(this);

    public LogicalDevice(PhysicalDevice physicalDevice) {
        this.vulkanInstance = physicalDevice.vulkanInstance;
        this.physicalDevice = physicalDevice;

        queueFamilyManager = new VulkanQueueFamilyManager(this);

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {

            PointerBuffer requiredExtensions = memoryStack.mallocPointer(1);
            requiredExtensions.put(0, memoryStack.ASCII(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME));

            VkDeviceCreateInfo info = VkDeviceCreateInfo.calloc(memoryStack);
            info.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);
            buffer = VkDeviceQueueCreateInfo.calloc(queueFamilyManager.queueFamilies.size());
            int x = 0;
            for (QueueFamily queueFamily : queueFamilyManager.queueFamilies) {
                buffer.get(x).set(queueFamily.createInfo(memoryStack));
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

    public VulkanBuffer allocateBuffer(int size, int usage, int properties) {
        return new VulkanBuffer(this, size, usage, properties);
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
        //info.free();
    }
}
