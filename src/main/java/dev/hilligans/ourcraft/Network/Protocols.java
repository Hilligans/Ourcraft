package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Network.Packet.AuthServerPackets.*;
import dev.hilligans.ourcraft.Network.Packet.Client.*;
import dev.hilligans.ourcraft.Network.Packet.Server.*;

public class Protocols {

    public static void register(ModContent modContent) {

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
        modContent.registerPacket(CActivateBlock::new);
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
        modContent.registerPacket(SSendModContentPacket::new);
        modContent.registerPacket(CRequestContent::new);

        modContent.registerPacket("Auth", SAccountPacket::new);
        modContent.registerPacket("Auth", SSendToken::new);
        modContent.registerPacket("Auth", STokenValid::new);
        modContent.registerPacket("Auth", SSendLoginToken::new);

        modContent.registerPacket("Auth", CCreateAccount::new);
        modContent.registerPacket("Auth", CGetToken::new);
        modContent.registerPacket("Auth", CTokenValid::new);
        modContent.registerPacket("Auth", CLogin::new);
    }

}
