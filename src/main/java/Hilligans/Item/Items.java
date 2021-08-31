package Hilligans.Item;

import Hilligans.Data.Other.ItemProperties;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.Ourcraft;

import java.util.ArrayList;
import java.util.HashMap;

public class Items {
 
    public static final ArrayList<Item> ITEMS = new ArrayList<>();
    public static final HashMap<String, Item> HASHED_ITEMS = new HashMap<>();

    public static Item TEST_ITEM = new Item("test_item",new ItemProperties().addModel("pickaxe.png"),"ourcraft");


    static short id = 0;
    public static short getNextId() {
        short val = id;
        id++;
        return val;
    }

    public static void registerItem(Item item) {
        Items.ITEMS.add(item);
        Items.HASHED_ITEMS.put(item.name, item);
    }

    public static Item getItem(int id) {
        if(ITEMS.size() > id) {
            return ITEMS.get(id);
        }
        return null;
    }

    public static void register() {}

    static {
        ModContent ourcraft = Ourcraft.OURCRAFT;
        ourcraft.registerItem(TEST_ITEM);
    }


}
