package dev.hilligans.ourcraft.World;



import dev.hilligans.ourcraft.Client.Audio.SoundBuffer;
import dev.hilligans.ourcraft.Client.Audio.Sounds;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.MiniMap.MiniMap;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Client.Camera;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Network.Packet.Client.CRequestChunkPacket;
import dev.hilligans.ourcraft.Util.Settings;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ClientWorld extends World {

    public ConcurrentLinkedQueue<SubChunk> queuedChunks = new ConcurrentLinkedQueue<>();

    public ConcurrentLinkedQueue<Tuple<PrimitiveBuilder, SubChunk>> asyncChunkQueue = new ConcurrentLinkedQueue<>();


    public int chunkCount = 0;
    public MiniMap miniMap = new MiniMap(this);

    public Client client;

    public ClientWorld(Client client) {
        super(client.gameInstance);
        getChunk(0,0);
        this.client = client;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    public ConcurrentLinkedQueue<BlockChange> skippedBlockChanges = new ConcurrentLinkedQueue<>();

    @Override
    public void tick() {
        for(BlockChange blockChange : blockChanges) {
            if(getChunk(blockChange.x >> 4, blockChange.z >> 4) == null) {
                skippedBlockChanges.add(blockChange);
                continue;
            }
            setBlockStateDirect(blockChange.x,blockChange.y,blockChange.z,blockChange.blockState);
        }
        blockChanges.clear();
        blockChanges.addAll(skippedBlockChanges);
        skippedBlockChanges.clear();
        if(purgeTime > 100) {
            purgeTime = 0;
            if(Settings.destroyChunkDistance != -1) {
                purgeChunks(Settings.destroyChunkDistance + 2);
            }
        } else {

        }

    }

    int purgeTime = 0;

    public void purgeChunks(int distance) {
        int cameraX = (int) Camera.pos.x >> 4;
        int cameraZ = (int)Camera.pos.z >> 4;
        ArrayList<Long> removedChunks = new ArrayList<>();
        chunkContainer.forEach(chunk -> {
            if(Math.abs(cameraX - chunk.x) > distance || Math.abs(cameraZ - chunk.z) > distance) {
                //TODO fix
              //  chunk.destroy();
                removedChunks.add((long)chunk.x & 4294967295L | ((long)chunk.z & 4294967295L) << 32);
            }
        });

        //TODO: sometimes tries to remove -1 and causes crash
        for(Long longVal : removedChunks) {
            chunkContainer.removeChunk(longVal);
        }
    }

    public int vertices = 0;
    public int count = 0;

    public void reloadChunks() {
        chunkContainer.forEach(chunk -> {
           if(chunk.id != -1 && chunk.id != -2 && chunk.id != -3) {
               VAOManager.destroyBuffer(chunk.id);
           }
           for (SubChunk subChunk : chunk.chunks) {
               subChunk.destroy();
               subChunk.id = -2;
           }
           chunk.id = -1;
        });
    }

    public void playSound(SoundBuffer soundBuffer, Vector3d pos) {
        if(Settings.sounds) {
            ClientMain.getClient().soundEngine.addSound(soundBuffer,pos);
        }
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState blockState) {
        playSound(Sounds.BLOCK_BREAK,new Vector3d(x,y,z));
        setBlockStateDirect(x,y,z,blockState);
    }

    public void setBlockStateDirect(int x, int y, int z, BlockState blockState) {
        if(y >= Settings.minHeight  && y < Settings.maxHeight) {
            super.setBlockState(x, y, z, blockState);
            notifySubChunks(x >> 4, y >> 4, z >> 4);
        }
    }

    public void notifySubChunks(int chunkX, int chunkY, int chunkZ) {
        Chunk chunk = getChunk(chunkX,chunkZ);

        Chunk[] chunks = new Chunk[] {getChunk(chunkX + 1,chunkZ),getChunk(chunkX - 1,chunkZ),getChunk(chunkX,chunkZ + 1),getChunk(chunkX,chunkZ - 1)};

        chunk.chunks.get(chunkY).destroy();

        SubChunk subChunk;
        if(chunkY < Settings.chunkHeight - 1) {
            subChunk = chunk.chunks.get(chunkY + 1);
            if (subChunk != null) {
                subChunk.destroy();
            }
        }
        if(chunkY != 0) {
            subChunk = chunk.chunks.get(chunkY - 1);
            if (subChunk != null) {
                subChunk.destroy();
            }
        }

        for(Chunk chunk1 : chunks) {
            if(chunk1 != null) {
                chunk1.chunks.get(chunkY).destroy();
            }
        }

    }

    @Override
    public void addEntity(Entity entity) {
        entities.put(entity.id,entity);
    }


    @Override
    public void removeEntity(int id) {
        entities.remove(id).destroy();
    }

    static class XZHolder {
        public int x;
        public int z;

        public XZHolder(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }


}
