package Hilligans.Command.CommandTypes;

import Hilligans.Command.CommandHandler;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SSetGameMode;
import Hilligans.Network.ServerNetworkHandler;

public class GameModeCommand extends CommandHandler {
    public GameModeCommand(String command) {
        super(command);
    }

    @Override
    public String handle(Entity executor, String[] args) {
        if(args.length >= 2) {
            String gamemode = args[0];
        } else if(args.length == 1) {
            String gamemode = args[0];
            if(gamemode.equals("creative") || gamemode.equals("1")) {
                if(executor instanceof PlayerEntity) {
                    ((PlayerEntity) executor).getPlayerData().isCreative = true;
                    ServerNetworkHandler.sendPacket(new SSetGameMode(1),(PlayerEntity)executor);
                }
                return "";
            } else if(gamemode.equals("survival") || gamemode.equals("0")) {
                if(executor instanceof PlayerEntity) {
                    ((PlayerEntity) executor).getPlayerData().isCreative = false;
                    ServerNetworkHandler.sendPacket(new SSetGameMode(0),(PlayerEntity)executor);
                }
                return "";
            }
            return "Invalid gamemode";
        }
        return "Missing arguments";
    }


}
