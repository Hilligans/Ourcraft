package dev.hilligans.ourcraft.Command.CommandTypes;

import dev.hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.hilligans.ourcraft.Command.CommandHandler;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;

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
