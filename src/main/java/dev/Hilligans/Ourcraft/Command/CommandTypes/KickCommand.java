package dev.Hilligans.Ourcraft.Command.CommandTypes;

import dev.Hilligans.Ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.Ourcraft.Command.CommandHandler;
import dev.Hilligans.Ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Ourcraft.Network.ServerNetworkHandler;

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
