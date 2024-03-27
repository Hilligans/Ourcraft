package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.network.packet.auth.*;
import dev.hilligans.ourcraft.network.packet.client.*;
import dev.hilligans.ourcraft.network.packet.server.*;

public class Protocols {

    public static void register(ModContainer modContent) {
        modContent.registerPacket(CRequestChunkPacket::new);
        modContent.registerPacket(SSendChunkPacket::new);
        modContent.registerPacket(CSendBlockChanges::new);
        modContent.registerPacket(SSendBlockChanges::new);
        modContent.registerPacket(CHandshakePacket::new);
        modContent.registerPacket(SHandshakePacket::new);
        modContent.registerPacket(CUpdatePlayerPacket::new);
        modContent.registerPacket(SUpdateEntityPacket::new);
        modContent.registerPacket(SCreateEntityPacket::new);
        modContent.registerPacket(SRemoveEntityPacket::new);
        modContent.registerPacket(CSendMessage::new);
        modContent.registerPacket(SChatMessage::new);
        modContent.registerPacket(CModifyStack::new);
        modContent.registerPacket(SUpdateContainer::new);
        modContent.registerPacket(SOpenContainer::new);
        modContent.registerPacket(SUpdateInventory::new);
        modContent.registerPacket(CDropItem::new);
        modContent.registerPacket(CUseItem::new);
        modContent.registerPacket(CCloseScreen::new);
        modContent.registerPacket(SUpdatePlayer::new);
        modContent.registerPacket(SDisconnectPacket::new);
        modContent.registerPacket(COpenScreen::new);
        modContent.registerPacket(SSendPlayerList::new);
        modContent.registerPacket(SSetGameMode::new);
        modContent.registerPacket(CRequestContent::new);

        modContent.registerPacket("ourcraft:Auth", SAccountPacket::new);
        modContent.registerPacket("ourcraft:Auth", SSendToken::new);
        modContent.registerPacket("ourcraft:Auth", STokenValid::new);
        modContent.registerPacket("ourcraft:Auth", SSendLoginToken::new);

        modContent.registerPacket("ourcraft:Auth", CCreateAccount::new);
        modContent.registerPacket("ourcraft:Auth", CGetToken::new);
        modContent.registerPacket("ourcraft:Auth", CLogin::new);
    }
}
