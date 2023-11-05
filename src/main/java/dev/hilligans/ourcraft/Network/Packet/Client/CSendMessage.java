package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.hilligans.ourcraft.Command.CommandHandler;
import dev.hilligans.ourcraft.Command.Commands;
import dev.hilligans.ourcraft.Network.*;
import dev.hilligans.ourcraft.Network.Packet.Server.SChatMessage;
import dev.hilligans.ourcraft.ServerMain;

public class CSendMessage extends PacketBaseNew<IServerPacketHandler> {

    public String message;

    public CSendMessage() {
        super(11);
    }

    public CSendMessage(String message) {
        this();
        this.message = message;
    }


    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeUTF16(message);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        message = packetData.readUTF16();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        if(!message.equals("")) {
            String name = serverPacketHandler.getServerPlayerData().getPlayerName();
            String[] args = message.split(" ");
            if (args.length != 0) {
                CommandHandler commandHandler = Commands.commands.get(args[0]);
                if (commandHandler != null) {
                    String[] args1 = new String[args.length - 1];
                    System.arraycopy(args,1,args1,0,args1.length);
                    serverPacketHandler.sendPacket(new SChatMessage((String) commandHandler.handle(new EntityExecutor(serverPacketHandler.getPlayerEntity()),args1)));
                    return;
                }
            }
            System.out.println(name + ": " + message);
            serverPacketHandler.sendPacket(new SChatMessage(name + ": " + message));
        }
    }


}
