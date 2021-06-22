package Hilligans.ModHandler.Content;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.ClientMain;
import Hilligans.Item.Item;
import Hilligans.Item.Items;
import Hilligans.ModHandler.Events.Client.RenderEndEvent;
import Hilligans.Ourcraft;
import Hilligans.Resource.ResourceManager;
import Hilligans.Util.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ContentPack {

    public HashMap<String, ModContent> mods = new HashMap<>();
    public HashMap<String, Boolean> loadedMods = new HashMap<>();
    public HashMap<String, Boolean> shouldLoad = new HashMap<>();

    public ContentPack() {
        Ourcraft.EVENT_BUS.register(t -> {
            if(waiting) {
                waiting = false;
                rebuild();
            }
        }, RenderEndEvent.class);
    }

    public void load() {
        for(String string : mods.keySet()) {
            recursivelyLoad(string);
            shouldLoad.put(string,true);
        }
    }

    private boolean waiting = false;

    public void generateData() {
        Ourcraft.REBUILDING.set(true);
       if(!Settings.isServer && ClientMain.getClient().rendering) {
           waiting = true;
       } else {
           rebuild();
       }
    }

    ///TODO use Blocks, Items, Entity Instances so the game can still render while its being reupdated.
    private void rebuild() {
        if(!Settings.isServer) {
            ClientMain.getClient().refreshTexture = true;
        }
        Blocks.BLOCKS.clear();
        Blocks.MAPPED_BLOCKS.clear();
        Items.ITEMS.clear();
        Items.HASHED_ITEMS.clear();
        Ourcraft.RESOURCE_MANAGER.clearData();
        //Textures.TEXTURES.clear();
        //Textures.MAPPED_TEXTURES.clear();
        for(String string : mods.keySet()) {
            if(shouldLoad.get(string)) {
                ModContent mod = mods.get(string);
                for (Texture texture : mod.textures) {
                    Textures.registerTexture(texture);
                }
                for(String string1 : mod.blockTextures.keySet()) {
                    Ourcraft.RESOURCE_MANAGER.putImage("Blocks/" + string1,mod.blockTextures.get(string1));
                }
                for (Block block : mod.blocks) {
                    Blocks.registerBlock(block);
                }
                for (Item item : mod.items) {
                    Items.registerItem(item);
                }
            }
        }
        Ourcraft.REBUILDING.set(false);
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


    public String[] getModList() {
        String[] modList = new String[mods.size() - 1];
        int x = 0;
        for(String string : mods.keySet()) {
            if(!string.equals("ourcraft")) {
                ModContent modContent = mods.get(string);
                modList[x] = modContent.modID + ":::" + modContent.version;
                x++;
            }
        }
        return modList;
    }

    public void putMod(ModContent modContent) {
        mods.put(modContent.modID,modContent);
        shouldLoad.put(modContent.modID,true);
    }

    public void releaseMod(String mod) {
        ModContent modContent = mods.remove(mod);
        Ourcraft.RESOURCE_MANAGER.classLoaders.remove(modContent.classLoader);
        try {
            modContent.classLoader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}