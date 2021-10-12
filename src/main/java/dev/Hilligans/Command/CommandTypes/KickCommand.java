package dev.Hilligans.Command.CommandTypes;

import dev.Hilligans.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.Command.CommandHandler;
import dev.Hilligans.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Network.ServerNetworkHandler;

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
