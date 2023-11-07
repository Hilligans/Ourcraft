package dev.hilligans.ourcraft.command.types;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.command.executors.EntityExecutor;
import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.packet.server.SSetGameMode;
import dev.hilligans.ourcraft.server.IServer;

public class GameModeCommand extends CommandHandler {
    public GameModeCommand(String command) {
        super(command);
    }

    @Override
    public String handle(CommandExecutor executor, String[] args) {
        IServer server = executor.getServer();
        if(args.length >= 2) {
            String gamemode = args[0];
        } else if(args.length == 1) {
            String gamemode = args[0];
            if(gamemode.equals("creative") || gamemode.equals("1")) {
                if(executor instanceof EntityExecutor && ((EntityExecutor) executor).entity instanceof PlayerEntity) {
                    ((PlayerEntity)((EntityExecutor) executor).entity).getPlayerData().isCreative = true;
                    server.sendPacket(new SSetGameMode(1),((PlayerEntity)((EntityExecutor) executor).entity));
                }
                return "";
            } else if(gamemode.equals("survival") || gamemode.equals("0")) {
                if(executor instanceof EntityExecutor && ((EntityExecutor) executor).entity instanceof PlayerEntity) {
                    ((PlayerEntity)((EntityExecutor) executor).entity).getPlayerData().isCreative = false;
                    server.sendPacket(new SSetGameMode(0),((PlayerEntity)((EntityExecutor) executor).entity));
                }
                return "";
            }
            return "Invalid gamemode";
        }
        return "Missing arguments";
    }


}
