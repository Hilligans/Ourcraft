package Hilligans.Command.CommandTypes;

import Hilligans.Command.CommandExecutors.CommandExecutor;
import Hilligans.Command.CommandExecutors.EntityExecutor;
import Hilligans.Command.CommandHandler;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.ServerNetworkHandler;

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
