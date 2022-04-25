package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;

import java.nio.ByteBuffer;

public interface IDefaultEngineImpl<T extends RenderWindow> {

    void drawMesh(T window, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length);

    int createMesh(T window, VertexMesh mesh);

    void destroyMesh(T window, int mesh);

    default int createTexture(T window, Image image) {
        return createTexture(window, image.getBuffer(), image.getWidth(), image.getHeight(), image.format);
    }

    int createTexture(T window, ByteBuffer buffer, int width, int height, int format);

    void destroyTexture(T window, int texture);

    void drawAndDestroyMesh(T window, MatrixStack matrixStack, VertexMesh mesh, int texture, int program);

    void setState(T window, PipelineState state);

    int createProgram(ShaderSource shaderSource);

    default void drawMesh(Object window, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length) {
        drawMesh((T)window, matrixStack, texture, program, meshID, indicesIndex, length);
    }

    default int createMesh(Object window, VertexMesh mesh) {
        return createMesh((T)window, mesh);
    }

    default int createTexture(Object window, Image image) {
        return createTexture((T)window, image);
    }

    default int createTexture(Object window, ByteBuffer buffer, int width, int height, int format) {
        return createTexture((T)window,buffer,width,height,format);
    }

    default void destroyTexture(Object window, int texture) {
        destroyTexture((T)window, texture);
    }

    default void drawAndDestroyMesh(Object window, MatrixStack matrixStack, VertexMesh mesh, int texture, int program) {
        drawAndDestroyMesh((T)window, matrixStack, mesh, texture, program);
    }

    default void setState(Object window, PipelineState state) {
        setState((T)window,state);
    }
}
