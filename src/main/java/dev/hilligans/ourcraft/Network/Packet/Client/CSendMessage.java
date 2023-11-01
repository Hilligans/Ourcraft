package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.hilligans.ourcraft.Command.CommandHandler;
import dev.hilligans.ourcraft.Command.Commands;
import dev.hilligans.ourcraft.Network.Packet.Server.SChatMessage;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.hilligans.ourcraft.ServerMain;

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
        packetData.writeUTF16(message);
    }

    @Override
    public void decode(PacketData packetData) {
        message = packetData.readUTF16();
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
