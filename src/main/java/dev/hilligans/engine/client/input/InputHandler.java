package dev.hilligans.engine.client.input;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
import dev.hilligans.engine.util.Logger;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.HashMap;

public class InputHandler {

    GameInstance gameInstance;
    Logger logger;
    public ArrayList<IInputProvider> inputProviders = new ArrayList<>();
    public HashMap<String, IInputProvider> hashedProvider = new HashMap<>();
    public float[] pressedInputs;
    public ArrayList<Input>[] inputs;
    public ArrayList<Input>[] repeatingInputs;
    public IntArrayList repeatingIds = new IntArrayList();
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
        inputs = new ArrayList[size];
        repeatingInputs = new ArrayList[size];
        for(Input input : gameInstance.KEY_BINDS.ELEMENTS) {
            int[] vals = input.build(this);
            for(int x : vals) {
                ArrayList<Input> binds;
                if(input.repeating) {
                    repeatingIds.add(x);
                    binds = repeatingInputs[x] = repeatingInputs[x] == null ? new ArrayList<>() : repeatingInputs[x];
                } else {
                    binds = inputs[x] = inputs[x] == null ? new ArrayList<>() : inputs[x];
                }
                binds.add(input);
            }
        }
    }

    /**
     * @param input
     * @param windowID can be -1 if not associated with window;
     * @param provider
     */

    public void handleInput(int input, int mode, long windowID, int action, int scancode, int extra, float strength, IInputProvider provider) {
        if(windowID == window.getWindowID() || windowID == -1) {
            input += provider.getOffset();
            pressedInputs[input] = strength;
            ArrayList<Input> inputs = this.inputs[input];
            if(inputs != null) {
                for(Input input1 : inputs) {
                    if(input1.canInput(window.renderPipeline.getIdentifierName())) {
                        input1.process(this, input, mode, window, action, strength);
                    }
                }
            }
        }
    }

    public void handleInput(int input, int mode, long windowID, IInputProvider provider) {
        handleInput(input,mode,windowID,0,0,0, mode != 0 ? 1 : 0, provider);
    }

    public void handleInput(int input, int mode, long windowID, int action, IInputProvider provider) {
        handleInput(input,mode,windowID,action,0,0,mode != 0 ? 1 : 0,provider);
    }

    public void tick(double deltaTime) {
        if(deltaTime > 100) {
            throw new RuntimeException();
        }
        for(int x = 0; x < repeatingIds.size(); x++) {
            int val = repeatingIds.getInt(x);
            float strength = pressedInputs[val];
            if(strength != 0) {
                ArrayList<Input> inputs1 = repeatingInputs[val];
                for (Input input : inputs1) {
                    if(input.canInput(window.renderPipeline.getIdentifierName())) {
                        input.repeat(window, strength * deltaTime);
                    }
                }
            }
        }
    }
}
