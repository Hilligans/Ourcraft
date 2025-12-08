package dev.hilligans.engine.mod.content;

public class ModDependency {

    public String modID = "";
    public int minVersion = -1;
    public int maxVersion = -1;


    public ModDependency(String modID) {
        this.modID = modID;
    }

    public ModDependency(String modID, int minVersion) {
        this(modID);
        this.minVersion = minVersion;
    }

    public ModDependency(String modID, int minVersion, int maxVersion) {
        this(modID,minVersion);
        this.maxVersion = maxVersion;
    }
}
