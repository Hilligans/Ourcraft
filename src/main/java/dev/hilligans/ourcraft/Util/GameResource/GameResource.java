package dev.hilligans.ourcraft.Util.GameResource;

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
