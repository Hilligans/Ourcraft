package Hilligans.Network;

import Hilligans.Network.Packet.AuthServerPackets.*;
import Hilligans.Ourcraft;
import Hilligans.Network.Packet.Client.*;
import Hilligans.Network.Packet.Server.*;

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

        Ourcraft.OURCRAFT.registerPacket(CRequestChunkPacket::new);
        Ourcraft.OURCRAFT.registerPacket(SSendChunkPacket::new);
        Ourcraft.OURCRAFT.registerPacket(CSendBlockChanges::new);
        Ourcraft.OURCRAFT.registerPacket(SSendBlockChanges::new);
        Ourcraft.OURCRAFT.registerPacket(CHandshakePacket::new);
        Ourcraft.OURCRAFT.registerPacket(SHandshakePacket::new);
        Ourcraft.OURCRAFT.registerPacket(CUpdatePlayerPacket::new);
        Ourcraft.OURCRAFT.registerPacket(SUpdateEntityPacket::new);
        Ourcraft.OURCRAFT.registerPacket(SCreateEntityPacket::new);
        Ourcraft.OURCRAFT.registerPacket(SRemoveEntityPacket::new);
        Ourcraft.OURCRAFT.registerPacket(CSendMessage::new);
        Ourcraft.OURCRAFT.registerPacket(SChatMessage::new);
        Ourcraft.OURCRAFT.registerPacket(CModifyStack::new);
        Ourcraft.OURCRAFT.registerPacket(SUpdateContainer::new);
        Ourcraft.OURCRAFT.registerPacket(CActivateBlock::new);
        Ourcraft.OURCRAFT.registerPacket(SOpenContainer::new);
        Ourcraft.OURCRAFT.registerPacket(SUpdateInventory::new);
        Ourcraft.OURCRAFT.registerPacket(CDropItem::new);
        Ourcraft.OURCRAFT.registerPacket(CUseItem::new);
        Ourcraft.OURCRAFT.registerPacket(CCloseScreen::new);
        Ourcraft.OURCRAFT.registerPacket(SUpdatePlayer::new);
        Ourcraft.OURCRAFT.registerPacket(SDisconnectPacket::new);
        Ourcraft.OURCRAFT.registerPacket(COpenScreen::new);
        Ourcraft.OURCRAFT.registerPacket(SSendPlayerList::new);
        Ourcraft.OURCRAFT.registerPacket(SSetGameMode::new);
        Ourcraft.OURCRAFT.registerPacket(SSendModContentPacket::new);
        Ourcraft.OURCRAFT.registerPacket(CRequestContent::new);

        Ourcraft.OURCRAFT.registerPacket("Auth", SAccountPacket::new);
        Ourcraft.OURCRAFT.registerPacket("Auth", SSendToken::new);
        Ourcraft.OURCRAFT.registerPacket("Auth", STokenValid::new);
        Ourcraft.OURCRAFT.registerPacket("Auth", SSendLoginToken::new);

        Ourcraft.OURCRAFT.registerPacket("Auth", CCreateAccount::new);
        Ourcraft.OURCRAFT.registerPacket("Auth", CGetToken::new);
        Ourcraft.OURCRAFT.registerPacket("Auth", CTokenValid::new);
        Ourcraft.OURCRAFT.registerPacket("Auth", CLogin::new);
    }

}
