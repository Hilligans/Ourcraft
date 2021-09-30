package Hilligans.World;



import Hilligans.Client.Audio.SoundBuffer;
import Hilligans.Client.Audio.Sounds;
import Hilligans.Client.Client;
import Hilligans.Client.Rendering.ClientUtil;
import Hilligans.Client.Rendering.MiniMap.MiniMap;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.NewRenderer.Shader;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.Data.Other.BlockStates.BlockState;
import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Entity.Entity;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CRequestChunkPacket;
import Hilligans.Util.Settings;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31C.GL_COPY_READ_BUFFER;
import static org.lwjgl.opengl.GL31C.GL_COPY_WRITE_BUFFER;

public class ClientWorld extends World {

    public ConcurrentLinkedQueue<SubChunk> queuedChunks = new ConcurrentLinkedQueue<>();

    public ConcurrentLinkedQueue<DoubleTypeWrapper<PrimitiveBuilder, SubChunk>> asyncChunkQueue = new ConcurrentLinkedQueue<>();


    public int chunkCount = 0;
    public MiniMap miniMap = new MiniMap(this);

    public Client client;

    public ClientWorld(Client client) {
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
            if(Settings.asyncChunkBuilding) {
                while(!queuedChunks.isEmpty()) {
                    SubChunk subChunk = queuedChunks.poll();
                    ClientUtil.chunkBuilder.submit(() -> {
                        PrimitiveBuilder primitiveBuilder = subChunk.getMeshBuilder();
                        asyncChunkQueue.add(new DoubleTypeWrapper<>(primitiveBuilder,subChunk));
                    });
                }

                while(!asyncChunkQueue.isEmpty()) {
                    DoubleTypeWrapper<PrimitiveBuilder,SubChunk> type = asyncChunkQueue.poll();
                    type.getTypeB().verticesCount = type.getTypeA().indices.size();
                    type.getTypeB().id = VAOManager.createVAO(type.getTypeA());
                }
            } else {
                buildChunks(12);
            }
            purgeTime++;
        }

    }


    public void setChunk(Chunk chunk) {
        super.setChunk(chunk);
        miniMap.update(chunk.x,chunk.z);
    }



    int purgeTime = 0;

    public void buildChunks(int count) {
        for(int x = 0; x < Math.min(count,queuedChunks.size()); x++) {
            queuedChunks.poll().createMesh1();
        }
    }

    public void purgeChunks(int distance) {
        int cameraX = (int)Camera.pos.x >> 4;
        int cameraZ = (int)Camera.pos.z >> 4;
        ArrayList<Long> removedChunks = new ArrayList<>();
        chunkContainer.forEach(chunk -> {
            if(Math.abs(cameraX - chunk.x) > distance || Math.abs(cameraZ - chunk.z) > distance) {
                chunk.destroy();
                removedChunks.add((long)chunk.x & 4294967295L | ((long)chunk.z & 4294967295L) << 32);
            }
        });

        //TODO: sometimes tries to remove -1 and causes crash
        for(Long longVal : removedChunks) {
            chunkContainer.removeChunk(longVal);
        }
    }


    public void render(MatrixStack matrixStack) {
        vertices = 0;
        count = 0;
        if(!Settings.renderTransparency) {
            glDisable(GL_BLEND);
        }
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);

        Vector3d pos = Camera.renderPos;
        for(int x = 0; x < Settings.renderDistance; x++) {
            for(int z = 0; z < Settings.renderDistance; z++) {
                drawChunk(matrixStack,pos,x,z);
                if(x != 0) {
                    drawChunk(matrixStack, pos, -x, z);
                    if(z != 0) {
                        drawChunk(matrixStack,pos,-x,-z);
                    }
                }
                if(z != 0) {
                    drawChunk(matrixStack,pos,x,-z);
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

    public int vertices = 0;
    public int count = 0;

    private void drawChunk(MatrixStack matrixStack, Vector3d pos, int x, int z) {
        Vector3i playerChunkPos = new Vector3i((int)pos.x >> 4, 0, (int)pos.z >> 4);
        Chunk chunk = getChunk(x * 16 + (int)pos.x >> 4,z * 16 + (int)pos.z >> 4);
        if(chunk == null) {
            if(!ClientMain.getClient().joinServer) {
                generateChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
            } else {
                if (ClientMain.getClient().valid && Settings.requestChunks) {
                    requestChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4);
                }
            }
        } else {
            vertices += chunk.getTotalVertices();
            count++;
            if(Camera.shouldRenderChunk(x * 16 + (int) pos.x >> 4, z * 16 + (int) pos.z >> 4,matrixStack)) {
                if(matrixStack.frustumIntersection.testAab(new Vector3f((chunk.x - playerChunkPos.x) * 16, 0, (chunk.z - playerChunkPos.z) * 16),new Vector3f((chunk.x + 1 - playerChunkPos.x) * 16, 256f, (chunk.z + 1 - playerChunkPos.z) * 16))) {
                    matrixStack.push();
                    matrixStack.translate((chunk.x - playerChunkPos.x) * 16, 0, (chunk.z - playerChunkPos.z) * 16);
                    chunk.render(matrixStack);
                    matrixStack.pop();
                }
            }
        }
    }

    public void requestChunk(int x, int z) {
        for (ClientWorld.XZHolder requestedChunk : requestedChunks) {
            if (requestedChunk.x == x && requestedChunk.z == z) {
                return;
            }
        }
        requestedChunks.add(new ClientWorld.XZHolder(x,z));
        client.sendPacket(new CRequestChunkPacket(x, z));
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
