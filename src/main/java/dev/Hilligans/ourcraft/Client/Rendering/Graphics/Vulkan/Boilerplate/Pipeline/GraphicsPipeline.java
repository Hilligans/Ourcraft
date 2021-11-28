package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public class GraphicsPipeline {

    public VulkanWindow vulkanWindow;
    public long pipeline;

    public GraphicsPipeline(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;

        VkAttachmentDescription attachmentDescription = VkAttachmentDescription.calloc();
        attachmentDescription.format(vulkanWindow.swapChain.surfaceFormat.format());
        attachmentDescription.samples(VK_SAMPLE_COUNT_1_BIT);
        attachmentDescription.loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR);
        attachmentDescription.storeOp(VK_ATTACHMENT_STORE_OP_STORE);

        attachmentDescription.stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE);
        attachmentDescription.stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE);
        attachmentDescription.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED);
        attachmentDescription.finalLayout(VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);


        VkAttachmentReference.Buffer attachmentReference = VkAttachmentReference.calloc(1);
        attachmentReference.get(0).layout(VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);
        attachmentReference.get(0).attachment(0);

        VkSubpassDescription description = VkSubpassDescription.calloc();
        description.pipelineBindPoint(VK_PIPELINE_BIND_POINT_GRAPHICS);
        description.colorAttachmentCount(1);
        description.pColorAttachments(attachmentReference);

        VkSubpassDependency dependency = VkSubpassDependency.calloc();
        dependency.srcSubpass(VK_SUBPASS_EXTERNAL);
        dependency.dstSubpass(0);
        dependency.srcStageMask(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
        dependency.srcAccessMask(0);
        dependency.dstStageMask(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
        dependency.dstAccessMask(VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);

        VkAttachmentDescription.Buffer descriptionBuffer = VkAttachmentDescription.calloc(1);
        descriptionBuffer.put(0,attachmentDescription);
        VkSubpassDescription.Buffer subpassBuffer = VkSubpassDescription.calloc(1);
        subpassBuffer.put(0,description);

        VkSubpassDependency.Buffer dependencies = VkSubpassDependency.calloc(1);
        dependencies.put(0,dependency);


        VkPipelineInputAssemblyStateCreateInfo pipelineInputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo.calloc();
        pipelineInputAssemblyStateCreateInfo.primitiveRestartEnable(false);
        pipelineInputAssemblyStateCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO);
        pipelineInputAssemblyStateCreateInfo.topology(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST);

        VkRenderPassCreateInfo renderPassInfo = VkRenderPassCreateInfo.calloc();
        renderPassInfo.sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO);
        renderPassInfo.pAttachments(descriptionBuffer);
        renderPassInfo.pSubpasses(subpassBuffer);
        renderPassInfo.pDependencies(dependencies);

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreateRenderPass(vulkanWindow.device.device, renderPassInfo, null, longBuffer) != VK_SUCCESS) {
                System.err.println("Failed to create render pass");
            }
            vulkanWindow.renderPass.renderPass = longBuffer.get(0);

            VkGraphicsPipelineCreateInfo graphicsPipelineCreateInfo = VkGraphicsPipelineCreateInfo.calloc();
            graphicsPipelineCreateInfo.sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO);
            VkPipelineShaderStageCreateInfo.Buffer stageCreateInfosBuffer = VkPipelineShaderStageCreateInfo.calloc(2);
            stageCreateInfosBuffer.put(0, vulkanWindow.vertexShader.shaderCreateInfo);
            stageCreateInfosBuffer.put(1, vulkanWindow.fragmentShader.shaderCreateInfo);
            graphicsPipelineCreateInfo.pStages(stageCreateInfosBuffer);
            graphicsPipelineCreateInfo.pVertexInputState(vulkanWindow.vertexShader.stateCreateInfo);
            graphicsPipelineCreateInfo.pInputAssemblyState(pipelineInputAssemblyStateCreateInfo);
            graphicsPipelineCreateInfo.pViewportState(vulkanWindow.viewport.viewportCreateInfo);
            graphicsPipelineCreateInfo.pRasterizationState(vulkanWindow.renderPass.createInfo);
            graphicsPipelineCreateInfo.pMultisampleState(vulkanWindow.renderPass.sampleCreateInfo);
            graphicsPipelineCreateInfo.pColorBlendState(vulkanWindow.renderPass.colorBlend);
            graphicsPipelineCreateInfo.layout(vulkanWindow.renderPass.pipelineLayout.pipeline);
            graphicsPipelineCreateInfo.renderPass(vulkanWindow.renderPass.renderPass);
            graphicsPipelineCreateInfo.subpass(0);

            VkGraphicsPipelineCreateInfo.Buffer buffer3 = VkGraphicsPipelineCreateInfo.calloc(1);
            buffer3.put(0, graphicsPipelineCreateInfo);

            if (vkCreateGraphicsPipelines(vulkanWindow.device.device, VK_NULL_HANDLE, buffer3, null, longBuffer) != VK_SUCCESS) {
                vulkanWindow.device.vulkanInstance.exit("Failed to create pipeline");
            }
            pipeline = longBuffer.get(0);
        }
    }

    public void cleanup() {
        vkDestroyPipeline(vulkanWindow.device.device,pipeline,null);
    }



}
