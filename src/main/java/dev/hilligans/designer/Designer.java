package dev.hilligans.designer;

import dev.hilligans.engine.mod.ModClass;

public class Designer implements ModClass {

    public static final String NAME = "designer";

    @Override
    public String getModID() {
        return NAME;
    }
}
