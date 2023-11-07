package dev.hilligans.ourcraft.client.rendering.graphics.api;

import dev.hilligans.ourcraft.client.input.InputHandler;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IInputProvider extends IRegistryElement {

    void setWindow(RenderWindow window, InputHandler handler);

    int getSize();

    void setOffset(int offset);

    int getOffset();

    boolean requiresTicking();

    void tick();

    String getButtonName(int button, int extra);
}
