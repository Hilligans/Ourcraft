package dev.hilligans.engine.mod;

public class Identifier {

    public String name;

    public String modID;

    public Identifier(String name, String modID) {
        this.name = name;
        this.modID = modID;
    }

    public String getModID() {
        return modID;
    }

    public String getName() {
        return getModID() + ":" + name;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "name='" + getName() +
                '}';
    }
}
