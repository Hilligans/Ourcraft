package dev.hilligans.engine3d;

import dev.hilligans.engine.mod.ModClass;
import dev.hilligans.engine.mod.content.CoreExtensionView;
import dev.hilligans.engine3d.client.MeshRenderer;

public class Engine3D implements ModClass {
    @Override
    public String getModID() {
        return "engine3D";
    }

    @Override
    public void registerCoreExtensions(CoreExtensionView view) {
        if (view.getGameInstance().getSide().isClient()) {
            view.registerRenderTask(new MeshRenderer());
        }
    }
}
