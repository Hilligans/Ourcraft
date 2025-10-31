package dev.hilligans.engine.client.graphics.multigl.command;

import org.lwjgl.opengl.GL45;

public record BindPipeline(int pipeline) implements MultiGLCommand {
    @Override
    public void run() {
        GL45.glUseProgram(pipeline);
    }
}
