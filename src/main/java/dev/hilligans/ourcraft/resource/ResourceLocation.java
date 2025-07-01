package dev.hilligans.ourcraft.resource;

import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.mod.handler.Identifier;

public class ResourceLocation {

    public String path;
    public ModContent source;
    public String sSource;

    public ResourceLocation(String path) {
        this.path = path;
    }

    public ResourceLocation(String path, ModContent source) {
        this.path = path;
        this.source = source;
    }

    public ResourceLocation(String path, ModContainer source) {
        this(path, source.getModID());
    }

    public ResourceLocation(String path, String source) {
        this.path = path;
        this.sSource = source;
    }

    public String getSource() {
        if(source == null) {
            return sSource;
        }
        return source.getModID();
    }

    @Override
    public String toString() {
        return "ResourceLocation{path='" + path + "\', source=" + (source == null ? sSource : source.getModID()) + "}";
        //return STR."ResourceLocation{path='\{path}\{'\''}, source=\{source == null ? sSource : source.getModID()}\{'}'}";
    }

    public Identifier toIdentifier() {
        return new Identifier(path, getSource());
    }

    public String identifier() {
        return getSource() + ":" + path;
    }
}
