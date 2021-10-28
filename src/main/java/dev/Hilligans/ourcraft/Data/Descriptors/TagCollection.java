package dev.Hilligans.ourcraft.Data.Descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TagCollection {

    public HashMap<String,Tag> tags = new HashMap<>();

    public TagCollection put(Tag tag) {
        tags.put(tag.tagName,tag);
        return this;
    }

    public TagCollection remove(Tag tag) {
        tags.remove(tag.tagName);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TagCollection tagCollection) {
            for(String tag : tagCollection.tags.keySet()) {
                if(!tags.containsKey(tag)) {
                    return false;
                }
            }
        }
        return true;
    }
}
