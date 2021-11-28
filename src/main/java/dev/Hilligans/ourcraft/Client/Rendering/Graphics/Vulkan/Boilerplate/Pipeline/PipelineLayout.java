package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class PipelineLayout {

    public RenderPass renderPipeline;
    public VkPipelineLayoutCreateInfo pipelineLayoutCreateInfo;

    public long pipeline;

    public PipelineLayout(RenderPass renderPipeline) {
        this.renderPipeline = renderPipeline;
        pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo.calloc();
        pipelineLayoutCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO);

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            if (vkCreatePipelineLayout(renderPipeline.vulkanWindow.device.device, pipelineLayoutCreateInfo, null, longBuffer) != VK_SUCCESS) {
                renderPipeline.vulkanWindow.device.vulkanInstance.exit("Failed to make pipeline layout");
            }
            pipeline = longBuffer.get(0);
        }
    }

    public void cleanup() {
        vkDestroyPipelineLayout(renderPipeline.vulkanWindow.device.device,pipeline,null);
    }


}
