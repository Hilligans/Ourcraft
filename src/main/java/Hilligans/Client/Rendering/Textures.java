package Hilligans.Client.Rendering;

import Hilligans.ClientMain;
import Hilligans.Data.Other.ServerSidedData;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Textures {

    public static final ArrayList<Texture> TEXTURES = new ArrayList<>();

    public static final HashMap<String, Texture> MAPPED_TEXTURES = new HashMap<>();

    public static final Texture ITEM_SLOT = new Texture("GUI/item_slot.png");
    public static final Texture INVENTORY = new Texture("GUI/inventory.png");
    public static final Texture HOTBAR = new Texture("GUI/hotbar.png");
    public static final Texture ITEM_OUTLINE = new Texture("GUI/item_outline.png");
    public static final Texture CHEST = new Texture("GUI/chest.png");
    public static final Texture CREATIVE_INVENTORY = new Texture("GUI/creative_inventory.png");


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

    public static final Texture BACKGROUND = new Texture("GUI/background.png");
    public static final Texture BUTTON = new Texture("GUI/button.png");
    public static final Texture BUTTON_DARK = new Texture("GUI/button_dark.png");
    public static final Texture CHECK_MARK = new Texture("GUI/check_mark.png");
    public static final Texture X_MARK = new Texture("GUI/x_mark.png");
    public static final Texture OUTLINE = new Texture("outline.png");

    public static void clear() {
        for(Texture texture : MAPPED_TEXTURES.values()) {
            GL30.glDeleteTextures(texture.textureId);
        }
        MAPPED_TEXTURES.clear();
    }

    public static void addTexture(String name, BufferedImage texture) {
        Texture tex = new Texture(texture);
        MAPPED_TEXTURES.put(name, tex);
    }

    public static Texture getTexture(int id) {
        if(id >= TEXTURES.size()) {
            return ServerSidedData.getInstance().TEXTURES.get(id - TEXTURES.size());
        }
        return TEXTURES.get(id);
    }

    public static Texture getTexture(String name) {
        Texture texture = MAPPED_TEXTURES.get(name);
        if(texture != null) {
            return texture;
        }
        return ServerSidedData.getInstance().MAPPED_TEXTURES.get(name);
    }


}
