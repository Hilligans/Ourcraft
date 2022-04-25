package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Util.Logger;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InputHandler {

    GameInstance gameInstance;
    Logger logger;
    public ArrayList<IInputProvider> inputProviders = new ArrayList<>();
    public HashMap<String, IInputProvider> hashedProvider = new HashMap<>();
    public Int2ObjectOpenHashMap<ArrayList<Input>> keyBinds = new Int2ObjectOpenHashMap<>();
    public float[] pressedInputs;
    public RenderWindow window;
    public int size = 0;

    public InputHandler(GameInstance gameInstance, RenderWindow window) {
        this.gameInstance = gameInstance;
        this.window = window;
    }

    public void add(IInputProvider iInputProvider) {
        inputProviders.add(iInputProvider);
        iInputProvider.setOffset(size);
        hashedProvider.put(iInputProvider.getIdentifierName(),iInputProvider);
        size += iInputProvider.getSize();
    }

    public void completeSetup() {
        pressedInputs = new float[size];
        for(Input input : gameInstance.KEY_BINDS.ELEMENTS) {
            int[] vals = input.build(this);
            for(int x : vals) {
                ArrayList<Input> binds = keyBinds.computeIfAbsent(x,(a) -> new ArrayList<>());
                binds.add(input);
            }
        }
    }

    /**
     * @param input
     * @param windowID can be -1 if not associated with window;
     * @param provider
     */

    public void handleInput(int input, float mode, long windowID, int action, int scancode, int extra, IInputProvider provider) {
        if(windowID == window.getWindowID() || windowID == -1) {
            input += provider.getOffset();
            pressedInputs[input] = mode;
            ArrayList<Input> inputs = keyBinds.get(input);
            if(inputs != null) {
                for(Input input1 : inputs) {
                    input1.process(this,input,mode,window,action);
                }
            }
        }
    }

    public void handleInput(int input, float mode, long windowID, IInputProvider provider) {
        handleInput(input,mode,windowID,0,0,0,provider);
    }

    public void handleInput(int input, float mode, long windowID, int action, IInputProvider provider) {
        handleInput(input,mode,windowID,action,0,0,provider);
    }
}
