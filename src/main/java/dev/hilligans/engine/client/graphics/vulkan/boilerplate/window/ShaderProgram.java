package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;

import static org.lwjgl.vulkan.VK10.*;

public class ShaderProgram {

    public Shader vertexShader;
    public Shader fragmentShader;

    public VkPipelineVertexInputStateCreateInfo vertexCreateInfo;
    public VkPipelineInputAssemblyStateCreateInfo pipelineCreateInfo;

    public ShaderProgram(Shader vertexShader, Shader fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;

        vertexCreateInfo = VkPipelineVertexInputStateCreateInfo.calloc();
        vertexCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO);


        pipelineCreateInfo = VkPipelineInputAssemblyStateCreateInfo.calloc();
        pipelineCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO);
        pipelineCreateInfo.topology(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST);
        pipelineCreateInfo.primitiveRestartEnable(false);
    }
}
