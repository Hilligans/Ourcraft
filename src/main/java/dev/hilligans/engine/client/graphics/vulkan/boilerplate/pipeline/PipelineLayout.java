package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class PipelineLayout {

    public RenderPass renderPipeline;

    public long pipeline;

    public PipelineLayout(RenderPass renderPipeline) {
        this.renderPipeline = renderPipeline;

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPipelineLayoutCreateInfo pipelineLayoutCreateInfo;
            pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo.calloc(memoryStack);
            pipelineLayoutCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO);
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreatePipelineLayout(renderPipeline.vulkanWindow.device.device, pipelineLayoutCreateInfo, null, longBuffer) != VK_SUCCESS) {
                renderPipeline.vulkanWindow.device.vulkanInstance.exit("Failed to make pipeline layout");
            }
            pipeline = longBuffer.get(0);
        }
    }

    public void free() {
        vkDestroyPipelineLayout(renderPipeline.vulkanWindow.device.device,pipeline,null);
    }
}
