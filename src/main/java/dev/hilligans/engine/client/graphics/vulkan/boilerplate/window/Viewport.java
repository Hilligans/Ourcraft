package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkViewport;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO;

public class Viewport {

    public VulkanWindow vulkanWindow;

    public VkViewport vkViewport;
    public VkRect2D scissor;
    public VkPipelineViewportStateCreateInfo viewportCreateInfo;

    public Viewport(VulkanWindow vulkanWindow) {
        scissor = VkRect2D.calloc();
        vkViewport = VkViewport.calloc();
        vkViewport.set(0,0, vulkanWindow.vulkanWidth, vulkanWindow.vulkanHeight,0.0f,1.0f);
        scissor.offset(vkOffset2D -> vkOffset2D.set(0,0));
        scissor.extent(vulkanWindow.extent2D);
        viewportCreateInfo = VkPipelineViewportStateCreateInfo.calloc();
        viewportCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO);
        VkRect2D.Buffer scissorBuffer = VkRect2D.calloc(1);
        scissorBuffer.put(0,scissor);
        VkViewport.Buffer viewportBuffer = VkViewport.calloc(1);
        viewportBuffer.put(0,vkViewport);
        viewportCreateInfo.viewportCount(1);
        viewportCreateInfo.pViewports(viewportBuffer);
        viewportCreateInfo.scissorCount(1);
        viewportCreateInfo.pScissors(scissorBuffer);
    }

    public void free() {
        scissor.free();
        vkViewport.free();
        viewportCreateInfo.free();
    }
}
