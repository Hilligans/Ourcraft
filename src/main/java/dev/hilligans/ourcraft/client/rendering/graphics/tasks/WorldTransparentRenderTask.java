package dev.hilligans.ourcraft.client.rendering.graphics.tasks;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.MeshHolder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.world.newworldsystem.EmptyContainer;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IThreeDContainer;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldTransparentRenderTask extends RenderTaskSource {

    public WorldTransparentRenderTask() {
        super("world_transparent_render_task", "ourcraft:translucent_world_renderer");
    }

    public int lastTickRenderCount = 0;

    public ShaderSource shaderSource;

    public ExecutorService chunkBuilder = Executors.newFixedThreadPool(2);

    public IThreeDContainer<MeshHolder> meshes = new EmptyContainer<>();

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IWorld world = client.newClientWorld;
                //engine.getDefaultImpl().setState(window, graphicsContext, new PipelineState().setDepth(true));
                Vector3d pos = window.camera.getSavedPosition();
                int chunkWidth = world.getChunkContainer().getChunkWidth();
                int chunkHeight = world.getChunkContainer().getChunkHeight();
                int renderYDist = client.renderYDistance;
                //renderYDist = 1;
                Vector3i playerChunkPos = new Vector3i((int) pos.x / chunkWidth, (int) pos.y / chunkHeight, (int) pos.z / chunkWidth);
                if (client.renderWorld) {
                    engine.getDefaultImpl().bindPipeline(graphicsContext, shaderSource.program);
                    engine.getDefaultImpl().bindTexture(graphicsContext, engine.getGraphicsData().getWorldTexture());
                    for (int x = client.renderDistance - 1; x >= 0; x--) {
                        for (int y = renderYDist - 1; y >= 0; y--) {
                            for (int z = client.renderDistance - 1; z >= 0; z--) {
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
            public PipelineState getPipelineState() {
                return new PipelineState().setDepth(true);
            }
        };
    }


    MeshHolder getMesh(int x, int y, int z) {
        return meshes.getChunk(x,y,z);
    }
    IChunk getChunk(int chunkX, int chunkY, int chunkZ, IWorld world) {
        return world.getChunk((long) chunkX * world.getChunkContainer().getChunkWidth(), (long) chunkY * world.getChunkContainer().getChunkHeight(), (long) chunkZ * world.getChunkContainer().getChunkWidth());
    }

    public static int rebuildDistance = 4;

    void drawChunk(RenderWindow window, GraphicsContext graphicsContext, Client client, IGraphicsEngine<?, ?,?> engine, MatrixStack matrixStack, Vector3i playerChunkPos, IWorld world, int x, int y, int z, int chunkWidth, int chunkHeight) {
        MeshHolder meshHolder = getMesh(x, y, z);
        for (Tuple<IChunk, PrimitiveBuilder> tuple : primitiveBuilders) {
            asyncedChunks.remove(((tuple.getTypeA().getX()) << 32) | (tuple.getTypeA().getZ() & 0xffffffffL));
            tuple.typeB.setVertexFormat(shaderSource.vertexFormat);
            int meshID = (int) window.getGraphicsEngine().getDefaultImpl().createMesh(graphicsContext, tuple.typeB.toVertexMesh());
            meshes.setChunk(tuple.getTypeA().getX(), tuple.getTypeA().getY(), tuple.getTypeA().getZ(), new MeshHolder().set(meshID, tuple.getTypeB().indices.size()));
            primitiveBuilders.remove(tuple);
        }
        if (meshHolder != null) {
            if (meshHolder.id != -1 && meshHolder.length != 0) {
                matrixStack.updateFrustum();
                if (matrixStack.frustumIntersection.testAab((x) * chunkWidth, (y) * chunkHeight, (z) * chunkWidth, (x + 1) * chunkWidth, (y + 1) * chunkHeight, (z + 1) * chunkWidth)) {
                    matrixStack.push();
                    matrixStack.translate(x * chunkWidth, y * chunkHeight, z * chunkWidth);
                    IDefaultEngineImpl<?,?> impl = engine.getDefaultImpl();
                    impl.uploadMatrix(graphicsContext, matrixStack, shaderSource);
                    impl.drawMesh(graphicsContext, matrixStack, meshHolder.getId(), meshHolder.index, meshHolder.length);
                    matrixStack.pop();
                }
            }
        } else {
            IChunk chunk = getChunk(x, y, z, world);
            if (chunk != null) {
                if (chunk.isDirty()) {
                    if (getChunk(x + 1, y, z, world) != null &&
                            getChunk(x - 1, y, z, world) != null &&
                            getChunk(x, y + 1, z, world) != null &&
                            getChunk(x, y - 1, z, world) != null &&
                            getChunk(x, y, z + 1, world) != null &&
                            getChunk(x, y, z - 1, world) != null) {
                        if ((Math.abs(x - playerChunkPos.x) < rebuildDistance && Math.abs(y - playerChunkPos.y) < rebuildDistance && Math.abs(z - playerChunkPos.z) < rebuildDistance)) {
                            buildMesh(window, graphicsContext, chunk);
                            chunk.setDirty(false);
                        }
                    }
                }
            }
        }
    }

    public ConcurrentLinkedQueue<Tuple<IChunk, PrimitiveBuilder>> primitiveBuilders = new ConcurrentLinkedQueue<>();
    Long2BooleanOpenHashMap asyncedChunks = new Long2BooleanOpenHashMap();

    boolean putChunk(int chunkX, int chunkZ) {
        if (!asyncedChunks.getOrDefault((((long) chunkX) << 32) | (chunkZ & 0xffffffffL), false)) {
            asyncedChunks.put((((long)chunkX) << 32) | (chunkZ & 0xffffffffL), true);
            return true;
        }
        return false;
    }

    public PrimitiveBuilder getPrimitiveBuilder(IChunk chunk) {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(shaderSource.vertexFormat);
        BlockPos p = new BlockPos(0, 0, 0);
        for(int x = 0; x < chunk.getWidth(); x++) {
            for(int y = chunk.getHeight() - 1; y >= 0; y--) {
                for(int z = 0; z < chunk.getWidth(); z++) {
                    IBlockState block = chunk.getBlockState1(x,y,z);
                    if(block.getBlock() != Blocks.AIR && block.getBlock().blockProperties.translucent) {
                        for (int a = 0; a < 6; a++) {
                            p.set(x, y, z).add(Block.getBlockPosImmutable(block.getBlock().getSide(block, a)));
                            IBlockState newState;
                            if (!p.inRange(0, 0, 0, chunk.getWidth() - 1, chunk.getHeight() - 1, chunk.getWidth() - 1)) {
                                newState = chunk.getWorld().getBlockState(p.add(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ()));
                            } else {
                                newState = chunk.getBlockState1(p);
                            }
                            if (newState.getBlock() != block.getBlock()) {
                                block.getBlock().addVertices(primitiveBuilder, a, 1f, block, p.set(x, y, z), x, z);
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
        int meshID = (int) window.getGraphicsEngine().getDefaultImpl().createMesh(graphicsContext, primitiveBuilder.toVertexMesh());
        meshes.setChunk(chunk.getX(),chunk.getY(),chunk.getZ(),new MeshHolder().set(meshID,primitiveBuilder.indices.size()));
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        shaderSource = gameInstance.SHADERS.get("ourcraft:world_shader");
    }
}


