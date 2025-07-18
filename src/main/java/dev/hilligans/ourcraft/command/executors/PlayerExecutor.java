package dev.hilligans.ourcraft.command.executors;

import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public class PlayerExecutor implements CommandExecutor {

    ServerPlayerData playerData;

    public PlayerExecutor(ServerPlayerData playerData) {
        this.playerData = playerData;
    }

    public IWorld getWorld() {
        return playerData.getWorld();
    }

    public ServerPlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public void sendMessage(String message) {

    }
}
