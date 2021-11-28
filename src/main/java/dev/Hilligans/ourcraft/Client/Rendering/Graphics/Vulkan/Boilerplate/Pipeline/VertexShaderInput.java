package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.vulkan.VkVertexInputAttributeDescription;
import org.lwjgl.vulkan.VkVertexInputBindingDescription;

import static org.lwjgl.vulkan.VK10.*;

public class VertexShaderInput {

    public VulkanWindow vulkanWindow;
    public VkVertexInputBindingDescription bindingDescription;
    public VkVertexInputAttributeDescription.Buffer parts;

    public VertexShaderInput(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;
        bindingDescription = VkVertexInputBindingDescription.callocStack();
        bindingDescription.binding(0);
        bindingDescription.stride(4*6);
        bindingDescription.inputRate(VK_VERTEX_INPUT_RATE_VERTEX);

        parts = VkVertexInputAttributeDescription.callocStack(2);
        parts.put(0,VkVertexInputAttributeDescription.calloc().binding(0).location(0).format(VK_FORMAT_R32G32_SFLOAT).offset(0));
        parts.put(1,VkVertexInputAttributeDescription.calloc().binding(0).location(1).format(VK_FORMAT_R32G32B32_SFLOAT).offset(3*4));

    }





}
