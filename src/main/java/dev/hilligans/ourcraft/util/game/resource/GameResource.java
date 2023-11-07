package dev.hilligans.ourcraft.util.game.resource;

public class GameResource {

    public String prefix;
    public long uniqueID;

    public GameResource() {

    }

    public GameResource setUniqueID(long id) {
        this.uniqueID = id;
        return this;
    }
}
