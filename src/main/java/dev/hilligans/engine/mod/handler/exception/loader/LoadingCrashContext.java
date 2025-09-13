package dev.hilligans.engine.mod.handler.exception.loader;

import dev.hilligans.engine.mod.handler.exception.IContext;

public class LoadingCrashContext implements IContext {

    public String mod;
    public String stage;

    public LoadingCrashContext(String mod, String stage) {
        this.mod = mod;
        this.stage = stage;
    }
}
