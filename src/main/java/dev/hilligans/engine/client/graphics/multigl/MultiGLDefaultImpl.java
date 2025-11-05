package dev.hilligans.engine.client.graphics.multigl;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.DefaultMeshBuilder;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.TextureFormat;
import dev.hilligans.engine.client.graphics.api.TextureOptions;
import dev.hilligans.engine.client.graphics.multigl.command.BindPipeline;
import dev.hilligans.engine.client.graphics.multigl.command.DrawCommand;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.resource.VertexMesh;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.content.UnknownResourceException;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MultiGLDefaultImpl implements IDefaultEngineImpl<MultiGLWindow, MultiGLContext, DefaultMeshBuilder> {

    public final Int2ObjectOpenHashMap<Tuple<Integer, Integer>> meshData = new Int2ObjectOpenHashMap<>();
    public final Int2LongOpenHashMap vertexArrayObjects = new Int2LongOpenHashMap();

    public MultiGLEngine engine;


    @Override
    public GameInstance getGameInstance() {
        return null;
    }

    @Override
    public void drawMesh(MultiGLContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        Tuple<Integer, Integer> data;
        synchronized (this) {
            data = meshData.get((int)meshID);
        }
        graphicsContext.addCommand(new DrawCommand(null, data.getTypeA(), length, data.getTypeB(), indicesIndex));
    }

    @Override
    public synchronized long createMesh(MultiGLContext graphicsContext, DefaultMeshBuilder builder) {
        VertexMesh mesh = builder.build();
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();

        glBindVertexArray(VAO);


        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, mesh.vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.indices, GL_STATIC_DRAW);

        int x = 0;
        int stride = mesh.vertexFormat.getStride();
        int pointer = 0;
        for(VertexFormat.VertexPart part : mesh.vertexFormat.parts) {
            //glVertexAttribPointer(x, part.primitiveCount, getGLPrimitive(part.primitiveType), part.normalized, stride, pointer);
            glEnableVertexAttribArray(x);
            pointer += part.getSize();
            x++;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        mesh.destroy();

        meshData.put(VAO, new Tuple<>(mesh.vertexFormat.primitiveType, mesh.elementSize == 4 ? GL_UNSIGNED_INT : GL_UNSIGNED_SHORT));
        vertexArrayObjects.put(VAO, ((long)VBO << 32) | (long)EBO);

        return VAO;
    }

    @Override
    public synchronized void destroyMesh(MultiGLContext graphicsContext, long mesh) {

    }

    @Override
    public synchronized long createTexture(MultiGLContext graphicsContext, ByteBuffer buffer, int width, int height, int format, TextureOptions textureOptions) {
        return 0;
    }

    @Override
    public synchronized void destroyTexture(MultiGLContext graphicsContext, long texture) {

    }

    @Override
    public synchronized void drawAndDestroyMesh(MultiGLContext graphicsContext, MatrixStack matrixStack, DefaultMeshBuilder mesh) {

    }

    @Override
    public synchronized void bindTexture(MultiGLContext graphicsContext, long texture) {

    }

    @Override
    public synchronized void bindPipeline(MultiGLContext graphicsContext, long pipeline) {
        if(graphicsContext.boundPipeline != pipeline) {
            graphicsContext.addCommand(new BindPipeline((int) pipeline));
        }
    }

    @Override
    public void setState(MultiGLContext graphicsContext, PipelineState state) {

    }

    @Override
    public long createProgram(MultiGLContext graphicsContext, ShaderSource shaderSource) {
        return 0;
    }

    @Override
    public void destroyProgram(MultiGLContext graphicsContext, long program) {

    }

    @Override
    public void uploadData(MultiGLContext graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {

    }

    @Override
    public long createFrameBuffer(MultiGLContext graphicsContext, int width, int height) {
        return 0;
    }

    @Override
    public void destroyFrameBuffer(MultiGLContext graphicsContext, long id) {

    }

    @Override
    public void bindFrameBuffer(MultiGLContext graphicsContext, long id) {

    }

    @Override
    public long getBoundFBO(MultiGLContext graphicsContext) {
        return 0;
    }

    @Override
    public long getBoundTexture(MultiGLContext graphicsContext) {
        return 0;
    }

    @Override
    public long getBoundProgram(MultiGLContext graphicsContext) {
        return 0;
    }

    @Override
    public void clearFBO(MultiGLContext graphicsContext, Vector4f clearColor) {

    }

    @Override
    public void setScissor(MultiGLContext graphicsContext, int x, int y, int width, int height) {

    }

    @Override
    public DefaultMeshBuilder getMeshBuilder(String vertexFormat) {
        return null;
    }

    @Override
    public DefaultMeshBuilder getMeshBuilder(VertexFormat vertexFormat) {
        return null;
    }

    @Override
    public boolean isTextureFormatSupported(TextureFormat textureFormat) {
        return switch (textureFormat) {
            case RGB, RGBA, DXT1, DXT5 -> true;
            default -> false;
        };
    }

    VertexFormat[] cache = new VertexFormat[2];

    public synchronized VertexFormat getFormat(String name) {
        if(cache[0] != null && cache[0].formatName.equals(name)) {
            return cache[0];
        }
        if(cache[1] != null && cache[1].formatName.equals(name)) {
            VertexFormat f = cache[0];
            cache[0] = cache[1];
            cache[1] = f;
            return cache[0];
        }
        for(VertexFormat vertexFormat : engine.getGameInstance().VERTEX_FORMATS.ELEMENTS) {
            if(vertexFormat.formatName.equals(name)) {
                cache[1] = vertexFormat;
                return vertexFormat;
            }
        }

        throw new UnknownResourceException("Failed to find resource in the registry by name: " + name, engine.getGameInstance().VERTEX_FORMATS, name, null);
    }

    public long createMesh() {
        return 0;
    }
}
