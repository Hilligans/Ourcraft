package dev.Hilligans.ourcraft.Client.Input.Handlers;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

public class MouseHandler implements IInputProvider {

    public int offset;

    @Override
    public void setWindow(long window, InputHandler handler) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public boolean requiresTicking() {
        return false;
    }

    @Override
    public void tick() {

    }

    @Override
    public String getButtonName(int button, int extra) {
        return null;
    }
}
