package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate;

import dev.hilligans.ourcraft.util.argument.ArgumentContainer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.vulkan.EXTDebugUtils.*;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanProperties {

    public String[] validationLayers;
    public int validationLayerDebug = 0;
    public boolean enableLayer = false;
    public boolean initializeWindow = true;
    public int colorFormat = VK_FORMAT_B8G8R8_SRGB;
    public int colorSpace = VK_COLOR_SPACE_SRGB_NONLINEAR_KHR;
    public int presentMode = VK_PRESENT_MODE_IMMEDIATE_KHR;
    public boolean attemptMemoryRelocations;

    public ArgumentContainer argumentContainer;

    public VulkanProperties() {
        this(new ArgumentContainer());
    }

    public VulkanProperties(ArgumentContainer argumentContainer) {
        this.argumentContainer = argumentContainer;
        attemptMemoryRelocations = argumentContainer.getBoolean("attemptMemoryRelocations", true);
    }

    public VulkanProperties addValidationLayers(String... layers) {
        validationLayers = layers;
        return this;
    }

    public VulkanProperties enableValidationLayers() {
        enableLayer = true;
        return this;
    }

    public VulkanProperties debugValidation() {
        validationLayerDebug |= VK_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT;
        return this;
    }

    public VulkanProperties verboseValidation() {
        validationLayerDebug |= VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT;
        return this;
    }

    public VulkanProperties warningValidation() {
        validationLayerDebug |= VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT;
        return this;
    }

    public VulkanProperties errorValidation() {
        validationLayerDebug |= VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT;
        return this;
    }

    public VulkanProperties initializeWindow(boolean val) {
        initializeWindow = val;
        return this;
    }

    public PointerBuffer enableLayers(MemoryStack memoryStack) {
        HashMap<String, VkLayerProperties> availableLayers = new HashMap<>();
        VkLayerProperties.Buffer buffer = VkInterface.vkEnumerateInstanceLayerProperties();
        buffer.stream().forEach(t -> availableLayers.put(t.layerNameString(), t));
        ArrayList<String> layersToEnable = new ArrayList<>();
        for (String string : validationLayers) {
            if (availableLayers.containsKey(string)) {
                layersToEnable.add(string);
            } else {
                System.out.println("Validation layer " + string + " was requested but not found.");
            }
        }
        PointerBuffer pointerBuffer = memoryStack.callocPointer(layersToEnable.size());
        for (String string : layersToEnable) {
            pointerBuffer.put(memoryStack.ASCII(string));
        }
        pointerBuffer.rewind();
        return pointerBuffer;
    }


    public VkDebugUtilsMessengerCreateInfoEXT setupLayerDebugger() {
        VkDebugUtilsMessengerCreateInfoEXT vkDebugUtilsMessengerCreateInfoEXT;
        vkDebugUtilsMessengerCreateInfoEXT = VkDebugUtilsMessengerCreateInfoEXT.calloc();
        vkDebugUtilsMessengerCreateInfoEXT.sType(VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT);
        vkDebugUtilsMessengerCreateInfoEXT.messageSeverity(validationLayerDebug);
        vkDebugUtilsMessengerCreateInfoEXT.messageType(VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT | VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT);
        vkDebugUtilsMessengerCreateInfoEXT.pfnUserCallback(new VkDebugUtilsMessengerCallbackEXT() {
            public int invoke(int messageSeverity, int messageTypes, long pCallbackData, long pUserData) {
                VkDebugUtilsMessengerCallbackDataEXT message = VkDebugUtilsMessengerCallbackDataEXT.create(pCallbackData);
                System.err.println(message.pMessageString());
                return 0;
            }
        });
        return vkDebugUtilsMessengerCreateInfoEXT;
    }




}
