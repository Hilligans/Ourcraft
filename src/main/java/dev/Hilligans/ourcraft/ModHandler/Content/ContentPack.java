package dev.Hilligans.ourcraft.ModHandler.Content;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Audio.SoundBuffer;
import dev.Hilligans.ourcraft.Client.Audio.Sounds;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.IModel;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Primitives.Triplet;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Item.Item;
import dev.Hilligans.ourcraft.Item.Items;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.RenderEndEvent;
import dev.Hilligans.ourcraft.Network.Protocol;
import dev.Hilligans.ourcraft.Network.Protocols;
import dev.Hilligans.ourcraft.Util.Settings;

import java.util.HashMap;

public class ContentPack {

    public GameInstance gameInstance;

    public HashMap<String, ModContent> mods = new HashMap<>();
    public HashMap<String, Boolean> loadedMods = new HashMap<>();
    public HashMap<String, Boolean> shouldLoad = new HashMap<>();
    public ContentPack(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        gameInstance.EVENT_BUS.register(t -> {
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
        gameInstance.REBUILDING.set(true);
        ///TODO Could potentially rebuild when rendering but very unlikely
       if(!Settings.isServer && !(ClientMain.getClient() == null || ClientMain.getClient().rendering)) {
           waiting = true;
       } else {
           rebuild();
       }
    }

    public void registerModContent(ModContent modContent) {
        mods.put(modContent.modID,modContent);
        gameInstance.RESOURCE_MANAGER.classLoaders.add(modContent.classLoader);
        gameInstance.MOD_LOADER.mainClasses.computeIfAbsent(modContent.modID,a -> new Triplet<>(modContent.mainClass, modContent.mainClass.getProtectionDomain().getCodeSource().getLocation().getPath(), false));
    }

    ///TODO use Blocks, Items, Entity Instances so the game can still render while its being reupdated.
    private void rebuild() {
        if(!Settings.isServer) {
            if(ClientMain.getClient() != null) {
                ClientMain.getClient().refreshTexture = true;
            }
        }
        gameInstance.clear();
        Items.ITEMS.clear();
        Items.HASHED_ITEMS.clear();
        Sounds.SOUNDS.clear();
        Sounds.MAPPED_SOUND.clear();
        gameInstance.RESOURCE_MANAGER.clearData();
        Protocols.clear();

        for(String string : mods.keySet()) {
            if(shouldLoad.get(string)) {
                ModContent mod = mods.get(string);
                for(SoundBuffer soundBuffer : mod.sounds) {
                   // soundBuffer.load();
                    Sounds.registerSound(soundBuffer.file,soundBuffer);
                }
                for (Texture texture : mod.textures) {
                    Textures.registerTexture(texture);
                }
                for(String string1 : mod.blockTextures.keySet()) {
                    gameInstance.RESOURCE_MANAGER.putImage("Blocks/" + string1,mod.blockTextures.get(string1));
                }
                for (Block block : mod.blocks) {
                    gameInstance.registerBlock(block);
                }
                for (Item item : mod.items) {
                    Items.registerItem(item);
                }
                for(IModel model : mod.models) {
                    gameInstance.RESOURCE_MANAGER.putModel(model.getPath(),model);
                }
                for(Protocol protocol : mod.protocols.values()) {
                    Protocols.register(protocol);
                }
            }
        }
        gameInstance.REBUILDING.set(false);
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
        gameInstance.RESOURCE_MANAGER.classLoaders.remove(modContent.classLoader);
        try {
            modContent.classLoader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCachedMod(String name) {
        ModContent modContent = ModContent.readLocal(name,gameInstance);
        if(modContent != null) {
            putMod(modContent);
        }
    }
}
