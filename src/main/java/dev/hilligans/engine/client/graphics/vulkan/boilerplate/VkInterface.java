package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.VulkanBaseGraphicsContext;
import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeType;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.VK_SUCCESS;


public class VkInterface {

    public static long glfwCreateWindowSurface(VkInstance instance, long window, VkAllocationCallbacks allocator) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.callocLong(1);
            GLFWVulkan.glfwCreateWindowSurface(instance,window,allocator,longBuffer);
            return longBuffer.get(0);
        }
    }

    public static boolean vkGetPhysicalDeviceSurfaceSupportKHR(VkPhysicalDevice physicalDevice, @NativeType("uint32_t") int queueFamilyIndex, @NativeType("VkSurfaceKHR") long surface) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer intBuffer = memoryStack.callocInt(1);
            KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice, queueFamilyIndex, surface,intBuffer);
            return intBuffer.get() == 1;
        }
    }

    public static VkLayerProperties.Buffer vkEnumerateInstanceLayerProperties() {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer validationLayerCount = memoryStack.mallocInt(1);
            VK10.vkEnumerateInstanceLayerProperties(validationLayerCount,null);
            VkLayerProperties.Buffer buffer = VkLayerProperties.malloc(validationLayerCount.get(0));
            VK10.vkEnumerateInstanceLayerProperties(validationLayerCount, buffer);
            return buffer;
        }
    }

    public static VkOffset2D createOffset2D(int x, int y) {
        VkOffset2D vkOffset2D = VkOffset2D.malloc();
        vkOffset2D.set(x,y);
        return vkOffset2D;
    }

    public static VkClearColorValue clearValue(float r, float g, float b, float a) {
        VkClearColorValue vkClearValue = VkClearColorValue.malloc();
        vkClearValue.float32(0,r).float32(1,g).float32(2,b).float32(3,a);
        return vkClearValue;
    }

    public static LogicalDevice getDevice(GraphicsContext graphicsContext) {
        if(graphicsContext instanceof VulkanBaseGraphicsContext context) {
            return context.getDevice();
        }
        throw new IllegalStateException("Invalid graphics context for engine");
    }

    public static CommandBuffer getCommandBuffer(GraphicsContext graphicsContext) {
        if(graphicsContext instanceof VulkanBaseGraphicsContext context) {
            return context.getCommandBuffer();
        }
        throw new IllegalStateException("Invalid graphics context for engine");
    }

    public static void check(int value, String reason) {
        if(value != VK_SUCCESS) {
            throw new VulkanEngineException(value, reason);
        }
    }
}
