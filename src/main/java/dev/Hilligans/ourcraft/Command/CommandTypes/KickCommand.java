package dev.Hilligans.ourcraft.Command.CommandTypes;

import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Command.CommandHandler;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.Network.ServerNetworkHandler;

public class KickCommand extends CommandHandler {


    public KickCommand(String command) {
        super(command);
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        PlayerEntity playerEntity = ServerNetworkHandler.getPlayerEntity(args[0]);
        if(playerEntity != null) {
          //  playerEntity.
        }

        return "";
    }
}
