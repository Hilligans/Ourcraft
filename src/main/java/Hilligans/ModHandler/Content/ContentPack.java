package Hilligans.ModHandler.Content;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Item.Item;
import Hilligans.Item.Items;
import Hilligans.Util.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ContentPack {

    public HashMap<String, ModContent> mods = new HashMap<>();
    public HashMap<String, Boolean> loadedMods = new HashMap<>();
    public HashMap<String, Boolean> shouldLoad = new HashMap<>();

    public void load() {
        for(String string : mods.keySet()) {
            recursivelyLoad(string);
            shouldLoad.put(string,true);
        }
    }

    public void generateData() {
        Blocks.BLOCKS.clear();
        Blocks.MAPPED_BLOCKS.clear();
        Items.ITEMS.clear();
        Items.HASHED_ITEMS.clear();
        Textures.TEXTURES.clear();
        Textures.MAPPED_TEXTURES.clear();
        for(String string : mods.keySet()) {
            if(shouldLoad.get(string)) {
                ModContent mod = mods.get(string);
                for (Block block : mod.blocks) {
                    Blocks.registerBlock(block);
                }
                for (Item item : mod.items) {
                    Items.registerItem(item);
                }
                for (Texture texture : mod.textures) {
                    Textures.registerTexture(texture);
                }
            }
        }
    }

    public void recursivelyLoad(String modID) {
        if(!mods.containsKey(modID)) {
            System.err.println("Missing Dependency: " + modID);
        }
        ModContent modContent = mods.get(modID);
        if(loadedMods.getOrDefault(modID,false)) {
            return;
        }
        for(ModDependency modDependency : modContent.dependencies) {
            if(!loadedMods.getOrDefault(modDependency.modID,false)) {
                recursivelyLoad(modDependency.modID);
            }
        }
        try {
            modContent.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadedMods.put(modID,true);
    }

}
