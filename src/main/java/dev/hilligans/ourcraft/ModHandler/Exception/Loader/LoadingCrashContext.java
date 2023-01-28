package dev.hilligans.ourcraft.ModHandler.Exception.Loader;

import dev.hilligans.ourcraft.ModHandler.Exception.IContext;

public class LoadingCrashContext implements IContext {

    public String mod;
    public String stage;

    public LoadingCrashContext(String mod, String stage) {
        this.mod = mod;
        this.stage = stage;
    }
}
