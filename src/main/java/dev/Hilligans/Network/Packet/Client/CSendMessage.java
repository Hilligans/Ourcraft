package dev.Hilligans.Network.Packet.Client;

import dev.Hilligans.Command.CommandExecutors.EntityExecutor;
import dev.Hilligans.Command.CommandHandler;
import dev.Hilligans.Command.Commands;
import dev.Hilligans.Network.Packet.Server.SChatMessage;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;
import dev.Hilligans.Network.ServerNetworkHandler;
import dev.Hilligans.ServerMain;

public class CSendMessage extends PacketBase {

    public String message;

    public CSendMessage() {
        super(11);
    }

    public CSendMessage(String message) {
        this();
        this.message = message;
    }


    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(message);
    }

    @Override
    public void decode(PacketData packetData) {
        message = packetData.readString();
    }

    @Override
    public void handle() {
        if(!message.equals("")) {
            String name = ServerNetworkHandler.mappedName.get(ctx.channel().id());
            String[] args = message.split(" ");
            if (args.length != 0) {
                CommandHandler commandHandler = Commands.commands.get(args[0]);
                if (commandHandler != null) {
                    String[] args1 = new String[args.length - 1];
                    System.arraycopy(args,1,args1,0,args1.length);
                    ServerMain.getServer().sendPacket(new SChatMessage((String) commandHandler.handle(new EntityExecutor(ServerNetworkHandler.getPlayerData(ctx).playerEntity),args1)));
                    return;
                }
            }
            System.out.println(name + ": " + message);
            ServerMain.getServer().sendPacket(new SChatMessage(name + ": " + message));
        }
    }


}
