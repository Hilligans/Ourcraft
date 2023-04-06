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
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.vulkan.VK10.*;

public class VulkanDefaultImpl implements IDefaultEngineImpl<VulkanWindow, VulkanGraphicsContext> {

    public VulkanEngine engine;

    public Long2ObjectOpenHashMap<Tuple<VertexBuffer, IndexBuffer>> meshes = new Long2ObjectOpenHashMap<>();

    public AtomicLong pipelineIndex = new AtomicLong();
    public final Long2ObjectOpenHashMap<GraphicsPipeline> pipelines = new Long2ObjectOpenHashMap<>();

    public final HashMap<String, Shader> shaderCache = new HashMap<>();
    public ForkJoinPool pool = new ForkJoinPool(1);
    public boolean asyncShaderLoading = true;
    public boolean exceptionOnWarning = true;

    public Long2LongOpenHashMap indexIndex = new Long2LongOpenHashMap();
    public VulkanDefaultImpl(VulkanEngine vulkanEngine) {
        this.engine = vulkanEngine;
    }


    @Override
    public void drawMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        System.out.println("Drawing");

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindVertexBuffers(graphicsContext.getBuffer(), 0, memoryStack.longs(meshID), memoryStack.longs(0));
            vkCmdBindIndexBuffer(graphicsContext.getBuffer(), indexIndex.get(meshID), 0, VK_INDEX_TYPE_UINT32);

            vkCmdDrawIndexed(graphicsContext.getBuffer(), length, 1, (int) indicesIndex, 0, 0);
        }
    }

    @Override
    public long createMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, VertexMesh mesh) {
        //TODO avoid the double copying
        VertexBuffer vertexBuffer = new VertexBuffer(graphicsContext.device, mesh.vertices, graphicsContext.getBuffer());
       // IndexBuffer indexBuffer = new IndexBuffer();

        //VertexBuffer vertexBuffer = new VertexBuffer(graphicsContext.getDevice()).putData(mesh.vertices);
       // IndexBuffer indexBuffer = new IndexBuffer(graphicsContext.getDevice()).putData(mesh.indices);
      //  indexIndex.put(vertexBuffer.buffer, indexBuffer.buffer);
       // meshes.put(vertexBuffer.buffer, new Tuple<>(vertexBuffer, indexBuffer));
       // return vertexBuffer.buffer;
        return 0;
    }

    @Override
    public void destroyMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, long mesh) {
        Tuple<VertexBuffer, IndexBuffer> meshData = new Tuple<>();
        //TODO free when resources are no longer in use
        //meshData.getTypeA().cleanup();
    }

    @Override
    public long createTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        System.out.println("Texture");
        return 0;
    }

    @Override
    public void destroyTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, long texture) {

    }

    @Override
    public void drawAndDestroyMesh(VulkanWindow window, VulkanGraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh) {
        System.out.println("Drawing");
    }

    @Override
    public void bindTexture(VulkanWindow window, VulkanGraphicsContext graphicsContext, long texture) {
        if (texture != graphicsContext.texture) {

        }
    }

    @Override
    public void bindPipeline(VulkanWindow window, VulkanGraphicsContext graphicsContext, long pipeline) {
        if (pipeline != graphicsContext.program) {
            vkCmdBindPipeline(graphicsContext.getBuffer(), VK_PIPELINE_BIND_POINT_GRAPHICS, pipeline);
            graphicsContext.program = pipeline;
            //TODO may cause problems
            graphicsContext.bindPipeline(pipelines.get(pipeline));
        }
        //TODO bind appropriate renderpass

    }

    @Override
    public void setState(VulkanWindow window, VulkanGraphicsContext graphicsContext, PipelineState state) {
        if(graphicsContext.pipelineStateSet) {
            throw new VulkanEngineException("Graphics state was already set by the render task and cannot be reset inside the render task");
        }

    }

    @Override
    public long createProgram(VulkanGraphicsContext graphicsContext, ShaderSource shaderSource) {
        LogicalDevice device = graphicsContext.device;

        GraphicsPipeline graphicsPipeline = new GraphicsPipeline(graphicsContext.device, shaderSource);

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
            graphicsPipeline.build(graphicsContext.window.renderPass, graphicsContext.window.viewport, vertexShader, fragmentShader);
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
    public void uploadData(VulkanGraphicsContext graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {
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
                pool.submit(() -> {
                    compileShaderAndPut(path, modID, device, bit);
                });
            }
        }
    }

    public void compileShaderAndPut(String path, String modID, LogicalDevice device, int bit) {
        String newPath = path.substring(0, path.length() - 5) + ".vulkan.glsl";
        try {
            System.out.println("Yew " + pipelineIndex.getAndIncrement());
            String shaderSource = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(newPath, modID));
            if(shaderSource == null) {
                System.out.println("Failed to load vulkan shader " + path);
                return;
            }
            Shader shader = new Shader(device, ShaderCompiler.compileShader(shaderSource, newPath, bit), bit);
            synchronized (shaderCache) {
                shaderCache.put(path, shader);
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
}
