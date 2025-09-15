package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.client.input.InputHandler;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.util.registry.IRegistryElement;

public interface IInputProvider extends IRegistryElement {

    void setWindow(RenderWindow window, InputHandler handler);

    int getSize();

    void setOffset(int offset);

    int getOffset();

    boolean requiresTicking();

    void tick();

    String getButtonName(int button, int extra);

    @Override
    default String getResourceType() {
        return "input_handler";
    }
}
