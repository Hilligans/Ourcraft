package Hilligans.World;



import Hilligans.Data.Other.BlockState;
import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.ClientMain;
import Hilligans.Entity.Entity;
import Hilligans.Util.Settings;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class ClientWorld extends World {

    public ArrayList<SubChunk> queuedChunks = new ArrayList<>();

    public ClientWorld() {
        getChunk(0,0);
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
            setBlockState(blockChange.x,blockChange.y,blockChange.z,blockChange.blockState);
        }
        blockChanges.clear();
        blockChanges.addAll(skippedBlockChanges);
        skippedBlockChanges.clear();
        if(purgeTime > 100) {
            purgeTime = 0;
            purgeChunks(Settings.renderDistance + 2);
        } else {
            buildChunks(16);
            purgeTime++;
        }

    }

    int purgeTime = 0;

    public void buildChunks(int count) {
        for(int x = 0; x < Math.min(count,queuedChunks.size()); x++) {
            queuedChunks.get(0).createMesh1();
            queuedChunks.remove(0);
        }
    }

    public void purgeChunks(int distance) {
        int cameraX = (int)Camera.pos.x >> 4;
        int cameraZ = (int)Camera.pos.z >> 4;
        ArrayList<Long> removedChunks = new ArrayList<>();
        for(Long longVal : chunks.keySet()) {
              int x = (int)((long)longVal);
              int z = (int)(longVal >> 32);

              if(Math.abs(cameraX - x) > distance || Math.abs(cameraZ - z) > distance) {
                  Chunk chunk = chunks.get(longVal);
                  chunk.destroy();
                  removedChunks.add(longVal);
              }
        }

        for(Long longVal : removedChunks) {
            chunks.remove(longVal);
        }
    }


    public void render(MatrixStack matrixStack) {

        if(!Settings.renderTransparency) {
            glDisable(GL_BLEND);
        }
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);

        Vector3d pos = Camera.pos;
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                Chunk chunk = getChunk(x * 16 + (int)pos.x >> 4,z * 16 + (int)pos.z >> 4);
                if(chunk == null) {
                    if(!ClientMain.getClient().joinServer) {
                        generateChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
                    } else {
                        if (ClientMain.getClient().valid) {
                            requestChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
                        }
                    }
                } else {
                    if(Camera.shouldRenderChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4)) {
                        matrixStack.push();
                        chunk.render(matrixStack);
                        matrixStack.pop();
                    }
                }
            }
        }
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        // TODO: 2021-02-06 server removes entities at a bad time
        try {
            for (Entity entity : entities.values()) {
                if (entity.id != ClientMain.getClient().playerId || Camera.thirdPerson) {
                    matrixStack.push();
                    entity.render(matrixStack);
                    matrixStack.pop();
                }
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

        glEnable(GL_BLEND);
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState blockState) {
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
