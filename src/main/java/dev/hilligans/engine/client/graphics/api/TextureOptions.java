package dev.hilligans.engine.client.graphics.api;

import java.util.HashMap;

public record TextureOptions(HashMap<String, String> extraMap, TextureCompression compression) {

    public static final TextureOptions NO_OPTIONS = new TextureOptions(new HashMap<>(), TextureCompression.NONE);

}
