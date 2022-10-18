package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Culling.CullingEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.MeshHolder;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Network.Packet.Client.CRequestChunkPacket;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;
import dev.Hilligans.ourcraft.World.NewWorldSystem.*;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.system.MemoryStack;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class NewWorldRenderTask extends RenderTaskSource {

    public NewWorldRenderTask() {
        super("new_world_render_task", "ourcraft:new_solid_world_renderer");
    }

    public ShaderSource shaderSource;

    public CullingEngine cullingEngine;

    public ExecutorService chunkBuilder = Executors.newSingleThreadExecutor();

    public IThreeDContainer<MeshHolder> meshes = new EmptyContainer<>();

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack) {
                IWorld world = client.newClientWorld;
                engine.getDefaultImpl().setState(window,graphicsContext, new PipelineState().setDepth(true));
                Vector3d pos = window.camera.getSavedPosition();
                int chunkWidth = world.getChunkContainer().getChunkWidth();
                int chunkHeight = world.getChunkContainer().getChunkHeight();
                int renderYDist = client.renderYDistance;
                renderYDist = 1;
                Vector3i playerChunkPos = new Vector3i((int)pos.x / chunkWidth, (int)pos.y / chunkHeight,  (int)pos.z / chunkWidth);
                if(client.renderWorld) {
                    for(int x = 0; x < client.renderDistance; x++) {
                        for(int y = 0; y < renderYDist; y++) {
                            for(int z = 0; z < client.renderDistance; z++) {
                                int xx = x + playerChunkPos.x;
                                int yy = y + playerChunkPos.y;
                                int zz = z + playerChunkPos.z;
                                int nx = -x + playerChunkPos.x;
                                int ny = -y + playerChunkPos.y;
                                int nz = -z + playerChunkPos.z;

                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(xx, yy, zz, world),getMesh(xx, yy, zz), xx, yy, zz);
                                if(x == 0 && y == 0 && z == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(nx, ny, nz, world), getMesh(nx, ny, nz), nx, ny, nz);
                                if(x == 0 && y == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(nx, ny, zz, world), getMesh(nx, ny, zz), nx, ny, zz);
                                if(x == 0 && z == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(nx, yy, nz, world), getMesh(nx, yy, nz), nx, yy, nz);
                                if(y == 0 && z == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(xx, ny, nz, world), getMesh(xx, ny, nz), xx, ny, nz);
                                if(x == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(nx, yy, zz, world), getMesh(nx, yy, zz), nx, yy, zz);
                                if(y == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(xx, ny, zz, world), getMesh(xx, ny, zz), xx, ny, zz);
                                if(z == 0) {
                                    continue;
                                }
                                drawChunk(window,graphicsContext,client,engine,worldStack,playerChunkPos,getChunk(xx, yy, nz, world), getMesh(xx, yy, nz), xx, yy, nz);
                            }
                        }
                    }
                }
            }
        };
    }


    MeshHolder getMesh(int x, int y, int z) {
        return meshes.getChunk(x,y,z);
    }
    IChunk getChunk(int chunkX, int chunkY, int chunkZ, IWorld world) {
        return world.getChunk((long) chunkX << 4, (long) chunkY << 4, (long) chunkZ << 4);
    }

    void drawChunk(RenderWindow window, GraphicsContext graphicsContext, Client client, IGraphicsEngine<?, ?,?> engine, MatrixStack matrixStack, Vector3i playerChunkPos, IChunk chunk, MeshHolder meshHolder, int x, int y, int z) {
        for(Tuple<IChunk, PrimitiveBuilder> tuple : primitiveBuilders) {
            asyncedChunks.remove( ((long)(int)tuple.getTypeA().getX() << 32) ^ (int)tuple.getTypeA().getZ());
            tuple.typeB.setVertexFormat(shaderSource.vertexFormat);
            int meshID = window.getGraphicsEngine().getDefaultImpl().createMesh(window, graphicsContext, tuple.typeB.toVertexMesh());
            meshes.setChunk(tuple.getTypeA().getX(),tuple.getTypeA().getY(),tuple.getTypeA().getZ(),new MeshHolder().set(meshID,tuple.getTypeB().indices.size()));
            primitiveBuilders.remove(tuple);
        }
        if (meshHolder != null) {
            if(meshHolder.id != -1) {
                //TODO fix not technically the right bounding box
                if(matrixStack.frustumIntersection.testAab((chunk.getX() - 1) * chunk.getWidth(), (chunk.getY() - 1) * chunk.getHeight(), (chunk.getZ() - 1) * chunk.getWidth(), (chunk.getX() + 1) * chunk.getWidth(), (chunk.getY() + 1) * chunk.getHeight(), (chunk.getZ() + 1) * chunk.getWidth())) {
                    matrixStack.push();
                    matrixStack.translate(chunk.getX() * chunk.getWidth(), chunk.getY() * chunk.getHeight(), chunk.getZ() * chunk.getWidth());
                    engine.getDefaultImpl().uploadMatrix(graphicsContext,matrixStack,shaderSource);
                    engine.getDefaultImpl().drawMesh(window, graphicsContext, matrixStack, engine.getGraphicsData().getWorldTexture(), shaderSource.program, meshHolder.getId(), meshHolder.index, meshHolder.length);
                    matrixStack.pop();
                }
            }
        } else if(chunk != null) {
            //if(chunk.isDirty()) {
            if(!asyncedChunks.getOrDefault(((long)(int)chunk.getX() << 32) ^ (int)chunk.getZ(), false)) {
                if (getChunk((int) chunk.getX() + 1, (int) chunk.getY(), (int) chunk.getZ(), chunk.getWorld()) != null &&
                        getChunk((int) chunk.getX() - 1, (int) chunk.getY(), (int) chunk.getZ(), chunk.getWorld()) != null &&
                        getChunk((int) chunk.getX(), (int) chunk.getY(), (int) chunk.getZ() + 1, chunk.getWorld()) != null &&
                        getChunk((int) chunk.getX(), (int) chunk.getY(), (int) chunk.getZ() - 1, chunk.getWorld()) != null) {
                    if (x < 2 && y < 2 && z < 2) {
                        buildMesh(window, graphicsContext, chunk);
                    } else {
                        if (putChunk((int) chunk.getX(), (int) chunk.getZ())) {
                            chunkBuilder.submit(() -> {
                                PrimitiveBuilder primitiveBuilder = getPrimitiveBuilder(chunk);
                                primitiveBuilders.add(new Tuple<>(chunk, primitiveBuilder));
                            });
                        }
                    }
                }
            }
            //    chunk.setDirty(false);
            //}
        } else {
            getChunk(x,z,client.newClientWorld,client);
          //  IWorld world = client.newClientWorld;
          //  IChunk chunk1 = new ClassicChunk(world,256,x,z);
          ///  chunk1.fill(Blocks.STONE.getDefaultState1(),0,0,0,16,64,16);
          //  world.setChunk((long) x * chunk1.getWidth(), (long) y * chunk1.getHeight(), (long) z * chunk1.getWidth(),chunk1);
        }
    }

    public ConcurrentLinkedQueue<Tuple<IChunk, PrimitiveBuilder>> primitiveBuilders = new ConcurrentLinkedQueue<>();
    Long2BooleanOpenHashMap asyncedChunks = new Long2BooleanOpenHashMap();
    Long2BooleanOpenHashMap map = new Long2BooleanOpenHashMap();

    void getChunk(int chunkX, int chunkZ, IWorld world, Client client) {
        if (!map.getOrDefault(((long) chunkX << 32) ^ chunkZ, false)) {
            map.put(((long) chunkX << 32) ^ chunkZ, true);
            client.sendPacket(new CRequestChunkPacket(chunkX, chunkZ));
        }
    }

    boolean putChunk(int chunkX, int chunkZ) {
        if (!asyncedChunks.getOrDefault(((long) chunkX << 32) ^ chunkZ, false)) {
            asyncedChunks.put(((long) chunkX << 32) ^ chunkZ, true);
            return true;
        }
        return false;
    }

    public PrimitiveBuilder getPrimitiveBuilder(IChunk chunk) {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
        for(int x = 0; x < chunk.getWidth(); x++) {
            for(int y = 0; y < chunk.getHeight(); y++) {
                for(int z = 0; z < chunk.getWidth(); z++) {
                    IBlockState block = chunk.getBlockState1(x,y,z);
                    for(int a = 0; a < 6; a++) {
                        if(block.getBlock() != Blocks.AIR) {
                            BlockPos p = new BlockPos(x,y,z).add(Block.getBlockPos(block.getBlock().getSide(block,a)));
                            IBlockState newState;
                            if(!p.inRange(0,0,0,16,255,16)) {
                                newState = chunk.getWorld().getBlockState(p.add(chunk.getBlockX(),chunk.getBlockY(),chunk.getBlockZ()));
                            } else {
                                newState = chunk.getBlockState1(new BlockPos(x, y, z).add(Block.getBlockPos(block.getBlock().getSide(block, a))));
                            }
                            if(newState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != newState.getBlock()) || block.getBlock().blockProperties.alwaysRender) {
                                block.getBlock().addVertices(primitiveBuilder,a,1f,block,new BlockPos(x + chunk.getX(), y + chunk.getY(), z + chunk.getY()),x,z);
                            }
                        }
                    }
                }
            }
        }
        return primitiveBuilder;
    }

    public void buildMesh(RenderWindow window, GraphicsContext graphicsContext, IChunk chunk) {
        PrimitiveBuilder primitiveBuilder = getPrimitiveBuilder(chunk);
        primitiveBuilder.setVertexFormat(shaderSource.vertexFormat);
        int meshID = window.getGraphicsEngine().getDefaultImpl().createMesh(window, graphicsContext, primitiveBuilder.toVertexMesh());
        meshes.setChunk(chunk.getX(),chunk.getY(),chunk.getZ(),new MeshHolder().set(meshID,primitiveBuilder.indices.size()));
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        shaderSource = gameInstance.SHADERS.get("ourcraft:world_shader");
    }

    @Override
    public void loadGraphics(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        super.loadGraphics(graphicsEngine);
    }
}