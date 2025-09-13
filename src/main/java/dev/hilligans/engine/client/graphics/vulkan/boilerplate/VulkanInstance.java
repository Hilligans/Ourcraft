package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.client.graphics.vulkan.VulkanEngine;
import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import dev.hilligans.engine.util.argument.ArgumentContainer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDevice;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanInstance {

    public PhysicalDevice physicalDevice;
    public PhysicalDeviceManager devices;
    public LogicalDevice logicalDevice;
    public VkInstance vkInstance;
    public VkApplicationInfo applicationInfo;
    public VkInstanceCreateInfo createInfo;
    public VulkanWindow vulkanWindow;
    public VulkanProperties vulkanProperties;

    public PointerBuffer extensions = memCallocPointer(64);
    public PointerBuffer instance = memAllocPointer(1);

    public VulkanEngine engine;

    public boolean attemptMemoryRelocations;

    public VulkanInstance(VulkanProperties vulkanProperties) {
        this.vulkanProperties = vulkanProperties;
        this.attemptMemoryRelocations = vulkanProperties.attemptMemoryRelocations;
    }

    public void setUp() {
        createInstance();
        devices = new PhysicalDeviceManager(this);
        physicalDevice = devices.selectPhysicalDevice();
        logicalDevice = physicalDevice.logicalDevice;
        vulkanWindow = new VulkanWindow(physicalDevice.vulkanInstance,500,500, engine).addDevice(logicalDevice);
        physicalDevice.buildForSurface(vulkanWindow.surface);
        vulkanWindow.selectFamily();
        vulkanWindow.addData();

    }

    public void setGraphicsEngine(VulkanEngine engine) {
        this.engine = engine;
    }

    public void createInstance() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            if(vulkanProperties.initializeWindow) {
                glfwInit();
                PointerBuffer glfwExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();
                if (glfwExtensions == null) {
                    exit("Unable to initialize vulkan");
                    return;
                }
                for (int x = 0; x < glfwExtensions.capacity(); x++) {
                    extensions.put(glfwExtensions.get(x));
                }
            }
            PointerBuffer validationLayers = null;
            long pAddress = NULL;
            if (vulkanProperties.enableLayer) {
                validationLayers = vulkanProperties.enableLayers(memoryStack);
                pAddress = vulkanProperties.setupLayerDebugger().address();
                extensions.put(memoryStack.UTF8(VK_EXT_DEBUG_UTILS_EXTENSION_NAME));
            }

            applicationInfo = VkApplicationInfo.calloc();
            applicationInfo.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
            applicationInfo.pNext(NULL);
            applicationInfo.pApplicationName(memoryStack.UTF8("test tri"));
            applicationInfo.applicationVersion(VK_MAKE_VERSION(1, 0, 0));
            applicationInfo.pEngineName(memoryStack.UTF8("No Engine"));
            applicationInfo.engineVersion(VK_MAKE_VERSION(1, 0, 0));
            applicationInfo.apiVersion(VK_API_VERSION_1_0);

            extensions.flip();

            createInfo = VkInstanceCreateInfo.calloc();
            createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
            createInfo.pApplicationInfo(applicationInfo);
            createInfo.ppEnabledExtensionNames(extensions);
            createInfo.pNext(pAddress);
            createInfo.ppEnabledLayerNames(validationLayers);

            if (vkCreateInstance(createInfo, null, instance) != VK_SUCCESS) {
                exit("Failed to create vulkan instance");
            }

            vkInstance = new VkInstance(instance.get(0), createInfo);
        }
    }

    public void run() {
        //vulkanWindow.startDrawing();
        //cleanup();
    }

    public static final int MAX_FRAMES_IN_FLIGHT = 2;

    public void selectPhysicalDevice() {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer deviceCount = memoryStack.mallocInt(1);
            vkEnumeratePhysicalDevices(vkInstance, deviceCount, null);
            PointerBuffer buffer = memoryStack.mallocPointer(deviceCount.get(0));
            vkEnumeratePhysicalDevices(vkInstance, deviceCount, buffer);
            int size = deviceCount.get(0);
            ArrayList<PhysicalDevice> devices = new ArrayList<>(size);
            for (int x = 0; x < size; x++) {
                devices.add(new PhysicalDevice(new VkPhysicalDevice(buffer.get(x), vkInstance), this));
            }

            physicalDevice = devices.get(0);

            //TODO fix
            physicalDevice.supportsSwapChain();
        }
    }

    public PhysicalDevice getDefaultDevice() {
        return devices.getDefaultDevice();
    }



    public void cleanup() {
        logicalDevice.destroy();
        physicalDevice.cleanup();
        vkDestroyInstance(vkInstance,null);
        if(vulkanProperties.initializeWindow) {
            glfwTerminate();
        }
    }

    public ArgumentContainer getArgumentContainer() {
        return vulkanProperties.argumentContainer;
    }

    public void exit(String reason) {
        throw new VulkanEngineException(reason);
        //System.exit(0);
    }

    public static String getStringErrorCode(int code) {
        return switch (code) {
            case 0 -> "VK_SUCCESS";
            case 1 -> "VK_NOT_READY";
            case 2 -> "VK_TIMEOUT";
            case 3 -> "VK_EVENT_SET";
            case 4 -> "VK_EVENT_RESET";
            case 5 -> "VK_INCOMPLETE";
            case -1 -> "VK_ERROR_OUT_OF_HOST_MEMORY";
            case -2 -> "VK_ERROR_OUT_OF_DEVICE_MEMORY";
            case -3 -> "VK_ERROR_INITIALIZATION_FAILED";
            case -4 -> "VK_ERROR_DEVICE_LOST";
            case -5 -> "VK_ERROR_MEMORY_MAP_FAILED";
            case -6 -> "VK_ERROR_LAYER_NOT_PRESENT";
            case -7 -> "VK_ERROR_EXTENSION_NOT_PRESENT";
            case -8 -> "VK_ERROR_FEATURE_NOT_PRESENT";
            case -9 -> "VK_ERROR_INCOMPATIBLE_DRIVER";
            case -10 -> "VK_ERROR_TOO_MANY_OBJECTS";
            case -11 -> "VK_ERROR_FORMAT_NOT_SUPPORTED";
            case -12 -> "VK_ERROR_FRAGMENTED_POOL";
            default -> "VK_ERROR_UNKNOWN";
        };
    }
}
