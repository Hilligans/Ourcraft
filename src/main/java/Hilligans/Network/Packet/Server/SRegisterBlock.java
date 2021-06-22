package Hilligans.Network.Packet.Server;

import Hilligans.Block.Block;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SRegisterBlock extends PacketBase {

    Block block;
    byte stateSize;
    short type;

    public SRegisterBlock() {
        super(23);
    }

    public SRegisterBlock(Block block) {
        this();
        this.block = block;
        this.stateSize = (byte) block.blockStateByteCount();
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(block.name);
        packetData.writeShort(type);
        packetData.writeByte(stateSize);
        packetData.writeString(block.blockProperties.blockTextureManager.location);
        if(block.blockProperties.blockTextureManager.textureNames != null) {
            for(int x = 0; x < 6; x++) {
                if(block.blockProperties.blockTextureManager.textureNames[x] != null) {
                    packetData.writeString(block.blockProperties.blockTextureManager.textureNames[x]);
                } else {
                    packetData.writeString("");
                }
            }
        }
    }

    @Override
    public void decode(PacketData packetData) {
        String name = packetData.readString();
        //System.out.println(name);
        BlockProperties blockProperties = new BlockProperties().serverSide();
        Block block;
        switch (packetData.readShort()) {
            default:
                block = new Block(name, blockProperties);
                break;
            case 1:
                block = new Block(name,blockProperties);
                break;
        }
        block.blockProperties.blockStateSize = packetData.readByte();
        String textureName = packetData.readString();
        blockProperties.withTexture(textureName);
        ClientMain.getClient().refreshTexture = true;
    }

    @Override
    public void handle() {

    }
}
