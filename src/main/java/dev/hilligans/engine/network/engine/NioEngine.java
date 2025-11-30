package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.Engine;

public class NioEngine { //extends NetworkEngine {
    //@Override
    public String getResourceName() {
        return "nioEngine";
    }

    //@Override
    public String getResourceOwner() {
        return Engine.ENGINE_NAME;
    }
}
