package dev.hilligans.ourcraft.client.rendering.graphics.opengl;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.PipelineState;
import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.graphics.VertexFormat;
import dev.hilligans.ourcraft.client.rendering.newrenderer.Image;
import dev.hilligans.ourcraft.client.rendering.VertexMesh;
import dev.hilligans.ourcraft.client.rendering.world.managers.ShaderManager;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.mod.handler.content.UnknownResourceException;
import dev.hilligans.ourcraft.resource.ResourceLocation;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.*;

public class OpenglDefaultImpl implements IDefaultEngineImpl<OpenGLWindow, GraphicsContext> {

    public OpenGLEngine engine;

    public Int2ObjectOpenHashMap<Tuple<Integer, Integer>> meshData = new Int2ObjectOpenHashMap<>();
    public final Int2IntOpenHashMap textureTypes = new Int2IntOpenHashMap();
    public Int2LongOpenHashMap vertexArrayObjects = new Int2LongOpenHashMap();
    public Int2ObjectOpenHashMap<VertexMesh> meshReferences = new Int2ObjectOpenHashMap<>();
    public final Int2IntOpenHashMap fbos = new Int2IntOpenHashMap();

    public long boundTexture = -1;
    public long boundProgram = -1;
    public long boundFBO = 0;

    public OpenglDefaultImpl(OpenGLEngine engine) {
        this.engine = engine;
    }

    @Override
    public void drawMesh(GraphicsContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        Tuple<Integer, Integer> data = meshData.get((int)meshID);
        if(data == null) {
            return;
        }
        glBindVertexArray((int)meshID);
        GL20.glDrawElements(data.typeA, length, data.typeB, indicesIndex);
    }

    @Override
    public long createMesh(GraphicsContext graphicsContext, VertexMesh mesh) {
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
            glVertexAttribPointer(x, part.primitiveCount, getGLPrimitive(part.primitiveType), part.normalized, stride, pointer);
            glEnableVertexAttribArray(x);
            pointer += part.getSize();
            x++;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        mesh.destroy();

        meshReferences.put(VAO, mesh);
        meshData.put(VAO, new Tuple<>(GL_TRIANGLES, GL_UNSIGNED_INT));
        vertexArrayObjects.put(VAO, ((long)VBO << 32) | (long)EBO);
        return VAO;
    }

    @Override
    public void destroyMesh(GraphicsContext graphicsContext, long mesh) {
        long array = vertexArrayObjects.get((int)mesh);
        meshData.remove((int)mesh);
        if((int)array != 0) {
            glDeleteBuffers((int) array);
        }
        glDeleteBuffers((int)(array >> 32));
        glDeleteBuffers((int)mesh);
    }

    private int getGLPrimitive(int type) {
        return type + 0x1400;
    }

    @Override
    public long createTexture(GraphicsContext graphicsContext, Image image) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        int format = image.format == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, image.width, image.height, 0, format, GL_UNSIGNED_BYTE, image.buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        textureTypes.put(texture, GL_TEXTURE_2D);
        return texture;
    }

    @Override
    public long createTexture(GraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        format = format == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        textureTypes.put(texture, GL_TEXTURE_2D);
        return texture;
    }

    @Override
    public void destroyTexture(GraphicsContext graphicsContext, long texture) {
        glDeleteTextures((int)texture);
        textureTypes.remove((int)texture);
    }

    @Override
    public void drawAndDestroyMesh(GraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh) {
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }
        glDisable(GL_DEPTH_TEST);

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
            glVertexAttribPointer(x,part.primitiveCount, getGLPrimitive(part.primitiveType),part.normalized,stride,pointer);
            glEnableVertexAttribArray(x);
            pointer += part.getSize();
            x++;
        }

        glDrawElements(mesh.vertexFormat.primitiveType,mesh.indices.limit(),GL_UNSIGNED_INT,0);

        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
        glDeleteBuffers(VAO);
    }

    @Override
    public void bindTexture(GraphicsContext graphicsContext, long texture) {
        if(texture != boundTexture) {
            GL20.glBindTexture(textureTypes.get((int)texture), (int)texture);
            boundTexture = texture;
        }
    }

    @Override
    public void bindPipeline(GraphicsContext graphicsContext, long pipeline) {
        if(pipeline != boundProgram){
            GL20.glUseProgram((int) pipeline);
            boundProgram = pipeline;
        }
    }

    @Override
    public void setState(GraphicsContext graphicsContext, PipelineState state) {
        if(graphicsContext.pipelineStateSet) {
            throw new RuntimeException("Graphics state was already set by the render task and cannot be reset inside the render task");
        }
        if(state.depthTest) {
            glEnable(GL_DEPTH_TEST);
        } else {
            glDisable(GL_DEPTH_TEST);
        }
    }

    @Override
    public long createProgram(GraphicsContext graphicsContext, ShaderSource shaderSource) {
        String vertex = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.vertexShader, shaderSource.modContent.getModID()));
        String fragment = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.fragmentShader, shaderSource.modContent.getModID()));
        String geometry = shaderSource.geometryShader == null ? null :  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.geometryShader, shaderSource.modContent.getModID()));

        int shader;
        if(geometry == null) {
            shader = ShaderManager.registerShader(vertex, fragment);
        } else {
            shader = ShaderManager.registerShader(vertex, geometry, fragment);
        }

        if(shaderSource.uniformNames != null) {
            shaderSource.uniformIndexes = new int[shaderSource.uniformNames.size()];
            for(int x = 0; x < shaderSource.uniformNames.size(); x++) {
                shaderSource.uniformIndexes[x] = glGetUniformLocation(shader, shaderSource.uniformNames.get(x));
            }
        }

        return shader;
    }

    @Override
    public void uploadData(GraphicsContext graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {
        //if(program != boundProgram) {
            GL20.glUseProgram((int)program);
            boundProgram = program;
       // }
        if ("4fv".equals(type)) {
            GL33.glUniformMatrix4fv((int) index, false, data);
        } else if("4f".equals(type)) {
            GL33.glUniform4fv((int) index, data);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public long createFrameBuffer(GraphicsContext graphicsContext, int width, int height) {
        int fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
        synchronized (fbos) {
            fbos.put(fbo, texture);
        }
        synchronized (textureTypes) {
            textureTypes.put(texture, GL_TEXTURE_2D);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        return texture;
    }

    @Override
    public void destroyFrameBuffer(GraphicsContext graphicsContext, long id) {
        int fbo;
        synchronized (fbos) {
            fbo = fbos.remove((int) id);
        }
        synchronized (textureTypes) {
            textureTypes.remove((int)fbo);
        }
        glDeleteTextures((int) id);
        glDeleteFramebuffers(fbo);
    }

    @Override
    public void bindFrameBuffer(GraphicsContext graphicsContext, long id) {
        this.boundFBO = id;
        if(id == 0) {
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        } else {
            glBindFramebuffer(GL_FRAMEBUFFER, fbos.get((int)id));
        }
    }

    @Override
    public long getBoundFBO(GraphicsContext graphicsContext) {
        return boundFBO;
    }

    @Override
    public long getBoundTexture(GraphicsContext graphicsContext) {
        return boundTexture;
    }

    @Override
    public long getBoundProgram(GraphicsContext graphicsContext) {
        return boundProgram;
    }

    @Override
    public void clearFBO(GraphicsContext graphicsContext, Vector4f clearColor) {
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void close() {
        for(int texture : textureTypes.keySet()) {
            glDeleteTextures(texture);
        }
        textureTypes.clear();
        /*
        long array = vertexArrayObjects.get(mesh);
        meshData.remove(mesh);
        if((int)array != 0) {
            glDeleteBuffers((int) array);
        }
        glDeleteBuffers((int)(array >> 32));
        glDeleteBuffers(mesh);

         */
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
        for(VertexFormat vertexFormat : engine.client.gameInstance.VERTEX_FORMATS.ELEMENTS) {
            if(vertexFormat.formatName.equals(name)) {
                cache[1] = vertexFormat;
                return vertexFormat;
            }
        }

        throw new UnknownResourceException("Failed to find resource in the registry by name: " + name, engine.client.gameInstance.VERTEX_FORMATS, name, engine.getGameInstance().OURCRAFT);
    }
}
