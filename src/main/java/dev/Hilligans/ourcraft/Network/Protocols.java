package dev.Hilligans.ourcraft.Network;

import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Network.Packet.AuthServerPackets.*;
import dev.Hilligans.ourcraft.Network.Packet.Client.*;
import dev.Hilligans.ourcraft.Network.Packet.Server.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Protocols {

    public static final ArrayList<Protocol> PROTOCOLS = new ArrayList<>();
    public static final HashMap<String, Protocol> MAPPED_PROTOCOLS = new HashMap<>();

    public static final Protocol PLAY = new Protocol("Play");
    public static final Protocol AUTH = new Protocol("Auth");

    public static String a = "";

    public static void register(Protocol protocol) {
        if(MAPPED_PROTOCOLS.containsKey(protocol.protocolName)) {
            MAPPED_PROTOCOLS.get(protocol.protocolName).mergeProtocols(protocol);
        } else {
            PROTOCOLS.add(protocol);
            MAPPED_PROTOCOLS.put(protocol.protocolName, protocol);
        }
    }

    public static void clear() {
        for(Protocol protocol : PROTOCOLS) {
            protocol.clear();
        }
    }

    static {
        register(PLAY);
        register(AUTH);

        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CRequestChunkPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SSendChunkPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CSendBlockChanges::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SSendBlockChanges::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CHandshakePacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SHandshakePacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CUpdatePlayerPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SUpdateEntityPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SCreateEntityPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SRemoveEntityPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CSendMessage::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SChatMessage::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CModifyStack::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SUpdateContainer::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CActivateBlock::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SOpenContainer::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SUpdateInventory::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CDropItem::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CUseItem::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CCloseScreen::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SUpdatePlayer::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SDisconnectPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(COpenScreen::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SSendPlayerList::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SSetGameMode::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(SSendModContentPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket(CRequestContent::new);

        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", SAccountPacket::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", SSendToken::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", STokenValid::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", SSendLoginToken::new);

        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", CCreateAccount::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", CGetToken::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", CTokenValid::new);
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerPacket("Auth", CLogin::new);
    }

}
