package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.engine.client.graphics.util.Texture;
import dev.hilligans.engine.mod.handler.content.CoreExtensionView;

public class Textures {

    private static final String MOD_ID = "ourcraft";

    public static final Texture ITEM_SLOT = new Texture("Images/GUI/item_slot.png", MOD_ID);
    public static final Texture INVENTORY = new Texture("Images/GUI/inventory.png", MOD_ID);
    public static final Texture HOTBAR = new Texture("Images/GUI/hotbar.png", MOD_ID);
    public static final Texture ITEM_OUTLINE = new Texture("Images/GUI/item_outline.png", MOD_ID);
    public static final Texture CHEST = new Texture("Images/GUI/chest.png", MOD_ID);
    public static final Texture CREATIVE_INVENTORY = new Texture("Images/GUI/creative_inventory.png", MOD_ID);
    public static final Texture CURSOR = new Texture("Images/cursor.png", MOD_ID);
    public static final Texture EMPTY_CHUNK = new Texture("Images/GUI/empty_chunk.png", MOD_ID);

    public static final Texture SEARCH_BAR = new ResizingTexture("Images/GUI/search_bar.png", MOD_ID).startSegment(0,0,1,16).middleSegment(1,0,14,16).endSegment(15,0,1,16);

    public static final Texture FOLDER = new Texture("Images/GUI/folder.png", MOD_ID);
    public static final Texture LIST_ICON = new Texture("Images/GUI/list_icon.png", MOD_ID);
    public static final Texture PLUS_ICON = new Texture("Images/GUI/plus.png", MOD_ID);
    public static final Texture MINUS_ICON = new Texture("Images/GUI/minus.png", MOD_ID);
    public static final Texture BYTE_ICON = new Texture("Images/GUI/byte_icon.png", MOD_ID);
    public static final Texture SHORT_ICON = new Texture("Images/GUI/short_icon.png", MOD_ID);
    public static final Texture INTEGER_ICON = new Texture("Images/GUI/integer_icon.png", MOD_ID);
    public static final Texture FLOAT_ICON = new Texture("Images/GUI/float_icon.png", MOD_ID);
    public static final Texture LONG_ICON = new Texture("Images/GUI/long_icon.png", MOD_ID);
    public static final Texture DOUBLE_ICON = new Texture("Images/GUI/double_icon.png", MOD_ID);

    public static final Texture BACKGROUND = new Texture("Images/GUI/background.png", MOD_ID);
    public static final Texture BUTTON = new ResizingTexture("Images/GUI/button.png", MOD_ID).startSegment(0,0,1,16).middleSegment(1,0,14,16).endSegment(15,0,1,16);
    public static final Texture SLIDER = new Texture("Images/GUI/slider.png", MOD_ID);
    public static final Texture SLIDER_BACKGROUND = new Texture("Images/GUI/slider_background.png", MOD_ID);
    public static final Texture BUTTON_DARK = new Texture("Images/GUI/button_dark.png", MOD_ID);
    public static final Texture CHECK_MARK = new Texture("Images/GUI/check_mark.png", MOD_ID);
    public static final Texture X_MARK = new Texture("Images/GUI/x_mark.png", MOD_ID);
    public static final Texture OUTLINE = new Texture("Images/outline.png", MOD_ID);
    public static final Texture TRANSPARENT_BACKGROUND = new Texture("Images/GUI/transparent_background.png", MOD_ID);

    public static final Texture FRAME_TIME = new ResizingTexture("Images/GUI/frame_time.png", MOD_ID).startSegment(0,0,1,101).middleSegment(1,0,99,101).endSegment(100,0,1,101);
    public static final Texture BACKFILL = new Texture("Images/GUI/backfill.png", MOD_ID);


    public static void addData(CoreExtensionView modContent) {
        modContent.registerTexture(ITEM_SLOT,INVENTORY,HOTBAR,ITEM_OUTLINE,CHEST,CREATIVE_INVENTORY,CURSOR,EMPTY_CHUNK);
        modContent.registerTexture(SEARCH_BAR);
        modContent.registerTexture(FOLDER,LIST_ICON,PLUS_ICON,MINUS_ICON,BYTE_ICON,SHORT_ICON,INTEGER_ICON,FLOAT_ICON,LONG_ICON,DOUBLE_ICON);
        modContent.registerTexture(BACKGROUND,BUTTON,SLIDER,SLIDER_BACKGROUND,BUTTON_DARK,CHECK_MARK,X_MARK,OUTLINE,TRANSPARENT_BACKGROUND);
        modContent.registerTexture(FRAME_TIME, BACKFILL);
    }
}
