package dev.hilligans.engine.client.graphics.multigl.command;

import dev.hilligans.engine.client.graphics.multigl.MultiGLWindow;

import java.util.ArrayList;

public class CommandBuffer {

    public MultiGLWindow window;
    public ArrayList<MultiGLCommand> commands = new ArrayList<>();

    public void enqueueCommand(MultiGLCommand command) {
        this.commands.add(command);
    }
}
