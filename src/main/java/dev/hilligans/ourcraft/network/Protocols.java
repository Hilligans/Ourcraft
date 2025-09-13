package dev.hilligans.ourcraft.network;

import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.network.packet.*;

public class Protocols {

    public static void register(ModContainer modContent) {
        modContent.registerPacket("ourcraft:login", SServerExceptionPacket.instance);
        modContent.registerPacket("ourcraft:login", SSwitchProtocol.instance);

        modContent.registerPacket("ourcraft:login", CPing.instance);
        modContent.registerPacket("ourcraft:login", SPing.instance);
        modContent.registerPacket("ourcraft:login", CGetServerInfo.instance);
        modContent.registerPacket("ourcraft:login", SSendServerInfo.instance);
        modContent.registerPacket("ourcraft:login", CLogin.instance);
        modContent.registerPacket("ourcraft:login", SRejectClient.instance);
        modContent.registerPacket("ourcraft:login", SRejectAuthentication.instance);


        modContent.registerPacket("ourcraft:Play", SServerExceptionPacket.instance);
        modContent.registerPacket("ourcraft:Play", SSwitchProtocol.instance);

        modContent.registerPacket("ourcraft:Play", SSendChunkPacket.instance);
        modContent.registerPacket("ourcraft:Play", CSendMessage.instance);
        modContent.registerPacket("ourcraft:Play", SSendMessage.instance);
        modContent.registerPacket("ourcraft:Play", CSendCommand.instance);
    }
}
