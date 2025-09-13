package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VkInterface;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.KHRSwapchain.VK_IMAGE_LAYOUT_PRESENT_SRC_KHR;
import static org.lwjgl.vulkan.VK10.*;

public class RenderPass {

    public VulkanWindow vulkanWindow;
    public PipelineLayout pipelineLayout;

    public VkPipelineRasterizationStateCreateInfo createInfo;
    public VkPipelineMultisampleStateCreateInfo sampleCreateInfo;

    public VkPipelineColorBlendStateCreateInfo colorBlend;
    public long renderPass;


    public RenderPass(VulkanWindow vulkanWindow, int a) {}

    public RenderPass(VulkanWindow vulkanWindow) {
        this.vulkanWindow = vulkanWindow;

        boolean depth = false;

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

            VkPipelineColorBlendAttachmentState colorPipeline;
            colorPipeline = VkPipelineColorBlendAttachmentState.calloc();
            colorPipeline.colorWriteMask(VK_COLOR_COMPONENT_R_BIT | VK_COLOR_COMPONENT_G_BIT | VK_COLOR_COMPONENT_B_BIT | VK_COLOR_COMPONENT_A_BIT);
            colorPipeline.blendEnable(false);

            VkPipelineDynamicStateCreateInfo dynamicStateCreateInfo = VkPipelineDynamicStateCreateInfo.calloc(memoryStack);
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

            ////////////////////

            VkAttachmentDescription attachmentDescription = VkAttachmentDescription.calloc(memoryStack);
            attachmentDescription.format(vulkanWindow.swapChain.surfaceFormat.format());
            attachmentDescription.samples(VK_SAMPLE_COUNT_1_BIT);
            attachmentDescription.loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR);
            attachmentDescription.storeOp(VK_ATTACHMENT_STORE_OP_STORE);

            attachmentDescription.stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE);
            attachmentDescription.stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE);
            attachmentDescription.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED);
            attachmentDescription.finalLayout(VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);

            if(depth) {
                VkAttachmentDescription depthAttachment = VkAttachmentDescription.calloc(memoryStack);
                //TODO add
                // depthAttachment.format = findDepthFormat();
                depthAttachment.samples(VK_SAMPLE_COUNT_1_BIT);
                depthAttachment.loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR);
                depthAttachment.storeOp(VK_ATTACHMENT_STORE_OP_DONT_CARE);
                depthAttachment.stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE);
                depthAttachment.stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE);
                depthAttachment.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED);
                depthAttachment.finalLayout(VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL);

            }
            VkAttachmentReference.Buffer attachmentReference = VkAttachmentReference.calloc(1, memoryStack);
            attachmentReference.get(0).layout(VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);
            attachmentReference.get(0).attachment(0);

            VkSubpassDescription description = VkSubpassDescription.calloc(memoryStack);
            description.pipelineBindPoint(VK_PIPELINE_BIND_POINT_GRAPHICS);
            description.colorAttachmentCount(1);
            description.pColorAttachments(attachmentReference);

            VkSubpassDependency dependency = VkSubpassDependency.calloc(memoryStack);
            dependency.srcSubpass(VK_SUBPASS_EXTERNAL);
            dependency.dstSubpass(0);
            dependency.srcStageMask(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
            dependency.srcAccessMask(0);
            dependency.dstStageMask(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
            dependency.dstAccessMask(VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);

            VkAttachmentDescription.Buffer descriptionBuffer = VkAttachmentDescription.calloc(1, memoryStack);
            descriptionBuffer.put(0, attachmentDescription);
            VkSubpassDescription.Buffer subpassBuffer = VkSubpassDescription.calloc(1, memoryStack);
            subpassBuffer.put(0, description);

            VkSubpassDependency.Buffer dependencies = VkSubpassDependency.calloc(1, memoryStack);
            dependencies.put(0, dependency);


            VkPipelineInputAssemblyStateCreateInfo pipelineInputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo.calloc(memoryStack);
            pipelineInputAssemblyStateCreateInfo.primitiveRestartEnable(false);
            pipelineInputAssemblyStateCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO);
            pipelineInputAssemblyStateCreateInfo.topology(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST);

            VkRenderPassCreateInfo renderPassInfo = VkRenderPassCreateInfo.calloc(memoryStack);
            renderPassInfo.sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO);
            renderPassInfo.pAttachments(descriptionBuffer);
            renderPassInfo.pSubpasses(subpassBuffer);
            renderPassInfo.pDependencies(dependencies);

            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreateRenderPass(vulkanWindow.device.device, renderPassInfo, null, longBuffer) != VK_SUCCESS) {
                System.err.println("Failed to create render pass");
            }
            renderPass = longBuffer.get(0);
        }
    }

    public void startRenderPass(VulkanWindow window, FrameBuffer frameBuffer, VkCommandBuffer commandBuffer) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo.calloc(memoryStack);
            renderPassBeginInfo.sType(VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO);
            renderPassBeginInfo.renderPass(renderPass);
            renderPassBeginInfo.framebuffer(frameBuffer.frameBuffer);

            renderPassBeginInfo.renderArea(a -> a.extent().set(vulkanWindow.vulkanWidth, vulkanWindow.vulkanHeight));
            VkClearValue vkClearValue = VkClearValue.calloc(memoryStack);
            vkClearValue.color(VkInterface.clearValue(window.clearColor.x, window.clearColor.y, window.clearColor.z, window.clearColor.w));
            VkClearValue.Buffer buffer = VkClearValue.calloc(1, memoryStack);
            buffer.put(0, vkClearValue);
            renderPassBeginInfo.pClearValues(buffer);

            vkCmdBeginRenderPass(commandBuffer, renderPassBeginInfo, VK_SUBPASS_CONTENTS_INLINE);
        }
    }

    public void endRenderPass(VkCommandBuffer commandBuffer) {
        vkCmdEndRenderPass(commandBuffer);
    }

    public void freeInit() {
        createInfo.free();
        sampleCreateInfo.free();
        colorBlend.pAttachments().free();
        colorBlend.free();
    }

    public void free() {
        vkDestroyRenderPass(vulkanWindow.device.device,renderPass,null);
        pipelineLayout.free();
    }
}
