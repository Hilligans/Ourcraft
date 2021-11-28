package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.VertexShaderInput;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;
import org.lwjgl.vulkan.VkVertexInputBindingDescription;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class Shader {

    public VulkanWindow vulkanWindow;
    public long shader;
    public VkPipelineShaderStageCreateInfo shaderCreateInfo;
    public VkPipelineVertexInputStateCreateInfo stateCreateInfo;

    public Shader(VulkanWindow vulkanWindow, byte[] shader, int bit) {
        this.vulkanWindow = vulkanWindow;
        VkShaderModuleCreateInfo createInfo = VkShaderModuleCreateInfo.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            createInfo.pCode(memoryStack.bytes(shader));
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            vkCreateShaderModule(vulkanWindow.device.device, createInfo, null, longBuffer);
            this.shader = longBuffer.get(0);

            shaderCreateInfo = VkPipelineShaderStageCreateInfo.calloc();
            shaderCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);
            shaderCreateInfo.stage(bit);
            shaderCreateInfo.module(this.shader);
            shaderCreateInfo.pName(memoryStack.UTF8("main"));


            stateCreateInfo = VkPipelineVertexInputStateCreateInfo.calloc();
            stateCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO);

        }
    }

    public void set(VertexShaderInput input) {
        stateCreateInfo.pVertexBindingDescriptions(VkVertexInputBindingDescription.callocStack(1).put(0,input.bindingDescription));
        stateCreateInfo.pVertexAttributeDescriptions(input.parts);
    }

    public void cleanup() {
        vkDestroyShaderModule(vulkanWindow.device.device, shader,null);
    }
}
