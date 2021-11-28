package dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.ChunkGraphicsContainer;

public class OpenGLGraphicsContainer extends ChunkGraphicsContainer {

    int vao = -1;
    int sizeVal = -1;

    int[] subChunkVAO;
    int[] subChunkVertexCount;

    public OpenGLGraphicsContainer() {
        subChunkVAO = new int[16];
        subChunkVertexCount = new int[16];
    }
}
