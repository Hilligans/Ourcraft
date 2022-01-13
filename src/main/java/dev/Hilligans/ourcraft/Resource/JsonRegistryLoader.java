package dev.Hilligans.ourcraft.Resource;

import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Identifier;
import org.json.JSONObject;

public abstract class JsonRegistryLoader extends RegistryLoader {

    public String path;

    public JsonRegistryLoader(Identifier name, String path) {
        super(name);
        this.path = path;
    }

    @Override
    public void run() {
        for(ModContent modContent : gameInstance.CONTENT_PACK.mods.values()) {
            JSONObject jsonObject = (JSONObject) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(path,modContent));
            run(modContent, jsonObject);
        }
    }

    public abstract void run(ModContent modContent, JSONObject resource);
}
