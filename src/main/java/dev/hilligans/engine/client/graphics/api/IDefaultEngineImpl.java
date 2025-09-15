package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.resource.Image;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface IDefaultEngineImpl<T extends RenderWindow, Q extends GraphicsContext, V extends IMeshBuilder> {

    default void close() {
    }

    default void cleanup() {}

    void drawMesh(Q graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length);

    long createMesh(Q graphicsContext, V mesh);

    void destroyMesh(Q graphicsContext, long mesh);

    default long createTexture(Q graphicsContext, Image image) {
        return createTexture(graphicsContext, image.getBuffer(), image.getWidth(), image.getHeight(), image.format);
    }

    long createTexture(Q graphicsContext, ByteBuffer buffer, int width, int height, int format);

    void destroyTexture(Q graphicsContext, long texture);

    void drawAndDestroyMesh(Q graphicsContext, MatrixStack matrixStack, V mesh);

    void bindTexture(Q graphicsContext, long texture);

    void bindPipeline(Q graphicsContext, long pipeline);

    void setState(Q graphicsContext, PipelineState state);

    long createProgram(Q graphicsContext, ShaderSource shaderSource);

    void destroyProgram(Q graphicsContext, long program);

    void uploadData(Q graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource);

    long createFrameBuffer(Q graphicsContext, int width, int height);

    void destroyFrameBuffer(Q graphicsContext, long id);

    void bindFrameBuffer(Q graphicsContext, long id);

    long getBoundFBO(Q graphicsContext);

    long getBoundTexture(Q graphicsContext);

    long getBoundProgram(Q graphicsContext);

    void clearFBO(Q graphicsContext, Vector4f clearColor);

    void setScissor(Q graphicsContext, int x, int y, int width, int height);

    V getMeshBuilder(String vertexFormat);

    V getMeshBuilder(VertexFormat vertexFormat);

    default void drawMesh(Object graphicsContext, MatrixStack matrixStack, long meshID, long indicesIndex, int length) {
        drawMesh((Q) graphicsContext, matrixStack, meshID, indicesIndex, length);
    }

    default long createMesh(Object graphicsContext, IMeshBuilder mesh) {
        return createMesh((Q) graphicsContext, (V)mesh);
    }

    default void destroyMesh(Object graphicsContext, long id) {
        destroyMesh((Q)graphicsContext, id);
    }

    default long createTexture(Object graphicsContext, Image image) {
        return createTexture((Q) graphicsContext, image);
    }

    default long createTexture(Object graphicsContext, ByteBuffer buffer, int width, int height, int format) {
        return createTexture((Q) graphicsContext, buffer, width, height, format);
    }

    default void destroyTexture(Object graphicsContext, long texture) {
        destroyTexture((Q) graphicsContext, texture);
    }

    default void drawAndDestroyMesh(Object graphicsContext, MatrixStack matrixStack, IMeshBuilder mesh) {
        drawAndDestroyMesh((Q) graphicsContext, matrixStack, (V)mesh);
    }

    default void bindTexture(Object graphicsContext, long texture) {
        bindTexture((Q) graphicsContext, texture);
    }

    default void bindPipeline(Object graphicsContext, long pipeline) {
        bindPipeline((Q) graphicsContext, pipeline);
    }

    default void setState(Object graphicsContext, PipelineState state) {
        setState((Q) graphicsContext, state);
    }

    default long createProgram(Object graphicsContext, ShaderSource shaderSource) {
        return createProgram((Q) graphicsContext, shaderSource);
    }

    default void destroyProgram(Object graphicsContext, long program) {
        destroyProgram((Q)graphicsContext, program);
    }

    default void uploadData(Object graphicsContext, FloatBuffer data, long index, String type, long program, ShaderSource shaderSource) {
        uploadData((Q) graphicsContext, data, index, type, program, shaderSource);
    }

    default void uploadData(Object graphicsContext, float[] data, long index, String type, long program, ShaderSource shaderSource) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData((Q) graphicsContext, memoryStack.floats(data), index, type, program, shaderSource);
        }
    }

    default void uploadMatrix(GraphicsContext context, MatrixStack matrixStack, @NotNull ShaderSource shaderSource) {
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            uploadData(context, memoryStack.floats(matrixStack.color.x, matrixStack.color.y, matrixStack.color.z, matrixStack.color.w), shaderSource.uniformIndexes[1], "4f", shaderSource.program, shaderSource);
            uploadData(context, matrixStack.matrix4f.get(memoryStack.mallocFloat(16)), shaderSource.uniformIndexes[0], "4fv", shaderSource.program, shaderSource);
        }
    }

    default long createFrameBuffer(Object graphicsContext, int width, int height) {
        return createFrameBuffer((Q)graphicsContext, width, height);
    }

    default void destroyFrameBuffer(Object graphicsContext, long id) {
        destroyFrameBuffer((Q)graphicsContext, id);
    }

    default void bindFrameBuffer(Object graphicsContext, long id) {
        bindFrameBuffer((Q)graphicsContext, id);
    }

    default long getBoundFBO(Object graphicsContext) {
        return getBoundFBO((Q)graphicsContext);
    }

    default long getBoundTexture(Object graphicsContext) {
        return getBoundTexture((Q)graphicsContext);
    }

    default long getBoundProgram(Object graphicsContext) {
        return getBoundProgram((Q)graphicsContext);
    }

    default void clearFBO(Object graphicsContext, Vector4f clearColor) {
        clearFBO((Q)graphicsContext, clearColor);
    }

    default void setScissor(Object graphicsContext, int x, int y, int width, int height) {
        setScissor((Q)graphicsContext, x, y, width, height);
    }

    default void defaultScissor(Object graphicsContext, RenderWindow renderWindow) {
        setScissor(graphicsContext, 0, 0, renderWindow.getWindowWidth(), renderWindow.getWindowHeight());
    }
}
