package dev.hilligans.ourcraft.command.types;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.command.executors.EntityExecutor;
import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.entity.Entity;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.packet.server.SUpdateEntityPacket;
import dev.hilligans.ourcraft.network.packet.server.SUpdatePlayer;
import dev.hilligans.ourcraft.server.IServer;

public class TeleportCommand extends CommandHandler {

    public TeleportCommand(String command) {
        super(command);
    }

    @Override
    public String handle(CommandExecutor executor, String[] args) {
        IServer server = executor.getServer();
        if(args.length >= 4) {
            PlayerEntity playerEntity = executor.getServer().getServerNetworkHandler().getPlayerEntity(args[0]);
            if(playerEntity != null) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                float z = Float.parseFloat(args[3]);
                playerEntity.setPos(x,y,z);
                server.sendPacket(new SUpdateEntityPacket(x,y,z,playerEntity.pitch,playerEntity.yaw,playerEntity.id));
                server.sendPacket(new SUpdatePlayer(x,y,z,playerEntity.pitch,playerEntity.yaw),playerEntity);
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
                    server.sendPacket(new SUpdateEntityPacket(x, y, z, entity.pitch, entity.yaw, entity.id));

                    if (entity instanceof PlayerEntity) {
                        server.sendPacket(new SUpdatePlayer(x, y, z, entity.pitch, entity.yaw), (PlayerEntity) entity);
                    }
                }

            } catch (Exception ignored) {}
        }
        return "";
    }
}
