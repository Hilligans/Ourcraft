package dev.hilligans.engine.client.graphics.multigl.command;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public record DrawCommand(GLMesh mesh, int mode, int count, int type, long indices) implements MultiGLCommand {
    @Override
    public void run() {
        glBindVertexArray(mesh.VAO());
        glDrawElements(mode, count, type, indices);
    }
}
