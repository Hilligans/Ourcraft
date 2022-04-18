package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;

public interface IInputProvider {

    void setWindow(RenderWindow window, InputHandler handler);

    int getSize();

    void setOffset(int offset);

    int getOffset();

    boolean requiresTicking();

    void tick();

    String getButtonName(int button, int extra);
}
