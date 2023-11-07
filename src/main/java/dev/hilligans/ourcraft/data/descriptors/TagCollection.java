package dev.hilligans.ourcraft.data.descriptors;

import java.util.HashMap;

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

    public boolean compare(TagCollection tagCollection) {
        for(String tag : tagCollection.tags.keySet()) {
            if(!tags.containsKey(tag)) {
                return false;
            }
            if(!tags.get(tag).tagName.contains(tagCollection.tags.get(tag).tagName)) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(Tag tag) {
        return tags.containsKey(tag.tagName);
    }
}
