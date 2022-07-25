package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.ShaderCompiler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.WorldSave.FileLoader;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;

import java.nio.ByteBuffer;

public class VulkanDefaultImpl implements IDefaultEngineImpl<VulkanWindow, VulkanGraphicsContext> {

    public VulkanEngine engine;

    public VulkanDefaultImpl(VulkanEngine vulkanEngine) {
        this.engine = vulkanEngine;
    }

    @Override
    public void drawMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length) {

    }

    @Override
    public int createMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, VertexMesh mesh) {
        return 0;
    }

    @Override
    public void destroyMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, int mesh) {

    }

    @Override
    public int createTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return 0;
    }

    @Override
    public void destroyTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, int texture) {

    }

    @Override
    public void drawAndDestroyMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh, int texture, int program) {

    }

    @Override
    public void setState(VulkanWindow window, VulkanGraphicsContext graphicsContext, PipelineState state) {

    }

    @Override
    public int createProgram(VulkanGraphicsContext graphicsContext, ShaderSource shaderSource) {
        String vertex =  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.vertexShader, shaderSource.modContent.getModID()));
        String fragment = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.fragmentShader, shaderSource.modContent.getModID()));
        String geometry = shaderSource.geometryShader == null ? null :  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.geometryShader, shaderSource.modContent.getModID()));

        ByteBuffer vertexShader = ShaderCompiler.compileShader(vertex,ShaderCompiler.VERTEX_SHADER);
        ByteBuffer fragmentShader = ShaderCompiler.compileShader(fragment,ShaderCompiler.FRAGMENT_SHADER);
        ByteBuffer geometryShader = geometry != null ? ShaderCompiler.compileShader(geometry,ShaderCompiler.GEOMETRY_SHADER) : null;

        return 0;
    }

    @Override
    public void uploadData(VulkanGraphicsContext graphicsContext, float[] data, String name) {

    }

    @Override
    public void uploadData(VulkanGraphicsContext graphicsContext, float[] data, int index) {

    }

}
