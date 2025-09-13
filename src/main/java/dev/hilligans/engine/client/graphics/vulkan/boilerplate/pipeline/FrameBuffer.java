package dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline;

import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class FrameBuffer {

    public VulkanWindow vulkanWindow;
    public long frameBuffer;

    public FrameBuffer(VulkanWindow vulkanWindow, int index) {
        this.vulkanWindow = vulkanWindow;
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkFramebufferCreateInfo framebufferCreateInfo;
            framebufferCreateInfo = VkFramebufferCreateInfo.calloc(memoryStack);
            framebufferCreateInfo.sType(VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO);
            framebufferCreateInfo.renderPass(vulkanWindow.renderPass.renderPass);
            LongBuffer longBuffer = memoryStack.longs(vulkanWindow.imageView.imageViews.get(index));
            framebufferCreateInfo.pAttachments(longBuffer);
            framebufferCreateInfo.width(vulkanWindow.vulkanWidth);
            framebufferCreateInfo.height(vulkanWindow.vulkanHeight);
            framebufferCreateInfo.layers(1);
            LongBuffer pos = memoryStack.mallocLong(1);
            if (vkCreateFramebuffer(vulkanWindow.device.device, framebufferCreateInfo, null, pos) != VK_SUCCESS) {
                vulkanWindow.device.vulkanInstance.exit("failed to create frame buffer");
            }
            frameBuffer = pos.get(0);
        }
    }

    public void cleanup() {
        vkDestroyFramebuffer(vulkanWindow.device.device,frameBuffer,null);
    }
}
