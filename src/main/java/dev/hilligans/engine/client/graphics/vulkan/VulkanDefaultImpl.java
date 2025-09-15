package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.vulkan.api.IVulkanMemoryManager;
import dev.hilligans.engine.client.graphics.vulkan.api.VulkanMemoryAllocation;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.*;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline.GraphicsPipeline;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline.RenderPass;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.Shader;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.ShaderCompiler;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.Viewport;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.util.argument.Argument;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.vulkan.VK10.*;

public class VulkanDefaultImpl implements IDefaultEngineImpl<VulkanWindow, VulkanBaseGraphicsContext, VulkanMeshBuilder> {

    public static Argument<Boolean> useSeparateTransferThread = Argument.booleanArg("--useSeparateVulkanTransferThread", true);

    public IVulkanMemoryManager memoryManager;


    public VulkanEngine engine;
    public AtomicBoolean running = new AtomicBoolean(true);

    public AtomicLong pipelineIndex = new AtomicLong();
    public final Long2ObjectOpenHashMap<GraphicsPipeline> pipelines = new Long2ObjectOpenHashMap<>();
    public final Long2ObjectOpenHashMap<VulkanTexture> textures = new Long2ObjectOpenHashMap<>();

    public final Long2ObjectOpenHashMap<VulkanMesh> meshes = new Long2ObjectOpenHashMap<>();

    public final HashMap<String, Shader> shaderCache = new HashMap<>();
    public ForkJoinPool pool = new ForkJoinPool(1);
    public boolean asyncShaderLoading = true;
    public boolean exceptionOnWarning = true;


    public final Long2ObjectOpenHashMap<Semaphore> pendingTransferBuffer = new Long2ObjectOpenHashMap<>();


    public boolean noRebar = true;

    public VulkanDefaultImpl(VulkanEngine vulkanEngine) {
        this.engine = vulkanEngine;
    }


    @Override
    public void drawMesh(VulkanBaseGraphicsContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {

        VulkanMesh mesh;
        synchronized (meshes) {
            mesh = meshes.get(meshID);
        }


        /* Synchronization for ensuring that are meshes are copied before we use them */
        if(noRebar && useSeparateTransferThread.get(getGameInstance())) {

            /*
            Semaphore indexSem;
            Semaphore vertexSem;
            synchronized (pendingTransferBuffer) {
                indexSem = pendingTransferBuffer.get(indexID);
                vertexSem = pendingTransferBuffer.get(meshID);
            }
            if(indexSem != null) {
                graphicsContext.addWaitSemaphore(indexSem);
            }
            if(vertexSem != null) {
                graphicsContext.addWaitSemaphore(vertexSem);
            }

             */
        }

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindVertexBuffers(graphicsContext.getBuffer(), 0, memoryStack.longs(meshID), memoryStack.longs(0));
            vkCmdBindIndexBuffer(graphicsContext.getBuffer(), mesh.indexBuffer().getVkBuffer(), 0, VK_INDEX_TYPE_UINT32);

            vkCmdDrawIndexed(graphicsContext.getBuffer(), length, 1, (int) indicesIndex, 0, 0);
        }
    }

    @Override
    public long createMesh(VulkanBaseGraphicsContext graphicsContext, VulkanMeshBuilder mesh) {
        VulkanBuffer vertices = VulkanBuffer.newVertexBuffer(graphicsContext, mesh.getVertexSize());
        VulkanBuffer indices = VulkanBuffer.newIndexBuffer(graphicsContext, mesh.getIndexSize());

        VulkanMemoryAllocation vertexAllocation = memoryManager.allocate(graphicsContext, vertices);
        VulkanMemoryAllocation indexAllocation = memoryManager.allocate(graphicsContext, indices);

        VulkanBuffer writeVertexBuffer = vertexAllocation.bindAndAllocateBuffer(vertices);
        VulkanBuffer writeIndexBuffer = indexAllocation.bindAndAllocateBuffer(indices);

        mesh.writeToBuffers(writeVertexBuffer, writeIndexBuffer);

        CommandBuffer commandBuffer = VkInterface.getCommandBuffer(graphicsContext);

        writeVertexBuffer.copyTo(commandBuffer.getCommandBuffer(), vertices);
        writeIndexBuffer.copyTo(commandBuffer.getCommandBuffer(), indices);

        if(vertexAllocation.requiresCopy()) {
            commandBuffer.add(() -> memoryManager.free(vertexAllocation));
        }
        if(indexAllocation.requiresCopy()) {
            commandBuffer.add(() -> memoryManager.free(indexAllocation));
        }

        VulkanMesh vMesh = new VulkanMesh(vertices, indices);

        long id = vertices.getVkBuffer();

        synchronized (meshes) {
            meshes.put(id, vMesh);
        }

        return id;
    }

    @Override
    public void destroyMesh(VulkanBaseGraphicsContext graphicsContext, long mesh) {
        CommandBuffer commandBuffer = VkInterface.getCommandBuffer(graphicsContext);

        VulkanMesh vMesh;
        synchronized (meshes) {
            vMesh = meshes.remove(mesh);
        }
        commandBuffer.add(vMesh::free);
    }

    @Override
    public long createTexture(VulkanBaseGraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        VulkanTexture vulkanTexture = new VulkanTexture(graphicsContext.getCommandBuffer(), graphicsContext.getDevice(), buffer, width, height, format);
        synchronized (textures) {
            textures.put(vulkanTexture.image, vulkanTexture);
        }
        return vulkanTexture.image;
    }

    @Override
    public void destroyTexture(VulkanBaseGraphicsContext graphicsContext, long texture) {
        synchronized (textures) {
            textures.remove(texture).cleanup();
        }
    }

    @Override
    public void drawAndDestroyMesh(VulkanBaseGraphicsContext graphicsContext, MatrixStack matrixStack, VulkanMeshBuilder mesh) {
        VulkanBuffer vertices = VulkanBuffer.newVertexBuffer(graphicsContext, mesh.getVertexSize());
        VulkanBuffer indices = VulkanBuffer.newIndexBuffer(graphicsContext, mesh.getIndexSize());

        VulkanMemoryAllocation vertexAllocation = memoryManager.allocate(graphicsContext, vertices);
        VulkanMemoryAllocation indexAllocation = memoryManager.allocate(graphicsContext, indices);

        VulkanBuffer writeVertexBuffer = vertexAllocation.bindAndAllocateBuffer(vertices);
        VulkanBuffer writeIndexBuffer = indexAllocation.bindAndAllocateBuffer(indices);

        mesh.writeToBuffers(writeVertexBuffer, writeIndexBuffer);

        CommandBuffer commandBuffer = VkInterface.getCommandBuffer(graphicsContext);

        writeVertexBuffer.copyTo(commandBuffer.getCommandBuffer(), vertices);
        writeIndexBuffer.copyTo(commandBuffer.getCommandBuffer(), indices);

        if(vertexAllocation.requiresCopy()) {
            commandBuffer.add(writeVertexBuffer::free);
        }
        if(indexAllocation.requiresCopy()) {
            commandBuffer.add(writeIndexBuffer::free);
        }
        commandBuffer.add(vertices::free);
        commandBuffer.add(indices::free);

        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            vkCmdBindVertexBuffers(graphicsContext.getBuffer(), 0, memoryStack.longs(vertices.getVkBuffer()), memoryStack.longs(0));
            vkCmdBindIndexBuffer(graphicsContext.getBuffer(), indices.getVkBuffer(), 0, VK_INDEX_TYPE_UINT32);

            vkCmdDrawIndexed(graphicsContext.getBuffer(), mesh.getIndexCount(), 1, (int) 0, 0, 0);
        }
    }

    @Override
    public void bindTexture(VulkanBaseGraphicsContext gc, long texture) {
        VulkanGraphicsContext graphicsContext = (VulkanGraphicsContext)gc;
        if (texture != graphicsContext.texture) {

        }
    }

    @Override
    public void bindPipeline(VulkanBaseGraphicsContext gc, long pipeline) {
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
    public void setState(VulkanBaseGraphicsContext graphicsContext, PipelineState state) {
        if(graphicsContext.pipelineStateSet) {
            throw new VulkanEngineException("Graphics state was already set by the render task and cannot be reset inside the render task");
        }
    }

    @Override
    public long createProgram(VulkanBaseGraphicsContext graphicsContext, ShaderSource shaderSource) {
        LogicalDevice device = graphicsContext.getDevice();

        GraphicsPipeline graphicsPipeline = new GraphicsPipeline(device, shaderSource);

        if(asyncShaderLoading) {
            submitShader(shaderSource.vertexShader, shaderSource.owner.getModID(), device, VK_SHADER_STAGE_VERTEX_BIT);
            submitShader(shaderSource.fragmentShader, shaderSource.owner.getModID(), device, VK_SHADER_STAGE_FRAGMENT_BIT);
        } else {
            compileShaderAndPut(shaderSource.vertexShader, shaderSource.owner.getModID(), device, VK_SHADER_STAGE_VERTEX_BIT);
            compileShaderAndPut(shaderSource.fragmentShader, shaderSource.owner.getModID(), device, VK_SHADER_STAGE_FRAGMENT_BIT);

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
    public void destroyProgram(VulkanBaseGraphicsContext graphicsContext, long program) {

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

    @Override
    public long createFrameBuffer(VulkanBaseGraphicsContext graphicsContext, int width, int height) {
        return 0;
    }

    @Override
    public void destroyFrameBuffer(VulkanBaseGraphicsContext graphicsContext, long id) {

    }

    @Override
    public void bindFrameBuffer(VulkanBaseGraphicsContext graphicsContext, long id) {

    }

    @Override
    public long getBoundFBO(VulkanBaseGraphicsContext graphicsContext) {
        throw new RuntimeException("");
        //return 0;
    }

    @Override
    public long getBoundTexture(VulkanBaseGraphicsContext gc) {
        VulkanGraphicsContext graphicsContext = (VulkanGraphicsContext)gc;
        return graphicsContext.texture;
    }

    @Override
    public long getBoundProgram(VulkanBaseGraphicsContext gc) {
        VulkanGraphicsContext graphicsContext = (VulkanGraphicsContext)gc;
        return graphicsContext.program;
    }

    @Override
    public void clearFBO(VulkanBaseGraphicsContext graphicsContext, Vector4f clearColor) {

    }

    @Override
    public void setScissor(VulkanBaseGraphicsContext graphicsContext, int x, int y, int width, int height) {
        //vkCmdSetScissor();
    }

    @Override
    public VulkanMeshBuilder getMeshBuilder(String vertexFormat) {
        return null;
    }

    @Override
    public VulkanMeshBuilder getMeshBuilder(VertexFormat vertexFormat) {
        return null;
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
        try {
            String shaderSource = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(path, modID));
            if(shaderSource == null) {
                System.err.println("Failed to load vulkan shader " + path);
                return;
            }

            Shader shader = new Shader(device, ShaderCompiler.compileShader(shaderSource, path, bit), bit);
            synchronized (shaderCache) {
                if(running.get()) {
                    System.out.println("Putting " + path);
                    shaderCache.put(path, shader);
                } else {
                    shader.free();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to compile shader " + path);
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

    public GameInstance getGameInstance() {
        return engine.getGameInstance();
    }
}
