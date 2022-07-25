package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Input implements IRegistryElement {

    public boolean repeating;
    public String key;
    public String modID;
    public int[] keyBinds;

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

    @Override
    public String getResourceName() {
        return key;
    }

    @Override
    public String getIdentifierName() {
        return modID + ":" + key;
    }

    @Override
    public String getUniqueName() {
        return "key_bind." + modID + "." + key;
    }

    public void setModContent(ModContent modContent) {
        this.modContent = modContent;
        this.modID = modContent.getModID();
    }

    public String getDisplay() {
        return displayName;
    }

    public void process(InputHandler inputHandler, int input, float mode, RenderWindow window, int action) {
        //TODO fix
        if(action == GLFW_PRESS) {
            press(window, mode);
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
