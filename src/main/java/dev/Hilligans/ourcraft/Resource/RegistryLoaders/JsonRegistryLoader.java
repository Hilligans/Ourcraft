package dev.Hilligans.ourcraft.Resource.RegistryLoaders;

import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Identifier;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.TriConsumer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class JsonRegistryLoader extends ModRegistryLoader<JSONObject> {

    public String path;
    public HashMap<String, TriConsumer<ModContent, JSONObject, String>> elementFunction = new HashMap<>();
    public TriConsumer<ModContent, JSONObject, String> defaultFunction;

    public JsonRegistryLoader(Identifier name, String path, TriConsumer<ModContent, JSONObject, String> defaultFunction) {
        super(name);
        registerLoader((modContent, jsonObject) -> {
            TriConsumer<ModContent, JSONObject, String> function = elementFunction.getOrDefault(modContent.modID, defaultFunction);
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
