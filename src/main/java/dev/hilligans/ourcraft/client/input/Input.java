package dev.hilligans.ourcraft.client.input;

import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Input implements IRegistryElement {

    public boolean repeating;
    public String key;
    public String modID;
    public int[] keyBinds;
    public String[] pipelines = null;

    public ArrayList<String> boundKey = new ArrayList<>(1);

    public String displayName;

    public ModContent modContent;

    public Input(String defaultBind) {
        bind(defaultBind);
    }

    public Input(String defaultBind, boolean repeating) {
        bind(defaultBind);
        this.repeating = repeating;
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

    public void press(RenderWindow renderWindow, float strength) {}

    public void repeat(RenderWindow renderWindow, float strength) {}

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
        return key;
    }

    @Override
    public String getResourceOwner() {
        return modID;
    }

    @Override
    public String getResourceType() {
        return "key_bind";
    }

    public void setModContent(ModContent modContent) {
        this.modContent = modContent;
        this.modID = modContent.getModID();
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
                System.out.println(source);
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
