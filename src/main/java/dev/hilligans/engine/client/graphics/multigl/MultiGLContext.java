package dev.hilligans.engine.client.graphics.multigl;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.multigl.command.CommandBuffer;
import dev.hilligans.engine.client.graphics.multigl.command.MultiGLCommand;

public class MultiGLContext extends GraphicsContext {

    public CommandBuffer commandBuffer;
    public int boundPipeline = -1;

    public MultiGLContext(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    public CommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public void addCommand(MultiGLCommand command) {
        this.getCommandBuffer().enqueueCommand(command);
    }
}
