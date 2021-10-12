package dev.Hilligans.World;

import dev.Hilligans.Block.Block;
import dev.Hilligans.Data.Other.BlockStates.BlockState;
import dev.Hilligans.Block.Blocks;
import dev.Hilligans.Data.Other.BlockPos;
import dev.Hilligans.Data.Other.ChunkPos;
import dev.Hilligans.Data.Other.Server.ServerPlayerData;
import dev.Hilligans.Entity.Entities.ItemEntity;
import dev.Hilligans.Entity.Entity;
import dev.Hilligans.Network.Packet.Server.SCreateEntityPacket;
import dev.Hilligans.Network.Packet.Server.SRemoveEntityPacket;
import dev.Hilligans.Network.ServerNetworkHandler;
import dev.Hilligans.Server.IServer;
import dev.Hilligans.ServerMain;
import dev.Hilligans.Util.NamedThreadFactory;
import dev.Hilligans.Util.Settings;
import dev.Hilligans.WorldSave.ChunkLoader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerWorld extends World {

    ArrayList<Integer> entityRemovals = new ArrayList<>();
    long autoSaveAfter = 30 * 1000;
    long autoSave = -1;
    public IServer server;

    @Override
    public void addEntity(Entity entity) {
        entities.put(entity.id,entity);
        ServerMain.getServer().sendPacket(new SCreateEntityPacket(entity));
    }

    @Override
    public void removeEntity(int id) {
        entityRemovals.add(id);
    }

    private void handleRemove() {
        for(Integer integer : entityRemovals) {
            entities.remove((int)integer);
            ServerMain.getServer().sendPacket(new SRemoveEntityPacket(integer));
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

    public static ExecutorService executorService = Executors.newFixedThreadPool(4, new NamedThreadFactory("chunk_saver"));

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
        chunkContainer.forEach(chunk -> chunk.tick());
        if(Settings.autoSave) {
            if (autoSave == -1) {
                autoSave = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - autoSave > autoSaveAfter) {
                try {
                    autoSave = System.currentTimeMillis();
                    final long start = System.currentTimeMillis();
                    ArrayList<Long> players = new ArrayList<>();
                    for (ServerPlayerData playerData : ServerNetworkHandler.playerData.values()) {
                        players.add(ChunkPos.fromPos((int) playerData.playerEntity.x, (int) playerData.playerEntity.z));
                        playerData.save();
                    }

                   if(Settings.asyncWorldSave) {
                       AtomicInteger chunkList = new AtomicInteger(0);
                       final int size = chunkContainer.getSize();
                       chunkContainer.forEach(chunk -> executorService.submit(() -> {
                           ChunkLoader.writeChunk(chunk);
                           boolean inBounds = false;
                           for (Long longVal : players) {
                               if (isInBounds(chunk.x, chunk.z, longVal, Settings.renderDistance)) {
                                   inBounds = true;
                                   break;
                               }
                           }
                           chunk.needsSaving = false;
                           if (!inBounds) {
                               removeChunk(chunk.x, chunk.z);
                           }
                           if (chunkList.addAndGet(1) == size) {
                               System.out.println("SAVE FINISH:" + (System.currentTimeMillis() - start) + "MS");
                               ChunkLoader.finishSave();
                           }
                       }));
                   } else {
                       chunkContainer.forEach(chunk -> {
                           ChunkLoader.writeChunk(chunk);
                           boolean inBounds = false;
                           for (Long longVal : players) {
                               if (isInBounds(chunk.x, chunk.z, longVal, Settings.renderDistance)) {
                                   inBounds = true;
                                   break;
                               }
                           }
                           chunk.needsSaving = false;
                           if (!inBounds) {
                               removeChunk(chunk.x, chunk.z);
                           }
                       });
                       System.out.println("SAVE FINISH:" + (System.currentTimeMillis() - start) + "MS");
                       ChunkLoader.finishSave();
                   }

                   // System.out.println("SAVE FINISH:" + (System.currentTimeMillis() - start) + "MS");
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
