package Hilligans.ModHandler.Content;

import Hilligans.Util.Settings;

public class ModDependency {

    public String modID = "";
    public int gameVersion = Settings.gameVersion;
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
