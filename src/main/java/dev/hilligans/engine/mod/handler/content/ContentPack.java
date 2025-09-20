package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.GameInstance;

import java.util.HashMap;

public class ContentPack {

    public GameInstance gameInstance;

    public HashMap<String, ModContent> mods = new HashMap<>();
    public HashMap<String, String> modStates = new HashMap<>();

    public ContentPack(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }
}
