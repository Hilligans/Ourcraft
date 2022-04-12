package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Util.Logger;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;

public class InputHandler {

    GameInstance gameInstance;
    Logger logger;
    public ArrayList<IInputProvider> inputProviders = new ArrayList<>();
    public float[] pressedInputs;
    public long window;
    public int size = 0;

    public InputHandler(GameInstance gameInstance, long window) {
        this.gameInstance = gameInstance;
        this.window = window;
    }

    public void add(IInputProvider iInputProvider) {
        inputProviders.add(iInputProvider);
        iInputProvider.setOffset(size);
        size += iInputProvider.getSize();
    }

    public void completeSetup() {
        pressedInputs = new float[size];
    }

    /**
     * @param input
     * @param windowID can be -1 if not associated with window;
     * @param provider
     */

    public void handleInput(int input, float mode, long windowID, int action, int scancode, int extra, IInputProvider provider) {
        if(windowID == window || windowID == -1) {
            System.out.println(provider.getButtonName(input, scancode));
            input += provider.getOffset();
            pressedInputs[input] = mode;
        }
    }

    public void handleInput(int input, float mode, long windowID, IInputProvider provider) {
        handleInput(input,mode,windowID,0,0,0,provider);
    }
}
