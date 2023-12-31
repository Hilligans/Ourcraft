package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.GameInstance;

public class ClientCubicWorld extends CubicWorld {
    public ClientCubicWorld(GameInstance gameInstance, int id, String worldName, int radius) {
        super(gameInstance, id, worldName, radius);
    }

    @Override
    public void tick() {
        chunkContainer.forEach(iChunk -> {
            if(iChunk.isDirty()) {
                iChunk.setDirty(false);
            }
        });
    }
}
