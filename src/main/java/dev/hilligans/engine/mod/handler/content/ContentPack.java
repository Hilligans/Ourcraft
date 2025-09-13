package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.client.Client;

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
