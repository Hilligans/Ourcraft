package dev.Hilligans.Ourcraft.Command.CommandTypes;

import dev.Hilligans.Ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.Ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.Hilligans.Ourcraft.Command.CommandHandler;
import dev.Hilligans.Ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Ourcraft.Network.Packet.Server.SSetGameMode;
import dev.Hilligans.Ourcraft.ServerMain;

public class GameModeCommand extends CommandHandler {
    public GameModeCommand(String command) {
        super(command);
    }

    @Override
    public String handle(CommandExecutor executor, String[] args) {
        if(args.length >= 2) {
            String gamemode = args[0];
        } else if(args.length == 1) {
            String gamemode = args[0];
            if(gamemode.equals("creative") || gamemode.equals("1")) {
                if(executor instanceof EntityExecutor && ((EntityExecutor) executor).entity instanceof PlayerEntity) {
                    ((PlayerEntity)((EntityExecutor) executor).entity).getPlayerData().isCreative = true;
                    ServerMain.getServer().sendPacket(new SSetGameMode(1),((PlayerEntity)((EntityExecutor) executor).entity));
                }
                return "";
            } else if(gamemode.equals("survival") || gamemode.equals("0")) {
                if(executor instanceof EntityExecutor && ((EntityExecutor) executor).entity instanceof PlayerEntity) {
                    ((PlayerEntity)((EntityExecutor) executor).entity).getPlayerData().isCreative = false;
                    ServerMain.getServer().sendPacket(new SSetGameMode(0),((PlayerEntity)((EntityExecutor) executor).entity));
                }
                return "";
            }
            return "Invalid gamemode";
        }
        return "Missing arguments";
    }


}
