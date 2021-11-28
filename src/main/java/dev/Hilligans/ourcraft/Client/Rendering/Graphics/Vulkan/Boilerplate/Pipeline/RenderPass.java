package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VkInterface;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import static org.lwjgl.vulkan.VK10.*;

public class RenderPass {

    public VulkanWindow vulkanWindow;
    public PipelineLayout pipelineLayout;

    public VkPipelineRasterizationStateCreateInfo createInfo;
    public VkPipelineMultisampleStateCreateInfo sampleCreateInfo;
    public VkPipelineColorBlendAttachmentState colorPipeline;

    public VkPipelineColorBlendStateCreateInfo colorBlend;
    public long renderPass;


    public RenderPass(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            createInfo = VkPipelineRasterizationStateCreateInfo.calloc();
            createInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO);
            createInfo.depthClampEnable(false);
            createInfo.rasterizerDiscardEnable(false);
            createInfo.polygonMode(VK_POLYGON_MODE_FILL);
            createInfo.lineWidth(1.0f);
            createInfo.cullMode(VK_CULL_MODE_BACK_BIT);
            createInfo.frontFace(VK_FRONT_FACE_CLOCKWISE);
            createInfo.depthBiasEnable(false);

            sampleCreateInfo = VkPipelineMultisampleStateCreateInfo.calloc();
            sampleCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO);
            sampleCreateInfo.sampleShadingEnable(false);
            sampleCreateInfo.rasterizationSamples(VK_SAMPLE_COUNT_1_BIT);

            colorPipeline = VkPipelineColorBlendAttachmentState.calloc();
            colorPipeline.colorWriteMask(VK_COLOR_COMPONENT_R_BIT | VK_COLOR_COMPONENT_G_BIT | VK_COLOR_COMPONENT_B_BIT | VK_COLOR_COMPONENT_A_BIT);
            colorPipeline.blendEnable(false);

            VkPipelineDynamicStateCreateInfo dynamicStateCreateInfo = VkPipelineDynamicStateCreateInfo.calloc();
            dynamicStateCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO);
            dynamicStateCreateInfo.pDynamicStates(memoryStack.ints(VK_DYNAMIC_STATE_VIEWPORT, VK_DYNAMIC_STATE_LINE_WIDTH));

            pipelineLayout = new PipelineLayout(this);

            VkPipelineColorBlendAttachmentState.Buffer buffer = VkPipelineColorBlendAttachmentState.calloc(1);
            buffer.put(0, colorPipeline);

            colorBlend = VkPipelineColorBlendStateCreateInfo.calloc();
            colorBlend.sType(VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO);
            colorBlend.logicOpEnable(false);
            colorBlend.pAttachments(buffer);
            colorBlend.blendConstants(0, 0.0f);
            colorBlend.blendConstants(1, 0.0f);
            colorBlend.blendConstants(2, 0.0f);
            colorBlend.blendConstants(3, 0.0f);
        }
    }

    public void createRenderPass(int index,VertexBuffer vertexBuffer) {
        VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.calloc();
        renderPassBeginInfo.sType(VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO);
        renderPassBeginInfo.renderPass(renderPass);
        renderPassBeginInfo.framebuffer(vulkanWindow.frameBuffers.get(index).frameBuffer);


        renderPassBeginInfo.renderArea(a -> a.extent().set(vulkanWindow.vulkanWidth, vulkanWindow.vulkanHeight));
        VkClearValue vkClearValue = VkClearValue.calloc();
        vkClearValue.color(VkInterface.clearValue(0,1,0,1));
        VkClearValue.Buffer buffer = VkClearValue.calloc(1);
        buffer.put(0,vkClearValue);
        renderPassBeginInfo.pClearValues(buffer);

        vkCmdBeginRenderPass(vulkanWindow.commandBuffer.commandBufferList.get(index),renderPassBeginInfo,VK_SUBPASS_CONTENTS_INLINE);
        vkCmdBindPipeline(vulkanWindow.commandBuffer.commandBufferList.get(index),VK_PIPELINE_BIND_POINT_GRAPHICS, vulkanWindow.graphicsPipeline.pipeline);
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindVertexBuffers(vulkanWindow.commandBuffer.commandBufferList.get(index), 0, memoryStack.longs(vertexBuffer.buffer),memoryStack.longs(0));
        }
        vkCmdDraw(vulkanWindow.commandBuffer.commandBufferList.get(index),3,1,0,0);


        vkCmdEndRenderPass(vulkanWindow.commandBuffer.commandBufferList.get(index));
        vkEndCommandBuffer(vulkanWindow.commandBuffer.commandBufferList.get(index));
    }

    public void cleanup() {
        vkDestroyRenderPass(vulkanWindow.device.device,renderPass,null);
        pipelineLayout.cleanup();
    }


}
