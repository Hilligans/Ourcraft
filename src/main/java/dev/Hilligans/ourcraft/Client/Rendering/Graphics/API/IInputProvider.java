package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;

public interface IInputProvider {

    void setWindow(long window, InputHandler handler);

    int getSize();

    void setOffset(int offset);

    int getOffset();

    boolean requiresTicking();

    void tick();

    String getButtonName(int button, int extra);
}
