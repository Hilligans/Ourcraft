package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.input.Input;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.Texture;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import dev.hilligans.ourcraft.client.rendering.newrenderer.IModel;
import dev.hilligans.ourcraft.data.primitives.Triplet;
import dev.hilligans.ourcraft.item.Item;
import dev.hilligans.ourcraft.network.Protocol;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.resource.registry.loaders.RegistryLoader;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.Feature;

import java.util.ArrayList;
import java.util.HashMap;

public class ContentPack {

    public GameInstance gameInstance;

    public HashMap<String, ModContent> mods = new HashMap<>();
    public HashMap<String, String> modStates = new HashMap<>();
    public HashMap<String, Boolean> shouldLoad = new HashMap<>();

    public ContentPack(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }



    public String[] getModList() {
        String[] modList = new String[mods.size()];
        int x = 0;
        for(String string : mods.keySet()) {
            if(!string.equals("ourcraft")) {
                ModContent modContent = mods.get(string);
                modList[x] = modContent.getModID() + ":::" + modContent.version;
                x++;
            }
        }
        return modList;
    }

    public void putMod(ModContent modContent) {
        mods.put(modContent.getModID(),modContent);
        shouldLoad.put(modContent.getModID(),true);
    }

    public void loadCachedMod(String name, Client client) {
        ModContent modContent = ModContent.readLocal(name,gameInstance, client);
        if(modContent != null) {
            putMod(modContent);
        }
    }
}
