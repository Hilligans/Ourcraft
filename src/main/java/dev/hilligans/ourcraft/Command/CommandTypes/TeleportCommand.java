package dev.hilligans.ourcraft.Command.CommandTypes;

import dev.hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.hilligans.ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.hilligans.ourcraft.Command.CommandHandler;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Network.Packet.Server.SUpdateEntityPacket;
import dev.hilligans.ourcraft.Network.Packet.Server.SUpdatePlayer;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.hilligans.ourcraft.ServerMain;

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
                playerEntity.setPos(x,y,z);
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
                    entity.setPos(x,y,z);
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
