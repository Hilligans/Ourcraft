package Hilligans.Network.Packet.Server;

import Hilligans.Block.Block;
import Hilligans.Block.BlockTypes.SlabBlock;
import Hilligans.Block.Blocks;
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
        if(block instanceof SlabBlock) {
            type = 1;
        }
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
        System.out.println(name);
        BlockProperties blockProperties = new BlockProperties().serverSide();
        Block block;
        switch (packetData.readShort()) {
            default:
                block = new Block(name, blockProperties);
                break;
            case 1:
                block = new SlabBlock(name,blockProperties);
                break;
        }
        packetData.readByte();
        String textureName = packetData.readString();
        System.out.println(textureName);
        blockProperties.withTexture(textureName);
        for(int x = 0; x < 6; x++) {
            //String tex = packetData.readString();
            //if(!tex.equals(" ")) {
            //    blockProperties.withSidedTexture(tex,x);
            //}
        }

        ClientMain.refreshTexture = true;

        //block.generateTextures();
       // stateSize = packetData.readByte();
    }

    @Override
    public void handle() {

    }
}
