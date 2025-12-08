package dev.hilligans.engine.resource;

import dev.hilligans.engine.mod.Identifier;
import dev.hilligans.engine.mod.content.ModContainer;

public class ResourceLocation {

    public String path;
    public String sSource;

    public ResourceLocation(String path) {
        this.path = path;
    }

    public ResourceLocation(String path, ModContainer source) {
        this(path, source.getModID());
    }

    public ResourceLocation(String path, String source) {
        this.path = path;
        this.sSource = source;
    }

    public String getSource() {
        return sSource;
    }

    public ResourceLocation withPrefix(String prefix) {
        return new ResourceLocation(prefix + this.path, this.sSource);
    }

    public ResourceLocation withSuffix(String suffix) {
        return new ResourceLocation(this.path + suffix, this.sSource);
    }

    public ResourceLocation with(String prefix, String suffix) {
        return new ResourceLocation(prefix + this.path + suffix, this.sSource);
    }

    @Override
    public String toString() {
        return "ResourceLocation{path='" + path + "\', source=" + sSource + "}";
        //return STR."ResourceLocation{path='\{path}\{'\''}, source=\{source == null ? sSource : source.getModID()}\{'}'}";
    }

    public Identifier toIdentifier() {
        return new Identifier(path, getSource());
    }

    public String identifier() {
        return getSource() + ":" + path;
    }
}
