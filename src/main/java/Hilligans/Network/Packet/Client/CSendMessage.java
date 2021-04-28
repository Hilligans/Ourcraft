package Hilligans.Network.Packet.Client;

import Hilligans.Command.CommandHandler;
import Hilligans.Command.Commands;
import Hilligans.Network.Packet.Server.SChatMessage;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;

public class CSendMessage extends PacketBase {

    String message;

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
                    ServerNetworkHandler.sendPacket(new SChatMessage(commandHandler.handle(ServerNetworkHandler.getPlayerData(ctx).playerEntity,args1)));
                    return;
                }
            }
            System.out.println(name + ": " + message);
            ServerNetworkHandler.sendPacket(new SChatMessage(name + ": " + message));
        }
    }


}
