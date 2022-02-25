package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

import java.util.ArrayList;

public class InputHandler {

    public ArrayList<IInputProvider> inputProviders = new ArrayList<>();
    public int size = 0;

    public void add(IInputProvider iInputProvider) {
        inputProviders.add(iInputProvider);
        iInputProvider.setOffset(size);
        size += iInputProvider.getSize();
    }
}
