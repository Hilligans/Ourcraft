package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

import java.util.ArrayList;

public class Input {

    public ArrayList<IInputProvider> inputProviders = new ArrayList<>();

    public void add(IInputProvider iInputProvider) {
        inputProviders.add(iInputProvider);
    }

}
