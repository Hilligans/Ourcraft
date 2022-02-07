package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Client;

public abstract class RenderWindow {

    public FrameTracker frameTracker = new FrameTracker();

    public IInputProvider inputProvider;

    public abstract void close();

    public abstract boolean shouldClose();

    public abstract void swapBuffers();

    public abstract Client getClient();

    public abstract IInputProvider getInputProvider();

}
