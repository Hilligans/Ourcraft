package dev.Hilligans.ourcraft.Client.Input;

import dev.Hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public abstract class Input implements IRegistryElement {

    public String key;
    public String modID;
    public int[] keyBinds;

    public String boundKey;

    public String displayName;

    public ModContent modContent;

    public Input(String defaultBind) {
        bind(defaultBind);
    }

    public abstract void invoke(RenderWindow renderWindow, float strength);

    public void bind(String key) {
        this.boundKey = key;
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
            invoke(window, mode);
        }
    }

    public int[] build(InputHandler handler) {
        String[] parts = boundKey.split("\\.");

        int[] vals = new int[parts.length];
        StringBuilder builder = new StringBuilder();
        int x = 0;

        for(String string : parts) {
            String[] pieces = string.split("::");
            String source = pieces[0];
            int index = Integer.parseInt(pieces[1]);

            IInputProvider provider = handler.hashedProvider.get(source);
            System.out.println(source);
            builder.append(provider.getButtonName(index,0)).append(' ');
            int newIndex = provider.getOffset() + index;
            vals[x++] = newIndex;
        }

        displayName = builder.toString();
        keyBinds = vals;

        return vals;
    }
}
