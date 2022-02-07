package dev.Hilligans.ourcraft.Util.GameResource;

import java.util.Random;

public class WorldGameResource extends GameResource {

    public String worldName;

    public WorldGameResource(String name) {
        this.worldName = name;
    }

    @Override
    public String toString() {
        return "WorldGameResource{" +
                "worldName='" + worldName + '\'' +
                '}';
    }
}
