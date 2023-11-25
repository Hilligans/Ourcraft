package dev.hilligans.ourcraft.data.descriptors;

import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.Objects;

public class Tag implements IRegistryElement {

    public String type;
    public String tagName;
    public String source;

    public Tag(String type, String tagName, String source) {
        this.type = type;
        this.tagName = tagName;
    }

    public String asString() {
        return "#" + source + ":" + type + "." + tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(type, tag.type) && Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, tagName);
    }

    @Override
    public String getResourceName() {
        return tagName;
    }

    @Override
    public String getResourceOwner() {
        return source;
    }

    @Override
    public String getResourceType() {
        return "tag";
    }
}
