package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.LogicalDevice;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.GraphicsPipeline;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.IndexBuffer;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.RenderPass;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline.VertexBuffer;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanTexture;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.Shader;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.ShaderCompiler;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.Viewport;
import dev.hilligans.ourcraft.Client.Rendering.VertexMesh;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.Resource.ResourceLocation;
import dev.hilligans.ourcraft.Util.NamedThreadFactory;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanDefaultImpl implements IDefaultEngineImpl<VulkanWindow, VulkanBaseGraphicsContext> {

    public VulkanEngine engine;
    public AtomicBoolean running = new AtomicBoolean(true);

    public AtomicLong pipelineIndex = new AtomicLong();
    public final Long2ObjectOpenHashMap<GraphicsPipeline> pipelines = new Long2ObjectOpenHashMap<>();
    public final Long2ObjectOpenHashMap<VulkanTexture> textures = new Long2ObjectOpenHashMap<>();

    public final Long2ObjectOpenHashMap<VertexBuffer> vertexBuffers = new Long2ObjectOpenHashMap<>();
    public final Long2ObjectOpenHashMap<IndexBuffer> indexBuffers = new Long2ObjectOpenHashMap<>();
    public final Long2LongOpenHashMap vertexToIndexMap = new Long2LongOpenHashMap();

    public final HashMap<String, Shader> shaderCache = new HashMap<>();
    public ForkJoinPool pool = new ForkJoinPool(1);
    public boolean asyncShaderLoading = true;
    public boolean exceptionOnWarning = true;

    public VulkanDefaultImpl(VulkanEngine vulkanEngine) {
        this.engine = vulkanEngine;
    }


    @Override
    public void drawMesh(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        System.out.println("Drawing");

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindVertexBuffers(graphicsContext.getBuffer(), 0, memoryStack.longs(meshID), memoryStack.longs(0));

            synchronized (vertexToIndexMap) {
                vkCmdBindIndexBuffer(graphicsContext.getBuffer(), vertexToIndexMap.get(meshID), 0, VK_INDEX_TYPE_UINT32);
            }

            vkCmdDrawIndexed(graphicsContext.getBuffer(), length, 1, (int) indicesIndex, 0, 0);
        }
    }

    @Override
    public long createMesh(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, VertexMesh mesh) {
        VertexBuffer vertexBuffer = new VertexBuffer(graphicsContext.getDevice(), mesh.vertices, graphicsContext.getCommandBuffer());
        synchronized (vertexBuffers) {
            vertexBuffers.put(vertexBuffer.buffer.buffer, vertexBuffer);
        }
        IndexBuffer indexBuffer = new IndexBuffer(graphicsContext.getDevice(), mesh.indices, graphicsContext.getCommandBuffer());
        synchronized (indexBuffers) {
            indexBuffers.put(indexBuffer.buffer.buffer, indexBuffer);
        }
        synchronized (vertexToIndexMap) {
            vertexToIndexMap.put(vertexBuffer.buffer.buffer, indexBuffer.buffer.buffer);
        }

        return vertexBuffer.buffer.buffer;
    }

    @Override
    public void destroyMesh(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, long mesh) {
        synchronized (vertexBuffers) {
            vertexBuffers.remove(mesh).cleanup();
        }
        long indexBuffer;
        synchronized (vertexToIndexMap) {
            indexBuffer = vertexToIndexMap.remove(mesh);
        }
        synchronized (indexBuffers) {
            indexBuffers.remove(indexBuffer).cleanup();
        }
    }

    @Override
    public long createTexture(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        VulkanTexture vulkanTexture = new VulkanTexture(graphicsContext.getCommandBuffer(), graphicsContext.getDevice(), buffer, width, height, format);
        synchronized (textures) {
            textures.put(vulkanTexture.image, vulkanTexture);
        }
        return vulkanTexture.image;
    }

    @Override
    public void destroyTexture(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, long texture) {
        synchronized (textures) {
            textures.remove(texture).cleanup();
        }
    }

    @Override
    public void drawAndDestroyMesh(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh) {
        System.out.println("DrawingANdDestroyed");
    }

    @Override
    public void bindTexture(VulkanWindow window, VulkanBaseGraphicsContext gc, long texture) {
        VulkanGraphicsContext graphicsContext = (VulkanGraphicsContext)gc;
        if (texture != graphicsContext.texture) {

        }
    }

    @Override
    public void bindPipeline(VulkanWindow window, VulkanBaseGraphicsContext gc, long pipeline) {
        VulkanGraphicsContext graphicsContext = (VulkanGraphicsContext)gc;
        if (pipeline != graphicsContext.program) {
            vkCmdBindPipeline(graphicsContext.getBuffer(), VK_PIPELINE_BIND_POINT_GRAPHICS, pipeline);
            graphicsContext.program = pipeline;
            //TODO may cause problems
            graphicsContext.bindPipeline(pipelines.get(pipeline));
        }
        //TODO bind appropriate renderpass
    }

    @Override
    public void setState(VulkanWindow window, VulkanBaseGraphicsContext graphicsContext, PipelineState state) {
        if(graphicsContext.pipelineStateSet) {
            throw new VulkanEngineException("Graphics state was already set by the render task and cannot be reset inside the render task");
        }
    }

    @Override
    public long createProgram(VulkanBaseGraphicsContext graphicsContext, ShaderSource shaderSource) {
        LogicalDevice device = graphicsContext.getDevice();

        GraphicsPipeline graphicsPipeline = new GraphicsPipeline(device, shaderSource);

        if(asyncShaderLoading) {
            submitShader(shaderSource.vertexShader, shaderSource.modContent.getModID(), device, VK_SHADER_STAGE_VERTEX_BIT);
            submitShader(shaderSource.fragmentShader, shaderSource.modContent.getModID(), device, VK_SHADER_STAGE_FRAGMENT_BIT);
        } else {
            compileShaderAndPut(shaderSource.vertexShader, shaderSource.modContent.getModID(), device, VK_SHADER_STAGE_VERTEX_BIT);
            compileShaderAndPut(shaderSource.fragmentShader, shaderSource.modContent.getModID(), device, VK_SHADER_STAGE_FRAGMENT_BIT);

            Shader vertexShader;
            Shader fragmentShader;
            synchronized (shaderCache) {
                vertexShader = shaderCache.get(shaderSource.vertexShader);
                fragmentShader = shaderCache.get(shaderSource.fragmentShader);
            }
            if (vertexShader == null || fragmentShader == null) {
                return -1;
            }
            graphicsPipeline.build(graphicsContext.getWindow().renderPass, graphicsContext.getWindow().viewport, vertexShader, fragmentShader);
        }

        int offset = 0;
        if(shaderSource.uniformNames != null) {
            shaderSource.uniformIndexes = new int[shaderSource.uniformNames.size()];
            for(int x = 0; x < shaderSource.uniformNames.size(); x++) {
                shaderSource.uniformIndexes[x] = offset;
                offset += switch (shaderSource.uniformTypes.get(x)) {
                    case "4fv" -> 16 * 4;
                    case "4f" -> 4 * 4;
                    default -> throw new VulkanEngineException("Unknown uniform type: " + shaderSource.uniformTypes.get(x));
                };
                if(offset > 128) {
                    if (exceptionOnWarning) {
                        throw new VulkanEngineException("Vulkan spec only guarantees 128 bytes for push constants");
                    }
                }
            }
        }

        synchronized (pipelines) {
            pipelines.put(graphicsPipeline.pipeline, graphicsPipeline);
        }
        return graphicsPipeline.pipeline;
    }

    @Override
    public void uploadData(VulkanBaseGraphicsContext gc, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {
        VulkanGraphicsContext graphicsContext = (VulkanGraphicsContext)gc;
        GraphicsPipeline pipeline = graphicsContext.boundPipeline;
        if(pipeline == null) {
            throw new VulkanEngineException("Bound graphics pipeline is null");
        }
        vkCmdPushConstants(graphicsContext.getBuffer(), pipeline.layout.pipeline, VK_SHADER_STAGE_VERTEX_BIT, (int)index, data);
    }

    public void submitShader(String path, String modID, LogicalDevice device, int bit) {
        synchronized (shaderCache) {
            if(!shaderCache.containsKey(path)) {
                shaderCache.put(path, null);
                System.out.println("Waiting " + path);
                pool.submit(() -> {
                    compileShaderAndPut(path, modID, device, bit);
                });
            }
        }
    }

    public void compileShaderAndPut(String path, String modID, LogicalDevice device, int bit) {
        String newPath = path.substring(0, path.length() - 5) + ".vulkan.glsl";
        try {
            String shaderSource = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(newPath, modID));
            if(shaderSource == null) {
                System.out.println("Failed to load vulkan shader " + path);
                return;
            }
            Shader shader = new Shader(device, ShaderCompiler.compileShader(shaderSource, newPath, bit), bit);
            synchronized (shaderCache) {
                if(running.get()) {
                    System.out.println("Putting " + path);
                    shaderCache.put(path, shader);
                } else {
                    shader.free();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to compile shader " + newPath);
            e.printStackTrace();
            throw e;
        }
    }

    public void compilePipelines(RenderPass renderPass, Viewport viewport) {
        //Wait for queue to be done compiling
        if(asyncShaderLoading) {
            boolean res = pool.awaitQuiescence(1000, TimeUnit.SECONDS);
            for (GraphicsPipeline graphicsPipeline : pipelines.values()) {
                Shader vertexShader = shaderCache.get(graphicsPipeline.shaderSource.vertexShader);
                Shader fragmentShader = shaderCache.get(graphicsPipeline.shaderSource.fragmentShader);
                if(vertexShader == null || fragmentShader == null) {
                    System.out.println("Missing shader " + graphicsPipeline.shaderSource.vertexShader);
                    continue;
                }
                graphicsPipeline.build(renderPass, viewport, vertexShader, fragmentShader);
            }
        }
    }

    @Override
    public void cleanup() {
        running.set(false);
        for(VulkanTexture texture : textures.values()) {
            texture.cleanup();
        }
        synchronized (shaderCache) {
            for (Shader shader : shaderCache.values()) {
                if(shader != null) {
                    shader.free();
                }
            }
            shaderCache.clear();
        }
        textures.clear();
    }
}
