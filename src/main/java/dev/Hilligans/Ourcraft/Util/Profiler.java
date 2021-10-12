package dev.Hilligans.Ourcraft.Util;

import dev.Hilligans.Ourcraft.Data.Primitives.Tuplet;

import java.util.HashMap;

public class Profiler {

    public HashMap<String, Tuplet<Long,String>> profiles = new HashMap<>();
    public String parent = "";

    public void startProfiling(String name) {
        profiles.put(name, new Tuplet<>(System.currentTimeMillis(), parent));
    }

    public void profile(String name) {
        profiles.put(name, new Tuplet<>(System.currentTimeMillis(), parent));
    }

    public void stopProfiling(String name) {
        Tuplet<Long,String> data = profiles.get(name);
        if(data != null) {
            data.typeA = System.currentTimeMillis() - data.getTypeA();
            profiles.put(name,data);
        }
    }

    public void printProfile() {
        for(String s : profiles.keySet()) {
            System.out.println(s + " Time:" + profiles.get(s).typeA);
        }
        profiles.clear();
    }

}
