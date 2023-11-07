package dev.hilligans.ourcraft.command.types;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;

public class KickCommand extends CommandHandler {


    public KickCommand(String command) {
        super(command);
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        PlayerEntity playerEntity = executor.getServer().getServerNetworkHandler().getPlayerEntity(args[0]);
        if(playerEntity != null) {
          //  playerEntity.
        }

        return "";
    }
}
