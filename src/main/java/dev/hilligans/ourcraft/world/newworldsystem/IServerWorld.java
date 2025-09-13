package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.network.packet.SSendChunkPacket;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.server.concurrent.ChunkLocker;

import java.util.function.Consumer;

public interface IServerWorld extends IWorld {

    BlockPos getWorldSpawn(BoundingBox boundingBox);

    void setServer(IServer server);

    IServer getServer();

    void queuePostTickEvent(Consumer<IServerWorld> consumer);


    void processPostTickEvents();

    default void sendChunksToPlayer(int playerX, int playerY, int playerZ, ServerPlayerData serverPlayerData) {
        int chunkWidth = getChunkContainer().getChunkWidth();
        int chunkHeight = getChunkContainer().getChunkWidth();

        int chunkX = playerX / chunkWidth;
        int chunkY = playerY / chunkHeight;
        int chunkZ = playerZ / chunkWidth;

        if(serverPlayerData.lastX != chunkX && serverPlayerData.lastY != chunkY && serverPlayerData.lastZ != chunkZ) {
            playerX = chunkX * chunkWidth;
            playerY = chunkY * chunkHeight;
            playerZ = chunkZ * chunkWidth;

            int blockWidth = serverPlayerData.renderDistance * getChunkContainer().getChunkWidth();
            int blockHeight = serverPlayerData.renderYDistance * getChunkContainer().getChunkHeight();
            BoundingBox boundingBox = new BoundingBox(playerX - blockWidth, playerY - blockHeight, playerZ - blockWidth, playerX + blockWidth, playerY + blockHeight, playerZ + blockWidth);
            BoundingBox old = new BoundingBox(serverPlayerData.lastX * chunkWidth - blockWidth, serverPlayerData.lastX * chunkWidth - blockWidth, serverPlayerData.lastY * chunkHeight - blockHeight, serverPlayerData.lastZ * chunkWidth - blockWidth, serverPlayerData.lastX * chunkWidth + blockWidth, serverPlayerData.lastY * chunkHeight + blockHeight, serverPlayerData.lastZ * chunkWidth + blockWidth);

            for(int x = -serverPlayerData.renderDistance; x <= serverPlayerData.renderDistance; x++) {
                for(int y = -serverPlayerData.renderDistance; y <= serverPlayerData.renderDistance; y++) {
                    for(int z = -serverPlayerData.renderDistance; z <= serverPlayerData.renderDistance; z++) {
                        IChunk chunk = getChunk((long) (x + chunkX) * chunkWidth, (long) (y + chunkY) * chunkHeight, (long) (z + chunkZ) * chunkWidth);
                        if(chunk != null) {
                            if (boundingBox.intersects(chunk) && !old.intersects(chunk)) {
                                SSendChunkPacket.send(serverPlayerData.networkEntity, chunk);
                            }
                        }
                    }
                }
            }

            serverPlayerData.lastX = chunkX;
            serverPlayerData.lastY = chunkY;
            serverPlayerData.lastZ = chunkZ;
        }
    }

    default void generateWorld() {}

    IWorldGenerator getWorldGenerator();

    ChunkLocker getChunkLocker();
}
