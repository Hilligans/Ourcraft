package Hilligans.Network.Packet.Client;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Camera;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.*;
import Hilligans.Server.PlayerData;
import Hilligans.World.Chunk;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.Util.Settings;
import Hilligans.World.ServerWorld;
import io.netty.channel.ChannelId;

import java.awt.image.BufferedImage;


public class  CHandshakePacket extends PacketBase {

    public int id;
    public String name;

    public CHandshakePacket() {
        super(5);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(Settings.gameVersion);
        packetData.writeString(ClientMain.name);
      //  packetData.writeString("testabc");
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readInt();
        name = packetData.readString();
      //  test = packetData.readString();
    }

    @Override
    public void handle() {
        if(id == Settings.gameVersion) {
            int playerId = Entity.getNewId();
            ServerNetworkHandler.sendPacket(new SHandshakePacket(playerId),ctx);
            ServerNetworkHandler.sendPacket(new SChatMessage(name + " has joined the game"));
            for(Entity entity : ServerMain.world.entities.values()) {
                ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity),ctx);
            }
            ChannelId channelId = ServerNetworkHandler.nameToChannel.get(name);
            BlockPos spawn = ServerMain.world.getWorldSpawn(Settings.playerBoundingBox);
            PlayerEntity playerEntity = new PlayerEntity(spawn.x,spawn.y,spawn.z,playerId);
            PlayerData playerData = new PlayerData(playerEntity);
            ServerNetworkHandler.playerData.put(playerId,playerData);
            ServerNetworkHandler.mappedChannels.put(playerId,ctx.channel().id());
            ServerNetworkHandler.mappedId.put(ctx.channel().id(),playerId);
            ServerNetworkHandler.mappedName.put(ctx.channel().id(),name);
            ServerNetworkHandler.nameToChannel.put(name, ctx.channel().id());
            ServerMain.world.addEntity(playerEntity);
            ServerNetworkHandler.sendPacket(new SUpdatePlayer(spawn.x,spawn.y,spawn.z,0,0),ctx);
            playerData.playerInventory.age++;

            //todo make this data easier for the server to send to clients

            for(Block block : Blocks.serverBlocks) {
                BlockTextureManager blockTextureManager = block.blockProperties.blockTextureManager;
                BufferedImage bufferedImage = WorldTextureManager.loadImage("/Blocks/" + blockTextureManager.location);
                ServerNetworkHandler.sendPacket(new SCreateTexture(bufferedImage,blockTextureManager.location.substring(0,blockTextureManager.location.length() - 4),true),ctx);
                for(int x = 0; x < 6; x++) {
                    if(blockTextureManager.textureNames != null) {
                        if (blockTextureManager.textureNames[x] != null) {
                            BufferedImage bufferedImage1 = WorldTextureManager.loadImage("/Blocks/" + blockTextureManager.textureNames[x]);
                            ServerNetworkHandler.sendPacket(new SCreateTexture(bufferedImage1, blockTextureManager.textureNames[x].substring(0, blockTextureManager.textureNames[x].length() - 4), true), ctx);
                        }
                    }
                }
            }

            for(Block block: Blocks.serverBlocks) {
                ServerNetworkHandler.sendPacket(new SRegisterBlock(block),ctx);
            }

            ServerNetworkHandler.sendPacket(new SUpdateInventory(playerData.playerInventory),ctx);

        } else {
            ctx.channel().close();
        }
    }
}
