package dev.hilligans.planets;

import dev.hilligans.ourcraft.mod.handler.ModClass;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;

public class Planets extends ModClass {

    @Override
    public void registerContent(ModContainer container) {
        System.out.println("Hello from planets!");
    }

    @Override
    public String getModID() {
        return "planets";
    }
}
