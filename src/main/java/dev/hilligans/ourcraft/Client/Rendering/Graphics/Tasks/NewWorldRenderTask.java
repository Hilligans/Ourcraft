package dev.hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Culling.CullingEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Client.Rendering.MeshHolder;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Network.Packet.Client.CRequestChunkPacket;
import dev.hilligans.ourcraft.Util.Settings;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.hilligans.ourcraft.World.NewWorldSystem.EmptyContainer;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IThreeDContainer;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NewWorldRenderTask extends RenderTaskSource {

    public NewWorldRenderTask() {
        super("new_world_render_task", "ourcraft:new_solid_world_renderer");
    }

    public ShaderSource shaderSource;

    public CullingEngine cullingEngine;

    public ExecutorService chunkBuilder = Executors.newFixedThreadPool(2);

    @Override
    public RenderTask getDefaultTask() {

        IThreeDContainer<MeshHolder> meshes = new EmptyContainer<>();

        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IWorld world = client.newClientWorld;
                engine.getDefaultImpl().setState(window, graphicsContext, new PipelineState().setDepth(true));
                Vector3d pos = window.camera.getSavedPosition();
                int chunkWidth = world.getChunkContainer().getChunkWidth();
                int chunkHeight = world.getChunkContainer().getChunkHeight();
                int renderYDist = client.renderYDistance;
                Vector3i playerChunkPos = new Vector3i((int) pos.x / chunkWidth, (int) pos.y / chunkHeight, (int) pos.z / chunkWidth);
                if (client.renderWorld) {
                    for (int x = 0; x < client.renderDistance; x++) {
                        for (int y = 0; y < renderYDist; y++) {
                            for (int z = 0; z < client.renderDistance; z++) {
                                int xx = x + playerChunkPos.x;
                                int yy = y + playerChunkPos.y;
                                int zz = z + playerChunkPos.z;
                                int nx = -x + playerChunkPos.x;
                                int ny = -y + playerChunkPos.y;
                                int nz = -z + playerChunkPos.z;

                                drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, xx, yy, zz, chunkWidth, chunkHeight);
                                if (x != 0 && y != 0 && z != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, nx, ny, nz, chunkWidth, chunkHeight);
                                }
                                if (x != 0 && y != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, nx, ny, zz, chunkWidth, chunkHeight);
                                }
                                if (x != 0 && z != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, nx, yy, nz, chunkWidth, chunkHeight);
                                }
                                if (y != 0 && z != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, xx, ny, nz, chunkWidth, chunkHeight);
                                }
                                if (x != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, nx, yy, zz, chunkWidth, chunkHeight);
                                }
                                if (y != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, xx, ny, zz, chunkWidth, chunkHeight);
                                }
                                if (z != 0) {
                                    drawChunk(window, graphicsContext, client, engine, worldStack, playerChunkPos, world, xx, yy, nz, chunkWidth, chunkHeight);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void close() {

            }

            MeshHolder getMesh(int x, int y, int z) {
                return meshes.getChunk(x,y,z);
            }

            IChunk getChunk(int chunkX, int chunkY, int chunkZ, IWorld world) {
                return world.getChunk((long) chunkX * world.getChunkContainer().getChunkWidth(), (long) chunkY * world.getChunkContainer().getChunkHeight(), (long) chunkZ * world.getChunkContainer().getChunkWidth());
            }

            public static int rebuildDistance = 10;

            void drawChunk(RenderWindow window, GraphicsContext graphicsContext, Client client, IGraphicsEngine<?, ?,?> engine, MatrixStack matrixStack, Vector3i playerChunkPos, IWorld world, int x, int y, int z, int chunkWidth, int chunkHeight) {
                MeshHolder meshHolder = getMesh(x, y, z);
                for (Tuple<IChunk, PrimitiveBuilder> tuple : primitiveBuilders) {
                    asyncedChunks.remove(((tuple.getTypeA().getX()) << 32) | (tuple.getTypeA().getZ() & 0xffffffffL));
                    tuple.typeB.setVertexFormat(shaderSource.vertexFormat);
                    int meshID = (int) window.getGraphicsEngine().getDefaultImpl().createMesh(window, graphicsContext, tuple.typeB.toVertexMesh());
                    meshes.setChunk(tuple.getTypeA().getX(), tuple.getTypeA().getY(), tuple.getTypeA().getZ(), new MeshHolder().set(meshID, tuple.getTypeB().indices.size()));
                    primitiveBuilders.remove(tuple);
                }
                if (meshHolder != null) {
                    if (meshHolder.id != -1 && meshHolder.length != 0) {
                        matrixStack.updateFrustum();
                        if (matrixStack.frustumIntersection.testAab((x) * chunkWidth, (y) * chunkHeight, (z) * chunkWidth, (x + 1) * chunkWidth, (y + 1) * chunkHeight, (z + 1) * chunkWidth)) {
                            matrixStack.push();
                            matrixStack.translate(x * chunkWidth, y * chunkHeight, z * chunkWidth);
                            engine.getDefaultImpl().uploadMatrix(graphicsContext, matrixStack, shaderSource);
                            engine.getDefaultImpl().drawMesh(window, graphicsContext, matrixStack, engine.getGraphicsData().getWorldTexture(), shaderSource.program, meshHolder.getId(), meshHolder.index, meshHolder.length);
                            matrixStack.pop();
                        }
                    }
                } else {
                    IChunk chunk = getChunk(x, y, z, world);

                    if (chunk != null) {
                        if (chunk.isDirty()) {
                            if (    getChunk(x + 1, y, z, world) != null &&
                                    getChunk(x - 1, y, z, world) != null &&
                                    getChunk(x, y + 1, z, world) != null &&
                                    getChunk(x, y - 1, z, world) != null &&
                                    getChunk(x, y, z + 1, world) != null &&
                                    getChunk(x, y, z - 1, world) != null) {
                                if ((Math.abs(x - playerChunkPos.x) < rebuildDistance && Math.abs(y - playerChunkPos.y) < rebuildDistance && Math.abs(z - playerChunkPos.z) < rebuildDistance)) {
                                    buildMesh(window, graphicsContext, chunk);
                                    //System.out.println(chunk.getX() + " " + chunk.getY() + " " + chunk.getZ());
                                    //System.out.println(chunk.getBlockState1(0,0,0).getBlock().getName());
                                    chunk.setDirty(false);
                                }
                            }
                           /* if (!asyncedChunks.getOrDefault(((long) x << 32) ^ (int) (z & 0xffffffffL), false)) {
                                if (getChunk(x + 1, y, z, world) != null &&
                                        getChunk(x - 1, y, z, world) != null &&
                                        getChunk(x, y, z + 1, world) != null &&
                                        getChunk(x, y, z - 1, world) != null) {
                                    if ((x < rebuildDistance && y < rebuildDistance && z < rebuildDistance)) {
                                        buildMesh(window, graphicsContext, chunk);
                                    } else {
                                        if (putChunk(x, z)) {
                                            chunkBuilder.submit(() -> {
                                                PrimitiveBuilder primitiveBuilder = getPrimitiveBuilder(chunk);
                                                primitiveBuilders.add(new Tuple<>(chunk, primitiveBuilder));
                                            });
                                        }
                                    }
                                    chunk.setDirty(false);
                                }
                            }
                        }

                            */
                        } else {
                            //  System.out.printf("X:%s Y:%s Z:%s\n", x, y, z);
                        }
                    } else {
                        //getChunk(x, z, client.newClientWorld, client);
                    }
                }
            }

            public ConcurrentLinkedQueue<Tuple<IChunk, PrimitiveBuilder>> primitiveBuilders = new ConcurrentLinkedQueue<>();
            Long2BooleanOpenHashMap asyncedChunks = new Long2BooleanOpenHashMap();
            Long2BooleanOpenHashMap map = new Long2BooleanOpenHashMap();

            void getChunk(int chunkX, int chunkZ, IWorld world, Client client) {
                if (!map.getOrDefault((((long)chunkX) << 32) | (chunkZ & 0xffffffffL), false)) {
                    map.put((((long)chunkX) << 32) | (chunkZ & 0xffffffffL), true);
                    //client.sendPacket(new CRequestChunkPacket(chunkX, chunkZ));
                }
            }

            boolean putChunk(int chunkX, int chunkZ) {
                if (!asyncedChunks.getOrDefault((((long) chunkX) << 32) | (chunkZ & 0xffffffffL), false)) {
                    asyncedChunks.put((((long)chunkX) << 32) | (chunkZ & 0xffffffffL), true);
                    return true;
                }
                return false;
            }

            /*
            public PrimitiveBuilder getPrimitiveBuilder(IChunk chunk) {
                PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(shaderSource.vertexFormat);
                for(int x = 0; x < chunk.getWidth(); x++) {
                    for(int y = chunk.getHeight() - 1; y >= 0; y--) {
                        for(int z = 0; z < chunk.getWidth(); z++) {
                            IBlockState block = chunk.getBlockState1(x,y,z);
                            if(block.getBlock() != Blocks.AIR && !block.getBlock().blockProperties.translucent) {
                                for (int a = 0; a < 6; a++) {
                                    BlockPos p = new BlockPos(x, y, z).add(Block.getBlockPos(block.getBlock().getSide(block, a)));
                                    IBlockState newState;
                                    if (!p.inRange(0, 0, 0, 16, 255, 16)) {
                                        newState = chunk.getWorld().getBlockState(p.add(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ()));
                                    } else {
                                        newState = chunk.getBlockState1(new BlockPos(x, y, z).add(Block.getBlockPos(block.getBlock().getSide(block, a))));
                                    }
                                    if (newState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != newState.getBlock()) || block.getBlock().blockProperties.alwaysRender) {
                                        block.getBlock().addVertices(primitiveBuilder, a, 1f, block, new BlockPos(x + chunk.getX(), y + chunk.getY(), z + chunk.getY()), x, z);
                                    }
                                }
                            }
                        }
                    }
                }
                return primitiveBuilder;
            }

             */

            public PrimitiveBuilder getPrimitiveBuilder(IChunk chunk, PrimitiveBuilder primitiveBuilder) {
                BlockPos p = new BlockPos(0, 0, 0);
                primitiveBuilder = (primitiveBuilder == null ? new PrimitiveBuilder(shaderSource.vertexFormat) : primitiveBuilder);
                for(int x = 0; x < chunk.getWidth(); x++) {
                    for(int y = 0; y < chunk.getHeight(); y++) {
                        for(int z = 0; z < chunk.getWidth(); z++) {
                            IBlockState block = chunk.getBlockState1(x,y,z);
                            if(block.getBlock() != Blocks.AIR && !block.getBlock().blockProperties.translucent) {
                                for (int a = 0; a < 6; a++) {
                                    p.set(x, y, z).add(Block.getBlockPosImmutable(block.getBlock().getSide(block, a)));
                                    IBlockState newState;
                                    if (!p.inRange(0, 0, 0, chunk.getWidth() - 1, chunk.getHeight() - 1, chunk.getWidth() - 1)) {
                                        newState = chunk.getWorld().getBlockState(p.add(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ()));
                                    } else {
                                        newState = chunk.getBlockState1(p);
                                    }
                                    if (newState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != newState.getBlock()) || block.getBlock().blockProperties.alwaysRender) {
                                        block.getBlock().addVertices(primitiveBuilder, a, 1f, block, p.set(x, y, z), x, z);
                                    }
                                }
                            }
                        }
                    }
                }
                return primitiveBuilder;
            }

            public PrimitiveBuilder primitiveBuilder = null;
            public void buildMesh(RenderWindow window, GraphicsContext graphicsContext, IChunk chunk) {
                long start = System.currentTimeMillis();
                primitiveBuilder = getPrimitiveBuilder(chunk, primitiveBuilder);
                primitiveBuilder.setVertexFormat(shaderSource.vertexFormat);
                int meshID = (int) window.getGraphicsEngine().getDefaultImpl().createMesh(window, graphicsContext, primitiveBuilder.toVertexMesh());
                if(primitiveBuilder.indices.size == 0) {
                    System.out.println(chunk.getX() + " " + chunk.getY() + " " + chunk.getZ());
                }
                meshes.setChunk(chunk.getX(),chunk.getY(),chunk.getZ(),new MeshHolder().set(meshID,primitiveBuilder.indices.size()));
                primitiveBuilder.size = 0;
                primitiveBuilder.vertices.size = 0;
                primitiveBuilder.indices.size = 0;
                long end = System.currentTimeMillis();
                //System.out.println("Time to build:" + (end - start));
            }

            /*
            public IPrimitiveBuilder getPrimitiveBuilder1(IChunk chunk) {
                BufferPrimitiveBuilder primitiveBuilder = new BufferPrimitiveBuilder(128, 128).setVertexFormat(shaderSource.vertexFormat);
                for(int x = 0; x < chunk.getWidth(); x++) {
                    for(int y = chunk.getHeight() - 1; y >= 0; y--) {
                        for(int z = 0; z < chunk.getWidth(); z++) {
                            IBlockState block = chunk.getBlockState1(x,y,z);
                            if(block.getBlock() != Blocks.AIR && !block.getBlock().blockProperties.translucent) {
                                for (int a = 0; a < 6; a++) {
                                    BlockPos p = new BlockPos(x, y, z).add(Block.getBlockPos(block.getBlock().getSide(block, a)));
                                    IBlockState newState;
                                    if (!p.inRange(0, 0, 0, 16, 255, 16)) {
                                        newState = chunk.getWorld().getBlockState(p.add(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ()));
                                    } else {
                                        newState = chunk.getBlockState1(new BlockPos(x, y, z).add(Block.getBlockPos(block.getBlock().getSide(block, a))));
                                    }
                                    if (newState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != newState.getBlock()) || block.getBlock().blockProperties.alwaysRender) {
                                        block.getBlock().addVertices(primitiveBuilder, a, 1f, block, new BlockPos(x + chunk.getX(), y + chunk.getY(), z + chunk.getY()), x, y, z);
                                    }
                                }
                            }
                        }
                    }
                }
                return primitiveBuilder;
            }

             */
        };
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