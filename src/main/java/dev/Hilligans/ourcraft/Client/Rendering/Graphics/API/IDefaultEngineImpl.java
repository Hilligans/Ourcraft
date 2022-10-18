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

    void drawMesh(T window, Q graphicsContext, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length);

    int createMesh(T window, Q graphicsContext, VertexMesh mesh);

    void destroyMesh(T window, Q graphicsContext, int mesh);

    default int createTexture(T window, Q graphicsContext, Image image) {
        return createTexture(window, graphicsContext, image.getBuffer(), image.getWidth(), image.getHeight(), image.format);
    }

    int createTexture(T window, Q graphicsContext, ByteBuffer buffer, int width, int height, int format);

    void destroyTexture(T window, Q graphicsContext, int texture);

    void drawAndDestroyMesh(T window, Q graphicsContext, MatrixStack matrixStack, VertexMesh mesh, int texture, int program);

    void setState(T window, Q graphicsContext, PipelineState state);

    int createProgram(Q graphicsContext, ShaderSource shaderSource);

    void uploadData(Q graphicsContext, FloatBuffer data, int index, String type, int program);

    int getUniformIndex(Q graphicsContext, String name, int shader);

    default void drawMesh(Object window, Object graphicsContext, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length) {
        drawMesh((T) window, (Q) graphicsContext, matrixStack, texture, program, meshID, indicesIndex, length);
    }

    default int createMesh(Object window, Object graphicsContext, VertexMesh mesh) {
        return createMesh((T) window, (Q) graphicsContext, mesh);
    }

    default int createTexture(Object window, Object graphicsContext, Image image) {
        return createTexture((T) window, (Q) graphicsContext, image);
    }

    default int createTexture(Object window, Object graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return createTexture((T) window, (Q) graphicsContext, buffer, width, height, format);
    }

    default void destroyTexture(Object window, Object graphicsContext, int texture) {
        destroyTexture((T) window, (Q) graphicsContext, texture);
    }

    default void drawAndDestroyMesh(Object window, Object graphicsContext, MatrixStack matrixStack, VertexMesh mesh, int texture, int program) {
        drawAndDestroyMesh((T) window, (Q) graphicsContext, matrixStack, mesh, texture, program);
    }

    default void setState(Object window, Object graphicsContext, PipelineState state) {
        setState((T) window, (Q) graphicsContext, state);
    }

    default void uploadData(Object graphicsContext, FloatBuffer data, int index, String type, int program) {
        uploadData((Q) graphicsContext, data, index, type, program);
    }

    default void uploadData(Object graphicsContext, float[] data, int index, String type, int program) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData((Q) graphicsContext, memoryStack.floats(data), index, type, program);
        }
    }

    default int getUniformIndex(Object graphicsContext, String name, int shader) {
        return getUniformIndex((Q) graphicsContext, name, shader);
    }

    default void uploadMatrix(GraphicsContext context, MatrixStack matrixStack, ShaderSource shaderSource) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData(context, memoryStack.floats(matrixStack.color.x, matrixStack.color.y, matrixStack.color.z, matrixStack.color.w), shaderSource.uniformIndexes[1], "4f", shaderSource.program);
            uploadData(context, matrixStack.matrix4f.get(memoryStack.mallocFloat(16)), shaderSource.uniformIndexes[0], "4fv", shaderSource.program);
        }
    }
}
