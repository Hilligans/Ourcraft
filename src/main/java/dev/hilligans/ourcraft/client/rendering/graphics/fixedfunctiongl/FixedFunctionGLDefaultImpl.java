package dev.hilligans.ourcraft.client.rendering.graphics.fixedfunctiongl;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.PipelineState;
import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.graphics.VertexFormat;
import dev.hilligans.ourcraft.client.rendering.VertexMesh;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.mod.handler.content.UnknownResourceException;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class FixedFunctionGLDefaultImpl implements IDefaultEngineImpl<FixedFunctionGLWindow, GraphicsContext> {

    public FixedFunctionGLEngine engine;

    public Int2ObjectOpenHashMap<Tuple<Integer, Integer>> meshData = new Int2ObjectOpenHashMap<>();
    public Int2ObjectOpenHashMap<VertexMesh> meshes = new Int2ObjectOpenHashMap<>();
    public Int2IntOpenHashMap textureTypes = new Int2IntOpenHashMap();
    public Int2LongOpenHashMap vertexArrayObjects = new Int2LongOpenHashMap();

    public long boundTexture = -1;

    public int meshPointer = 0;

    @Override
    public void drawMesh(FixedFunctionGLWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        VertexMesh mesh = meshes.get((int)meshID);
        VertexFormat format = mesh.vertexFormat;
        glBegin(format.primitiveType);
        int pos = format.getOffset("position");
        int colour = format.getOffset("color");
        int texturePos = format.getOffset("texture");
        if(pos == -1 || texturePos == -1) {
            throw new RuntimeException("Vertex Format Missing Pieces: " + format.formatName);
        }
        int stride = mesh.vertexFormat.getStride() / 4;

        Vector4f vec = new Vector4f();
     /*  if(colour != -1) {
            for (int val : mesh.indices) {
                int pointer = val * stride;
                glTexCoord2f(mesh.vertices[texturePos + pointer], mesh.vertices[texturePos + pointer + 1]);
                glColor4f(mesh.vertices[colour + pointer],mesh.vertices[colour + pointer + 1],mesh.vertices[colour + pointer + 2],mesh.vertices[colour + pointer + 3]);
                vec.set(mesh.vertices[pos + pointer], mesh.vertices[pos + pointer + 1], mesh.vertices[pos + pointer + 2],0).mul(matrixStack.matrix4f);
                glVertex3f(vec.x - 1,vec.y + 1,vec.z);
            }
        } else {
            for (int val : mesh.indices) {
                int pointer = val * stride;
                glTexCoord2f(mesh.vertices[texturePos + pointer], mesh.vertices[texturePos + pointer + 1]);
                vec.set(mesh.vertices[pos + pointer], mesh.vertices[pos + pointer + 1], mesh.vertices[pos + pointer + 2],0).mul(matrixStack.matrix4f);
                glVertex3f(vec.x - 1,vec.y + 1,vec.z);
            }
        }

      */

        glEnd();
    }

    @Override
    public long createMesh(FixedFunctionGLWindow window, GraphicsContext graphicsContext, VertexMesh mesh) {
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }
        int ptr = meshPointer++;
        meshes.put(ptr,mesh);
        return ptr;
    }

    @Override
    public void destroyMesh(FixedFunctionGLWindow window, GraphicsContext graphicsContext, long mesh) {
        meshes.remove((int)mesh);
    }

    @Override
    public long createTexture(FixedFunctionGLWindow window, GraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return 0;
    }

    @Override
    public void destroyTexture(FixedFunctionGLWindow window, GraphicsContext graphicsContext, long texture) {

    }

    @Override
    public void bindTexture(FixedFunctionGLWindow window, GraphicsContext graphicsContext, long texture) {

    }

    @Override
    public void bindPipeline(FixedFunctionGLWindow window, GraphicsContext graphicsContext, long pipeline) {

    }

    @Override
    public void drawAndDestroyMesh(FixedFunctionGLWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, VertexMesh mesh) {
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }
        Matrix4f matrix4f = matrixStack.get();
        // if(texture != boundTexture) {
        //glBindTexture(textureTypes.get(texture), texture);
        //glBindTexture(GL_TEXTURE_2D, (int)texture);
        //boundTexture = texture;
        //  }
        glBegin(mesh.vertexFormat.primitiveType);
        int pos = mesh.vertexFormat.getOffset("position");
        int colour = mesh.vertexFormat.getOffset("color");
        int texturePos = mesh.vertexFormat.getOffset("texture");
        if(pos == -1 || texturePos == -1) {
            throw new RuntimeException("Vertex Format Missing Pieces: " + mesh.vertexFormat.formatName);
        }
        int stride = mesh.vertexFormat.getStride() / 4;
/*
        if(colour != -1) {
            for (int val : mesh.indices) {
                int pointer = val * stride;
                glTexCoord2f(mesh.vertices[texturePos + pointer], mesh.vertices[texturePos + pointer + 1]);
                glColor4f(mesh.vertices[colour + pointer],mesh.vertices[colour + pointer + 1],mesh.vertices[colour + pointer + 2],mesh.vertices[colour + pointer + 3]);
                Vector4f vec = new Vector4f(mesh.vertices[pos + pointer], mesh.vertices[pos + pointer + 1], mesh.vertices[pos + pointer + 2],0).mul(matrix4f);
                glVertex3f(vec.x - 1,vec.y + 1,vec.z);
            }
        } else {
            for (int val : mesh.indices) {
                int pointer = val * stride;
                glTexCoord2f(mesh.vertices[texturePos + pointer], mesh.vertices[texturePos + pointer + 1]);
                Vector4f vec = new Vector4f(mesh.vertices[pos + pointer], mesh.vertices[pos + pointer + 1], mesh.vertices[pos + pointer + 2],0).mul(matrix4f);
                glVertex3f(vec.x - 1,vec.y + 1,vec.z);
            }
        }

 */

        glEnd();
    }

    @Override
    public void setState(FixedFunctionGLWindow window, GraphicsContext graphicsContext, PipelineState state) {

    }

    @Override
    public long createProgram(GraphicsContext graphicsContext, ShaderSource shaderSource) {
        return 0;
    }

    @Override
    public void uploadData(GraphicsContext graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {

    }

    private int getGLPrimitive(int type) {
        return type + 0x1400;
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
