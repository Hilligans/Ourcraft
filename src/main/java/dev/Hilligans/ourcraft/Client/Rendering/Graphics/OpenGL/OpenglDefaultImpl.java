package dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.ModHandler.Content.UnknownResourceException;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.*;

public class OpenglDefaultImpl implements IDefaultEngineImpl<OpenGLWindow, GraphicsContext> {

    public OpenGLEngine engine;

    public Int2ObjectOpenHashMap<Tuple<Integer, Integer>> meshData = new Int2ObjectOpenHashMap<>();
    public Int2IntOpenHashMap textureTypes = new Int2IntOpenHashMap();
    public Int2LongOpenHashMap vertexArrayObjects = new Int2LongOpenHashMap();
    public Int2ObjectOpenHashMap<VertexMesh> meshReferences = new Int2ObjectOpenHashMap<>();

    public int boundTexture = -1;
    public int boundProgram = -1;

    public OpenglDefaultImpl(OpenGLEngine engine) {
        this.engine = engine;
    }

    @Override
    public void drawMesh(OpenGLWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length) {
        Tuple<Integer, Integer> data = meshData.get(meshID);
        if(texture != boundTexture) {
            GL20.glBindTexture(textureTypes.get(texture), texture);
            boundTexture = texture;
        }
      //  if(program != boundProgram){
            GL20.glUseProgram(program);
            boundProgram = program;
      //  }

        if(data == null) {
            return;
        }
        glBindVertexArray(meshID);
        GL20.glDrawElements(data.typeA, length, data.typeB, indicesIndex);
    }

    @Override
    public int createMesh(OpenGLWindow window, GraphicsContext graphicsContext, VertexMesh mesh) {
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
            glVertexAttribPointer(x,part.primitiveCount, getGLPrimitive(part.primitiveType),part.normalized,stride,pointer);
            glEnableVertexAttribArray(x);
            pointer += part.getSize();
            x++;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        MemoryUtil.memFree(mesh.vertices);
        MemoryUtil.memFree(mesh.indices);

        meshReferences.put(VAO, mesh);
        meshData.put(VAO, new Tuple<>(GL_TRIANGLES, GL_UNSIGNED_INT));
        vertexArrayObjects.put(VAO, ((long)VBO << 32) | (long)EBO);
        return VAO;
    }

    @Override
    public void destroyMesh(OpenGLWindow window, GraphicsContext graphicsContext, int mesh) {
        long array = vertexArrayObjects.get(mesh);
        meshData.remove(mesh);
        if((int)array != 0) {
            glDeleteBuffers((int) array);
        }
        glDeleteBuffers((int)(array >> 32));
        glDeleteBuffers(mesh);
    }

    private int getGLPrimitive(int type) {
        return type + 0x1400;
    }

    @Override
    public int createTexture(OpenGLWindow window, GraphicsContext graphicsContext, Image image) {
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
    public int createTexture(OpenGLWindow window, GraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
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
    public void destroyTexture(OpenGLWindow window, GraphicsContext graphicsContext, int texture) {
        glDeleteTextures(texture);
        textureTypes.remove(texture);
    }

    @Override
    public void drawAndDestroyMesh(OpenGLWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh, int texture, int program) {
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }
        glDisable(GL_DEPTH_TEST);
        if(texture != boundTexture) {
            // GL20.glBindTexture(textureTypes.get(texture), texture);
            if (texture != 0) {
                GL20.glBindTexture(GL_TEXTURE_2D, texture);
                boundTexture = texture;
            }
        }
      //  if(program != boundProgram){
            GL20.glUseProgram(program);
            boundProgram = program;
      //  }
      //  matrixStack.applyTransformation(program);
        //try(MemoryStack memoryStack = MemoryStack.stackPush()) {
          //  uploadData(graphicsContext, memoryStack.floats(matrixStack.color.x, matrixStack.color.y, matrixStack.color.z, matrixStack.color.w), "4f");
          //  uploadData(graphicsContext, matrixStack.get().get(memoryStack.mallocFloat(16)));
        //}
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
    public void setState(OpenGLWindow window, GraphicsContext graphicsContext, PipelineState state) {
        if(state.depthTest) {
            glEnable(GL_DEPTH_TEST);
        } else {
            glDisable(GL_DEPTH_TEST);
        }
    }

    @Override
    public int createProgram(GraphicsContext graphicsContext, ShaderSource shaderSource) {
        String vertex =  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.vertexShader, shaderSource.modContent.getModID()));
        String fragment = engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.fragmentShader, shaderSource.modContent.getModID()));
        String geometry = shaderSource.geometryShader == null ? null :  engine.getGameInstance().RESOURCE_LOADER.getString(new ResourceLocation(shaderSource.geometryShader, shaderSource.modContent.getModID()));

        if(geometry == null) {
            int val = ShaderManager.registerShader(vertex,fragment);
            System.out.println("Shader Error:" + glGetError());
            return val;
        } else {
            return ShaderManager.registerShader(vertex,geometry,fragment);
        }
    }

    @Override
    public void uploadData(GraphicsContext graphicsContext, FloatBuffer data, int index, String type, int program) {
        //if(program != boundProgram) {
            GL20.glUseProgram(program);
            boundProgram = program;
       // }
        if ("4fv".equals(type)) {
            GL33.glUniformMatrix4fv(index, false, data);
        } else if("4f".equals(type)) {
            GL33.glUniform4fv(index, data);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int getUniformIndex(GraphicsContext graphicsContext, String name, int shader) {
        return glGetUniformLocation(shader, name);
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
