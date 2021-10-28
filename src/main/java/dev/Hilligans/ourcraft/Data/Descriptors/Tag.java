package dev.Hilligans.ourcraft.Data.Descriptors;

import java.util.Objects;

public class Tag {

    public String type;
    public String tagName;

    public Tag(String type, String tagName) {
        this.type = type;
        this.tagName = tagName;
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
}
