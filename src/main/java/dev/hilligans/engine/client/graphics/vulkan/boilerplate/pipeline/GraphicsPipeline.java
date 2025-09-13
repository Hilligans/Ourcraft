package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.Shader;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.Viewport;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class GraphicsPipeline {

    public LogicalDevice device;
    public ShaderSource shaderSource;
    public PipelineLayout layout;
    public long pipeline;


    public GraphicsPipeline(LogicalDevice device, ShaderSource shaderSource) {
        this.device = device;
        this.shaderSource = shaderSource;
    }

    public void addToBuffer(MemoryStack memoryStack, int bufferIndex, VkGraphicsPipelineCreateInfo.Buffer buffer, RenderPass renderPass, Viewport viewport, Shader vertexShader, Shader fragmentShader) {
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
        graphicsPipelineCreateInfo.pVertexInputState(vertexShader.stateCreateInfo);
        graphicsPipelineCreateInfo.pInputAssemblyState(pipelineInputAssemblyStateCreateInfo);
        graphicsPipelineCreateInfo.pViewportState(viewport.viewportCreateInfo);
        graphicsPipelineCreateInfo.pRasterizationState(renderPass.createInfo);
        graphicsPipelineCreateInfo.pMultisampleState(renderPass.sampleCreateInfo);
        graphicsPipelineCreateInfo.pColorBlendState(renderPass.colorBlend);
        graphicsPipelineCreateInfo.layout(renderPass.pipelineLayout.pipeline);
        graphicsPipelineCreateInfo.renderPass(renderPass.renderPass);
        graphicsPipelineCreateInfo.subpass(0);

        buffer.put(bufferIndex, graphicsPipelineCreateInfo);

        this.layout = renderPass.pipelineLayout;
    }

    public long build(RenderPass renderPass, Viewport viewport, Shader vertexShader, Shader fragmentShader) {
        if(pipeline != 0) {
            vkDestroyPipeline(device.device, pipeline,null);
        }
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.mallocLong(1);

            VkGraphicsPipelineCreateInfo.Buffer buffer3 = VkGraphicsPipelineCreateInfo.calloc(1, memoryStack);
            addToBuffer(memoryStack, 0, buffer3, renderPass, viewport, vertexShader, fragmentShader);

            if (vkCreateGraphicsPipelines(device.device, VK_NULL_HANDLE, buffer3, null, longBuffer) != VK_SUCCESS) {
                device.vulkanInstance.exit("Failed to create pipeline");
            }

            pipeline = longBuffer.get(0);
            return pipeline;
        }
    }

    public void cleanup() {
        vkDestroyPipeline(device.device, pipeline,null);
    }
}
