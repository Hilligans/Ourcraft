package dev.hilligans.engine.client.input.handlers;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.ourcraft.Ourcraft;

public class SDLHandler implements IInputProvider {
    @Override
    public void setWindow(RenderWindow window, InputHandler handler) {

    }

    @Override
    public int getSize() {
        return 1000;
    }

    @Override
    public void setOffset(int offset) {

    }

    @Override
    public int getOffset() {
        return 0;
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
        return "";
    }

    @Override
    public String getResourceName() {
        return "mouse_handler";
    }

    @Override
    public String getResourceOwner() {
        return Engine.ENGINE_NAME;
    }
}
