package dev.hilligans.engine.resource.registry.loaders;

import dev.hilligans.engine.mod.handler.Identifier;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.ModContent;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.util.interfaces.TriConsumer;
import org.json.JSONObject;

import java.util.HashMap;

public class JsonRegistryLoader extends ModRegistryLoader<JSONObject> {

    public String path;
    public HashMap<String, TriConsumer<ModContainer, JSONObject, String>> elementFunction = new HashMap<>();
    public TriConsumer<ModContainer, JSONObject, String> defaultFunction;

    public JsonRegistryLoader(String name, String path, TriConsumer<ModContainer, JSONObject, String> defaultFunction) {
        super(name);
        registerLoader((modContent, jsonObject) -> {
            TriConsumer<ModContainer, JSONObject, String> function = elementFunction.getOrDefault(modContent.getModID(), defaultFunction);
            for (String s : jsonObject.keySet()) {
                function.accept(modContent, jsonObject.getJSONObject(s), s);
            }
        });
        this.path = path;
        this.defaultFunction = defaultFunction;
    }

    //TODO maybe check if the resource exists before trying to load it to avoid an exception
    @Override
    public JSONObject provideResource(ModContainer modContent) {
        try {
            return (JSONObject) getGameInstance().RESOURCE_LOADER.getResource(new ResourceLocation(path, modContent));
        } catch (Exception e) {
            return null;
        }
    }
}
