package dev.Hilligans.Command.CommandTypes;

import dev.Hilligans.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.Command.CommandExecutors.EntityExecutor;
import dev.Hilligans.Command.CommandHandler;
import dev.Hilligans.Entity.Entity;
import dev.Hilligans.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import dev.Hilligans.Network.Packet.Server.SUpdatePlayer;
import dev.Hilligans.Network.ServerNetworkHandler;
import dev.Hilligans.ServerMain;

public class TeleportCommand extends CommandHandler {

    public TeleportCommand(String command) {
        super(command);
    }

    @Override
    public String handle(CommandExecutor executor, String[] args) {
        if(args.length >= 4) {
            PlayerEntity playerEntity = ServerNetworkHandler.getPlayerEntity(args[0]);
            if(playerEntity != null) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                float z = Float.parseFloat(args[3]);
                playerEntity.x = x;
                playerEntity.y = y;
                playerEntity.z = z;
                ServerMain.getServer().sendPacket(new SUpdateEntityPacket(x,y,z,playerEntity.pitch,playerEntity.yaw,playerEntity.id));
                ServerMain.getServer().sendPacket(new SUpdatePlayer(x,y,z,playerEntity.pitch,playerEntity.yaw),playerEntity);
            } else {
                return "no player found with name " + args[0];
            }
        } else if(args.length == 3) {
            try {
                if(executor instanceof EntityExecutor) {
                    Entity entity = ((EntityExecutor) executor).entity;
                    float x = Float.parseFloat(args[0]);
                    float y = Float.parseFloat(args[1]);
                    float z = Float.parseFloat(args[2]);
                    entity.x = x;
                    entity.y = y;
                    entity.z = z;
                    ServerMain.getServer().sendPacket(new SUpdateEntityPacket(x, y, z, entity.pitch, entity.yaw, entity.id));

                    if (entity instanceof PlayerEntity) {
                        ServerMain.getServer().sendPacket(new SUpdatePlayer(x, y, z, entity.pitch, entity.yaw), (PlayerEntity) entity);
                    }
                }

            } catch (Exception ignored) {}
        }


        return "";
    }
}