package Hilligans.World;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.ChunkPos;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.Entity;
import Hilligans.Network.Packet.Server.SCreateEntityPacket;
import Hilligans.Network.Packet.Server.SRemoveEntityPacket;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Server.MultiPlayerServer;
import Hilligans.Util.Settings;
import Hilligans.WorldSave.ChunkLoader;

import java.util.ArrayList;

public class ServerWorld extends World {

    ArrayList<Integer> entityRemovals = new ArrayList<>();
    long autoSaveAfter = 30 * 1000;
    long autoSave = -1;
    public MultiPlayerServer multiPlayerServer;

    @Override
    public void addEntity(Entity entity) {
        entities.put(entity.id,entity);
        ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity));
    }

    @Override
    public void removeEntity(int id) {
        entityRemovals.add(id);
    }

    private void handleRemove() {
        for(Integer integer : entityRemovals) {
            entities.remove((int)integer);
            ServerNetworkHandler.sendPacket(new SRemoveEntityPacket(integer));
        }
    }


    @Override
    public void generateChunk(int x, int z) {
        if(getChunk(x,z) == null) {
            Chunk chunk = ChunkLoader.readChunk(x,z);
            if(chunk != null) {
                setChunk(chunk,x,z);
                return;
            }
        }
        super.generateChunk(x, z);
    }

    @Override
    public void unloadChunk(int x, int z) {
        Chunk chunk = getChunk(x,z);
        if(chunk != null) {
            ChunkLoader.writeChunk(chunk);
            ChunkLoader.finishSave();
        }
        super.unloadChunk(x,z);
    }

    @Override
    public void tick() {
        int x = 0;
        handleRemove();
        for(Entity entity : entities.values()) {
            try {
                entity.tick();
            } catch (Exception e) {
                e.printStackTrace();
            }
            x++;
        }
        for(Chunk chunk : chunks.values()) {
            chunk.tick();
        }
        if(autoSave == -1) {
            autoSave = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - autoSave > autoSaveAfter) {
            try {
                autoSave = System.currentTimeMillis();
                long start = System.currentTimeMillis();
                ArrayList<Long> players = new ArrayList<>();
                for(ServerPlayerData playerData : ServerNetworkHandler.playerData.values()) {
                    players.add(ChunkPos.fromPos((int)playerData.playerEntity.x,(int)playerData.playerEntity.z));
                    //System.out.println(players.get(players.size() - 1));
                    playerData.save();
                }

                boolean inBounds;
                int purgeCount = 0;
                for (Chunk chunk : chunks.values()) {
                    ChunkLoader.writeChunk(chunk);
                    inBounds = false;
                    for(Long longVal : players) {
                        if(isInBounds(chunk.x,chunk.z,longVal,Settings.renderDistance)) {
                            inBounds = true;
                            break;
                        }
                    }
                    if(!inBounds) {
                        purgeCount++;
                        removeChunk(chunk.x,chunk.z);
                    }
                }


                ChunkLoader.finishSave();



                System.out.println("SAVE FINISH:" + (System.currentTimeMillis() - start) + "MS");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState blockState) {
        super.setBlockState(x, y, z, blockState);
        //ServerNetworkHandler.sendPacket(new SSendBlockChanges(x,y,z,blockState.block.id));
    }

    public void createItemEntity(BlockPos blockPos) {
        Block block = getBlockState(blockPos).getBlock();
        if(block != Blocks.AIR) {
            ItemEntity itemEntity = new ItemEntity(blockPos.x,blockPos.y,blockPos.z,Entity.getNewId(),block);
            addEntity(itemEntity);
        }
    }

    private int id = 0;

    public synchronized int getNextContainerID() {
        int id = this.id;
        id++;
        return id;
    }
}
