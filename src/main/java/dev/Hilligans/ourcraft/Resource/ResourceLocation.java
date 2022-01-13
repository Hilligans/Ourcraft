package dev.Hilligans.ourcraft.Resource;

import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Mod;

public class ResourceLocation {

    public String path;
    public ModContent source;

    public ResourceLocation(String path) {
        this.path = path;
    }

    public ResourceLocation(String path, ModContent source) {
        this.path = path;
        this.source = source;
    }
}
