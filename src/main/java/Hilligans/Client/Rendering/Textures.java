package Hilligans.Client.Rendering;

import Hilligans.Client.Rendering.Texture;
import Hilligans.ClientMain;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarFile;

public class Textures {

    public static ArrayList<Texture> textures = new ArrayList<>();

    public static HashMap<String, Texture> mappedTextures = new HashMap<>();

    public static final Texture ITEM_SLOT = new Texture("GUI/item_slot.png");
    public static final Texture INVENTORY = new Texture("GUI/inventory.png");
    public static final Texture HOTBAR = new Texture("GUI/hotbar.png");
    public static final Texture ITEM_OUTLINE = new Texture("GUI/item_outline.png");
    public static final Texture CHEST = new Texture("GUI/chest.png");

    public static final Texture FOLDER = new Texture("GUI/folder.png");
    public static final Texture LIST_ICON = new Texture("GUI/list_icon.png");
    public static final Texture PLUS_ICON = new Texture("GUI/plus.png");
    public static final Texture MINUS_ICON = new Texture("GUI/minus.png");
    public static final Texture BYTE_ICON = new Texture("GUI/byte_icon.png");
    public static final Texture SHORT_ICON = new Texture("GUI/short_icon.png");
    public static final Texture INTEGER_ICON = new Texture("GUI/integer_icon.png");
    public static final Texture FLOAT_ICON = new Texture("GUI/float_icon.png");
    public static final Texture LONG_ICON = new Texture("GUI/long_icon.png");
    public static final Texture DOUBLE_ICON = new Texture("GUI/double_icon.png");

    public static void clear() {
        for(Texture texture : mappedTextures.values()) {
            GL30.glDeleteTextures(texture.textureId);
        }
        mappedTextures = new HashMap<>();
    }

    public static void addTexture(String name, BufferedImage texture) {
        Texture tex = new Texture(name,texture);
        mappedTextures.put(name, tex);
        ClientMain.queued = tex;
    }


}
