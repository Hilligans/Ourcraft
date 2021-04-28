package Hilligans.Command.CommandTypes;

import Hilligans.Command.CommandHandler;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import Hilligans.Network.Packet.Server.SUpdatePlayer;
import Hilligans.Network.ServerNetworkHandler;

public class TeleportCommand extends CommandHandler {

    public TeleportCommand(String command) {
        super(command);
    }

    @Override
    public String handle(Entity executor, String[] args) {
        if(args.length >= 3) {
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
