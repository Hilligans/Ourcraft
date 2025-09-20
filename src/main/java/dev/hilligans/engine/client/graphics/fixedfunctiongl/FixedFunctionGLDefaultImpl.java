package dev.hilligans.engine.client.graphics.fixedfunctiongl;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.resource.VertexMesh;
import dev.hilligans.engine.client.graphics.DefaultMeshBuilder;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.data.Tuple;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

public class FixedFunctionGLDefaultImpl implements IDefaultEngineImpl<FixedFunctionGLWindow, GraphicsContext, DefaultMeshBuilder> {

    public FixedFunctionGLEngine engine;

    public Int2ObjectOpenHashMap<Tuple<Integer, Integer>> meshData = new Int2ObjectOpenHashMap<>();
    public Int2ObjectOpenHashMap<VertexMesh> meshes = new Int2ObjectOpenHashMap<>();
    public Int2IntOpenHashMap textureTypes = new Int2IntOpenHashMap();
    public Int2LongOpenHashMap vertexArrayObjects = new Int2LongOpenHashMap();

    public long boundTexture = -1;

    public int meshPointer = 0;

    @Override
    public void drawMesh(GraphicsContext graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
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
    public long createMesh(GraphicsContext graphicsContext, DefaultMeshBuilder builder) {
        VertexMesh mesh = builder.build();
        if(mesh.vertexFormat == null) {
            mesh.vertexFormat = getFormat(mesh.vertexFormatName);
        }
        int ptr = meshPointer++;
        meshes.put(ptr,mesh);
        return ptr;
    }

    @Override
    public void destroyMesh(GraphicsContext graphicsContext, long mesh) {
        meshes.remove((int)mesh);
    }

    @Override
    public long createTexture(GraphicsContext graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return 0;
    }

    @Override
    public void destroyTexture(GraphicsContext graphicsContext, long texture) {

    }

    @Override
    public void bindTexture(GraphicsContext graphicsContext, long texture) {

    }

    @Override
    public void bindPipeline(GraphicsContext graphicsContext, long pipeline) {

    }

    @Override
    public void drawAndDestroyMesh(GraphicsContext graphicsContext, MatrixStack matrixStack, DefaultMeshBuilder builder) {
        VertexMesh mesh = builder.build();
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
    public void setState(GraphicsContext graphicsContext, PipelineState state) {

    }

    @Override
    public long createProgram(GraphicsContext graphicsContext, ShaderSource shaderSource) {
        return 0;
    }

    @Override
    public void destroyProgram(GraphicsContext graphicsContext, long program) {

    }

    @Override
    public void uploadData(GraphicsContext graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {

    }

    @Override
    public long createFrameBuffer(GraphicsContext graphicsContext, int width, int height) {
        return 0;
    }

    @Override
    public void destroyFrameBuffer(GraphicsContext graphicsContext, long id) {

    }

    @Override
    public void bindFrameBuffer(GraphicsContext graphicsContext, long id) {

    }

    @Override
    public long getBoundFBO(GraphicsContext graphicsContext) {
        return 0;
    }

    @Override
    public long getBoundTexture(GraphicsContext graphicsContext) {
        return 0;
    }

    @Override
    public long getBoundProgram(GraphicsContext graphicsContext) {
        return 0;
    }

    @Override
    public void clearFBO(GraphicsContext graphicsContext, Vector4f clearColor) {

    }

    @Override
    public void setScissor(GraphicsContext graphicsContext, int x, int y, int width, int height) {

    }

    @Override
    public DefaultMeshBuilder getMeshBuilder(String vertexFormat) {
        return null;
    }

    @Override
    public DefaultMeshBuilder getMeshBuilder(VertexFormat vertexFormat) {
        return null;
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
        /*
        for(VertexFormat vertexFormat : engine.client.gameInstance.VERTEX_FORMATS.ELEMENTS) {
            if(vertexFormat.formatName.equals(name)) {
                cache[1] = vertexFormat;
                return vertexFormat;
            }
        }

        throw new UnknownResourceException("Failed to find resource in the registry by name: " + name, engine.client.gameInstance.VERTEX_FORMATS, name, engine.getGameInstance().OURCRAFT);
        */
        return null;
    }
}
