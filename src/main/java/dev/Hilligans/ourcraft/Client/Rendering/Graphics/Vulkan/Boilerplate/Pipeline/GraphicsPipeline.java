package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.Shader;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.Viewport;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public class GraphicsPipeline {

    public VulkanWindow vulkanWindow;
    public long pipeline;

    public GraphicsPipeline(VulkanWindow vulkanWindow, RenderPass renderPass, Viewport viewport, Shader vertexShader, Shader fragmentShader) {
        this.vulkanWindow = vulkanWindow;

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.mallocLong(1);

            VkPipelineInputAssemblyStateCreateInfo pipelineInputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo.calloc(memoryStack);
            pipelineInputAssemblyStateCreateInfo.primitiveRestartEnable(false);
            pipelineInputAssemblyStateCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO);
            pipelineInputAssemblyStateCreateInfo.topology(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST);

            VkGraphicsPipelineCreateInfo graphicsPipelineCreateInfo = VkGraphicsPipelineCreateInfo.calloc(memoryStack);
            graphicsPipelineCreateInfo.sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO);
            VkPipelineShaderStageCreateInfo.Buffer stageCreateInfosBuffer = VkPipelineShaderStageCreateInfo.calloc(2, memoryStack);
            stageCreateInfosBuffer.put(0, vertexShader.shaderCreateInfo);
            stageCreateInfosBuffer.put(1, fragmentShader.shaderCreateInfo);
            graphicsPipelineCreateInfo.pStages(stageCreateInfosBuffer);
            graphicsPipelineCreateInfo.pVertexInputState(vulkanWindow.vertexShader.stateCreateInfo);
            graphicsPipelineCreateInfo.pInputAssemblyState(pipelineInputAssemblyStateCreateInfo);
            graphicsPipelineCreateInfo.pViewportState(viewport.viewportCreateInfo);
            graphicsPipelineCreateInfo.pRasterizationState(renderPass.createInfo);
            graphicsPipelineCreateInfo.pMultisampleState(renderPass.sampleCreateInfo);
            graphicsPipelineCreateInfo.pColorBlendState(renderPass.colorBlend);
            graphicsPipelineCreateInfo.layout(renderPass.pipelineLayout.pipeline);
            graphicsPipelineCreateInfo.renderPass(renderPass.renderPass);
            graphicsPipelineCreateInfo.subpass(0);

            VkGraphicsPipelineCreateInfo.Buffer buffer3 = VkGraphicsPipelineCreateInfo.calloc(1, memoryStack);
            buffer3.put(0, graphicsPipelineCreateInfo);

            //longBuffer = memoryStack.callocLong(1);

            if (vkCreateGraphicsPipelines(vulkanWindow.device.device, VK_NULL_HANDLE, buffer3, null, longBuffer) != VK_SUCCESS) {
                vulkanWindow.device.vulkanInstance.exit("Failed to create pipeline");
            }

            vulkanWindow.vertexShader.freeInit();
            renderPass.freeInit();

            pipeline = longBuffer.get(0);
        }
    }

    public void cleanup() {
        vkDestroyPipeline(vulkanWindow.device.device, pipeline,null);
    }
}
