package Hilligans.World;



import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.ClientMain;
import Hilligans.Entity.Entity;
import Hilligans.Util.Settings;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ClientWorld extends World {

    public ClientWorld() {
        getChunk(0,0);
    }

    public void render(MatrixStack matrixStack) {
        Vector3f pos = Camera.pos;
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                Chunk chunk = getChunk(x * 16 + (int)pos.x >> 4,z * 16 + (int)pos.z >> 4);
                if(chunk == null) {
                    if(!ClientMain.joinServer) {
                        generateChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
                    } else {
                        if (ClientMain.valid) {
                            requestChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
                        }
                    }
                } else {
                    matrixStack.push();
                    chunk.render(matrixStack);
                    matrixStack.pop();
                }
            }
        }

        for(Entity entity : entities.values()) {
            if(entity.id != ClientMain.playerId || Camera.thirdPerson) {
                matrixStack.push();
                entity.render(matrixStack);
                matrixStack.pop();
            }
        }
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
        entities.remove(id);
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
