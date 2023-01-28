package dev.hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.hilligans.ourcraft.Client.Input.InputHandler;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Util.Registry.IRegistryElement;

public interface IInputProvider extends IRegistryElement {

    void setWindow(RenderWindow window, InputHandler handler);

    int getSize();

    void setOffset(int offset);

    int getOffset();

    boolean requiresTicking();

    void tick();

    String getButtonName(int button, int extra);
}
