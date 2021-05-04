package Hilligans.Command.CommandTypes;

import Hilligans.Command.CommandHandler;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import Hilligans.Network.Packet.Server.SUpdatePlayer;
import Hilligans.Network.ServerNetworkHandler;
import org.lwjgl.glfw.GLFW;

public class TeleportCommand extends CommandHandler {

    public TeleportCommand(String command) {
        super(command);
    }

    @Override
    public String handle(Entity executor, String[] args) {
        if(args.length >= 4) {
            PlayerEntity playerEntity = ServerNetworkHandler.getPlayerEntity(args[0]);
            if(playerEntity != null) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                float z = Float.parseFloat(args[3]);
                playerEntity.x = x;
                playerEntity.y = y;
                playerEntity.z = z;
                ServerNetworkHandler.sendPacket(new SUpdateEntityPacket(x,y,z,playerEntity.pitch,playerEntity.yaw,playerEntity.id));
                ServerNetworkHandler.sendPacket(new SUpdatePlayer(x,y,z,playerEntity.pitch,playerEntity.yaw),playerEntity);
            } else {
                return "no player found with name " + args[0];
            }
        } else if(args.length == 3) {
            try {
                float x = Float.parseFloat(args[0]);
                float y = Float.parseFloat(args[1]);
                float z = Float.parseFloat(args[2]);
                executor.x = x;
                executor.y = y;
                executor.z = z;
                ServerNetworkHandler.sendPacket(new SUpdateEntityPacket(x,y,z,executor.pitch,executor.yaw,executor.id));

                if(executor instanceof PlayerEntity) {
                    ServerNetworkHandler.sendPacket(new SUpdatePlayer(x,y,z,executor.pitch,executor.yaw),(PlayerEntity)executor);
                }

            } catch (Exception ignored) {}
        }


        return "";
    }
}
