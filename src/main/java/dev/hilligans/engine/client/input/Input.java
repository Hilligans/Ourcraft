package dev.hilligans.engine.client.input;

import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.IInputProvider;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.UnknownResourceException;
import dev.hilligans.engine.mod.handler.exception.ResourceInitializationException;
import dev.hilligans.engine.util.registry.IRegistryElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Input implements IRegistryElement {

    public boolean repeating;
    public String name;
    public String key;
    public String modID;
    public int[] keyBinds;
    public String[] pipelines = null;

    public ArrayList<String> boundKey = new ArrayList<>(1);

    public String displayName;

    public Input(String name, String defaultBind) {
        this.name = name;
        bind(defaultBind);

        track();
    }

    public Input(String name, String defaultBind, boolean repeating) {
        this.name = name;
        bind(defaultBind);
        this.repeating = repeating;

        track();
    }

    public boolean canInput(String pipeline) {
        if(pipelines == null) {
            return true;
        }
        for(String s : pipelines) {
            if(pipeline.equals(s)) {
                return true;
            }
        }
        //System.out.println(pipeline);
        return false;
    }

    public void press(RenderWindow renderWindow, double strength) {}

    public void repeat(RenderWindow renderWindow, double strength) {}

    public void bind(String key) {
        this.boundKey.add(key);
    }

    public void removeBind(String key) {
        boundKey.remove(key);
    }

    public void removeBind(int index) {
        boundKey.remove(index);
    }

    public Input onlyWithPipelines(String... pipelines) {
        this.pipelines = pipelines;
        return this;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return modID;
    }

    @Override
    public String getResourceType() {
        return "key_bind";
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.modID = owner.getModID();
    }

    public String getDisplay() {
        return displayName;
    }

    public void process(InputHandler inputHandler, int input, float mode, RenderWindow window, int action, float strength) {
        //TODO fix
         if(action == GLFW_PRESS || action == GLFW_REPEAT) {
            press(window, strength);
        }
    }

    public int[] build(InputHandler handler) {
        IntArrayList list = new IntArrayList();

        for(String key : boundKey) {
            String[] parts = key.split("\\.");

           //int[] vals = new int[parts.length];
            StringBuilder builder = new StringBuilder();

            for (String string : parts) {
                String[] pieces = string.split("::");
                String source = pieces[0];
                int index = Integer.parseInt(pieces[1]);

                IInputProvider provider = handler.hashedProvider.get(source);
                if(provider == null) {
                    throw new ResourceInitializationException(this, "Failed to load resource from key string: " + string);
                }
                builder.append(provider.getButtonName(index, 0)).append(' ');
                int newIndex = provider.getOffset() + index;
                if(!list.contains(newIndex)) {
                    list.add(newIndex);
                }
            }

            displayName = builder.toString();
            //keyBinds = vals;
        }

        return list.toIntArray();
    }
}
