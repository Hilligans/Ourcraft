package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.command.executors.EntityExecutor;
import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.command.Commands;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.network.packet.server.SChatMessage;

public class CSendMessage extends PacketBase<IServerPacketHandler> {

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
