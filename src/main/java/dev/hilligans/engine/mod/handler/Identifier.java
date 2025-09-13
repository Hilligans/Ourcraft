package dev.hilligans.engine.mod.handler;

public class Identifier {

    public String name;

    public String modID;
    public ModID modId;

    public Identifier(String name, String modID) {
        this.name = name;
        this.modID = modID;
    }

    public Identifier(String name, ModID modID) {
        this.name = name;
        this.modId = modID;
    }

    public String getModID() {
        if(modID == null) {
            return modId.getModID();
        }
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
