package Hilligans.ModHandler.Content;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Audio.SoundBuffer;
import Hilligans.Client.Audio.Sounds;
import Hilligans.Client.Rendering.NewRenderer.IModel;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.Triplet;
import Hilligans.Item.Item;
import Hilligans.Item.Items;
import Hilligans.ModHandler.Events.Client.RenderEndEvent;
import Hilligans.Network.Protocol;
import Hilligans.Network.Protocols;
import Hilligans.Ourcraft;
import Hilligans.Util.Settings;
import Hilligans.WorldSave.WorldLoader;
import org.json.JSONObject;

import java.util.HashMap;

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
        ///TODO Could potentially rebuild when rendering but very unlikely
       if(!Settings.isServer && ClientMain.getClient().rendering) {
           waiting = true;
       } else {
           rebuild();
       }
    }

    public void registerModContent(ModContent modContent) {
        mods.put(modContent.modID,modContent);
        Ourcraft.RESOURCE_MANAGER.classLoaders.add(modContent.classLoader);
        Ourcraft.MOD_LOADER.mainClasses.computeIfAbsent(modContent.modID,a -> new Triplet<>(modContent.mainClass, modContent.mainClass.getProtectionDomain().getCodeSource().getLocation().getPath(), false));
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
        Sounds.SOUNDS.clear();
        Sounds.MAPPED_SOUND.clear();
        Ourcraft.RESOURCE_MANAGER.clearData();
        Protocols.clear();

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
                for(SoundBuffer soundBuffer : mod.sounds) {
                    Sounds.registerSound(soundBuffer.file,soundBuffer);
                }
                for(IModel model : mod.models) {
                    Ourcraft.RESOURCE_MANAGER.putModel(model.getPath(),model);
                }
                for(Protocol protocol : mod.protocols.values()) {
                    Protocols.register(protocol);
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

    public void loadCachedMod(String name) {
        ModContent modContent = ModContent.readLocal(name);
        if(modContent != null) {
            putMod(modContent);
        }
    }
}
