package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface IDefaultEngineImpl<T extends RenderWindow, Q extends GraphicsContext> {

    default void close() {
    }

    void drawMesh(T window, Q graphicsContext, MatrixStack matrixStack, long texture, long program, long meshID, long indicesIndex, int length);

    long createMesh(T window, Q graphicsContext, VertexMesh mesh);

    void destroyMesh(T window, Q graphicsContext, long mesh);

    default long createTexture(T window, Q graphicsContext, Image image) {
        return createTexture(window, graphicsContext, image.getBuffer(), image.getWidth(), image.getHeight(), image.format);
    }

    long createTexture(T window, Q graphicsContext, ByteBuffer buffer, int width, int height, int format);

    void destroyTexture(T window, Q graphicsContext, long texture);

    void drawAndDestroyMesh(T window, Q graphicsContext, MatrixStack matrixStack, VertexMesh mesh, long texture, long program);

    void setState(T window, Q graphicsContext, PipelineState state);

    long createProgram(Q graphicsContext, ShaderSource shaderSource);

    void uploadData(Q graphicsContext, FloatBuffer data, long index, String type, long program);

    long getUniformIndex(Q graphicsContext, String name, long shader);

    default void drawMesh(Object window, Object graphicsContext, MatrixStack matrixStack, long texture, long program, long meshID, long indicesIndex, int length) {
        drawMesh((T) window, (Q) graphicsContext, matrixStack, texture, program, meshID, indicesIndex, length);
    }

    default long createMesh(Object window, Object graphicsContext, VertexMesh mesh) {
        return createMesh((T) window, (Q) graphicsContext, mesh);
    }

    default long createTexture(Object window, Object graphicsContext, Image image) {
        return createTexture((T) window, (Q) graphicsContext, image);
    }

    default long createTexture(Object window, Object graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return createTexture((T) window, (Q) graphicsContext, buffer, width, height, format);
    }

    default void destroyTexture(Object window, Object graphicsContext, long texture) {
        destroyTexture((T) window, (Q) graphicsContext, texture);
    }

    default void drawAndDestroyMesh(Object window, Object graphicsContext, MatrixStack matrixStack, VertexMesh mesh, long texture, long program) {
        drawAndDestroyMesh((T) window, (Q) graphicsContext, matrixStack, mesh, texture, program);
    }

    default void setState(Object window, Object graphicsContext, PipelineState state) {
        setState((T) window, (Q) graphicsContext, state);
    }

    default void uploadData(Object graphicsContext, FloatBuffer data, long index, String type, long program) {
        uploadData((Q) graphicsContext, data, index, type, program);
    }

    default void uploadData(Object graphicsContext, float[] data, long index, String type, long program) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData((Q) graphicsContext, memoryStack.floats(data), index, type, program);
        }
    }

    default long getUniformIndex(Object graphicsContext, String name, long shader) {
        return getUniformIndex((Q) graphicsContext, name, shader);
    }

    default void uploadMatrix(GraphicsContext context, MatrixStack matrixStack, ShaderSource shaderSource) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData(context, memoryStack.floats(matrixStack.color.x, matrixStack.color.y, matrixStack.color.z, matrixStack.color.w), shaderSource.uniformIndexes[1], "4f", shaderSource.program);
            uploadData(context, matrixStack.matrix4f.get(memoryStack.mallocFloat(16)), shaderSource.uniformIndexes[0], "4fv", shaderSource.program);
        }
    }
}
