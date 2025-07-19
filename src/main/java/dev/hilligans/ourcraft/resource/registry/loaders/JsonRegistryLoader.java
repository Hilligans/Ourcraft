package dev.hilligans.ourcraft.resource.registry.loaders;

import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.mod.handler.Identifier;
import dev.hilligans.ourcraft.resource.ResourceLocation;
import dev.hilligans.ourcraft.util.interfaces.TriConsumer;
import org.json.JSONObject;

import java.util.HashMap;

public class JsonRegistryLoader extends ModRegistryLoader<JSONObject> {

    public String path;
    public HashMap<String, TriConsumer<ModContent, JSONObject, String>> elementFunction = new HashMap<>();
    public TriConsumer<ModContent, JSONObject, String> defaultFunction;

    public JsonRegistryLoader(Identifier name, String path, TriConsumer<ModContent, JSONObject, String> defaultFunction) {
        super(name);
        registerLoader((modContent, jsonObject) -> {
            TriConsumer<ModContent, JSONObject, String> function = elementFunction.getOrDefault(modContent.getModID(), defaultFunction);
            for (String s : jsonObject.keySet()) {
                function.accept(modContent, jsonObject.getJSONObject(s), s);
            }
        });
        this.path = path;
        this.defaultFunction = defaultFunction;
    }

    //TODO maybe check if the resource exists before trying to load it to avoid an exception
    @Override
    public JSONObject provideResource(ModContent modContent) {
        try {
            return (JSONObject) gameInstance.RESOURCE_LOADER.getResource(new ResourceLocation(path, modContent));
        } catch (Exception e) {
            return null;
        }
    }
}
