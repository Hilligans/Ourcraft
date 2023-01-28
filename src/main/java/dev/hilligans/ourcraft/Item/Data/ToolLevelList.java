package dev.hilligans.ourcraft.Item.Data;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.ArrayList;

public class ToolLevelList {

    public ArrayList<ToolLevel> levelList = new ArrayList<>();
    public Object2IntOpenHashMap<String> id = new Object2IntOpenHashMap<>();

    public synchronized ToolLevelList insert(int index, ToolLevel... materials) {
        for(int x = materials.length - 1; x > 0; x--) {
            levelList.add(index,materials[x]);
        }
        id.clear();
        for(int x = 0; x < levelList.size(); x++) {
            id.put(levelList.get(x).name.getName(),x);
        }
        return this;
    }

    public synchronized ToolLevelList insertAfter(String location, ToolLevel... materials) {
        return insert(id.getInt(location), materials);
    }

    public synchronized ToolLevelList insertBefore(String location, ToolLevel... materials) {
        return insert(id.getInt(location) - 1, materials);
    }

    public ToolLevelList addToStart(ToolLevel... materials) {
        return insert(0,materials);
    }

    public ToolLevelList addToEnd(ToolLevel... materials) {
        return insert(levelList.size(),materials);
    }
}
