package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.IndexBuffer;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.VertexBuffer;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.ShaderCompiler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.WorldSave.FileLoader;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkWriteDescriptorSet;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanDefaultImpl implements IDefaultEngineImpl<VulkanWindow, VulkanGraphicsContext> {

    public VulkanEngine engine;

    public Long2ObjectOpenHashMap<Tuple<VertexBuffer, IndexBuffer>> meshes = new Long2ObjectOpenHashMap<>();
    public Long2LongOpenHashMap indexIndex = new Long2LongOpenHashMap();
    public VulkanDefaultImpl(VulkanEngine vulkanEngine) {
        this.engine = vulkanEngine;
    }


    @Override
    public void drawMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, MatrixStack matrixStack, long texture, long program, long meshID, long indicesIndex, int length) {
        if(program != graphicsContext.program) {
            vkCmdBindPipeline(graphicsContext.getBuffer(),VK_PIPELINE_BIND_POINT_GRAPHICS, program);
            graphicsContext.program = program;
        }
        if(texture != graphicsContext.texture) {
        }

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindVertexBuffers(graphicsContext.getBuffer(), 0, memoryStack.longs(meshID), memoryStack.longs(0));
            vkCmdBindIndexBuffer(graphicsContext.getBuffer(), indexIndex.get(meshID), 0, VK_INDEX_TYPE_UINT32);

            vkCmdDrawIndexed(graphicsContext.getBuffer(), length, 1, (int) indicesIndex, 0, 0);
        }
    }

    @Override
    public long createMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, VertexMesh mesh) {
        //TODO avoid the double copying
        VertexBuffer vertexBuffer = new VertexBuffer(graphicsContext.getDevice()).putData(mesh.vertices);
        IndexBuffer indexBuffer = new IndexBuffer(graphicsContext.getDevice()).putData(mesh.indices);
        indexIndex.put(vertexBuffer.buffer, indexBuffer.buffer);
        meshes.put(vertexBuffer.buffer, new Tuple<>(vertexBuffer, indexBuffer));
        return vertexBuffer.buffer;
    }

    @Override
    public void destroyMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, long mesh) {
        Tuple<VertexBuffer, IndexBuffer> meshData = new Tuple<>();
        //TODO free when resources are no longer in use
        //meshData.getTypeA().cleanup();
    }

    @Override
    public long createTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return 0;
    }

    @Override
    public void destroyTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, long texture) {

    }

    @Override
    public void drawAndDestroyMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh, long texture, long program) {

    }

    @Override
    public void setState(VulkanWindow window, VulkanGraphicsContext graphicsContext, PipelineState state) {

    }

    @Override
    public long createProgram(VulkanGraphicsContext graphicsContext, ShaderSource shaderSource) {
        String vertex =  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.vertexShader, shaderSource.modContent.getModID()));
        String fragment = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.fragmentShader, shaderSource.modContent.getModID()));
        String geometry = shaderSource.geometryShader == null ? null :  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.geometryShader, shaderSource.modContent.getModID()));

        ByteBuffer vertexShader = ShaderCompiler.compileShader(vertex,ShaderCompiler.VERTEX_SHADER);
        ByteBuffer fragmentShader = ShaderCompiler.compileShader(fragment,ShaderCompiler.FRAGMENT_SHADER);
        ByteBuffer geometryShader = geometry != null ? ShaderCompiler.compileShader(geometry,ShaderCompiler.GEOMETRY_SHADER) : null;

        return 0;
    }

    @Override
    public void uploadData(VulkanGraphicsContext graphicsContext, FloatBuffer data, long index, String type, long program) {

    }


    @Override
    public long getUniformIndex(VulkanGraphicsContext graphicsContext, String name, long shader) {
        return 0;
    }

}
