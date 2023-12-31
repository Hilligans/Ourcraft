package dev.hilligans.ourcraft.client.rendering.graphics.tasks;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.culling.CullingEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.MeshHolder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.TextAtlas;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
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
import java.util.function.Consumer;


public class WorldRenderTask extends RenderTaskSource {

    public WorldRenderTask() {
        super("new_world_render_task", "ourcraft:new_solid_world_renderer");
    }

    public ShaderSource shaderSource;

    public CullingEngine cullingEngine;

    public ExecutorService chunkBuilder = Executors.newFixedThreadPool(2);
    TextAtlas textAtlas;

    @Override
    public RenderTask getDefaultTask() {

        IThreeDContainer<MeshHolder> meshes = new EmptyContainer<>();

        return new RenderTask() {

            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IWorld world = client.newClientWorld;
                //engine.getDefaultImpl().setState(window, graphicsContext, new PipelineState().setDepth(true));
                Vector3d pos = window.camera.getSavedPosition();
                int chunkWidth = world.getChunkContainer().getChunkWidth();
                int chunkHeight = world.getChunkContainer().getChunkHeight();
                int renderYDist = client.renderYDistance;
                Vector3i playerChunkPos = new Vector3i(Math.floorDiv((int)pos.x, chunkWidth), Math.floorDiv((int)pos.y, chunkHeight), Math.floorDiv((int)pos.z, chunkWidth));
                if (client.renderWorld) {
                    engine.getDefaultImpl().bindPipeline(graphicsContext, shaderSource.program);
                    engine.getDefaultImpl().bindTexture(graphicsContext, textAtlas.texture);

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
                for (Tuple<IChunk, PrimitiveBuilder> tuple : primitiveBuilders) {
                    asyncedChunks.remove(((tuple.getTypeA().getX()) << 32) | (tuple.getTypeA().getZ() & 0xffffffffL));
                    tuple.typeB.setVertexFormat(shaderSource.vertexFormat);
                    int meshID = (int) window.getGraphicsEngine().getDefaultImpl().createMesh(graphicsContext, tuple.typeB.toVertexMesh());
                    meshes.setChunk(tuple.getTypeA().getX(), tuple.getTypeA().getY(), tuple.getTypeA().getZ(), new MeshHolder().set(meshID, tuple.getTypeB().indices.size()));
                    primitiveBuilders.remove(tuple);
                }

                try (var $$ = graphicsContext.getSection().startSection("draw_chunk")) {
                    IChunk chunk = getChunk(x, y, z, world);
                    if (chunk != null) {
                        MeshHolder meshHolder = getMesh(x, y, z);
                        if (chunk.isDirty() || meshHolder == null) {
                            if ((Math.abs(x - playerChunkPos.x) < rebuildDistance && Math.abs(y - playerChunkPos.y) < rebuildDistance && Math.abs(z - playerChunkPos.z) < rebuildDistance)) {
                                buildMesh(window, graphicsContext, chunk);
                            }
                        }
                        if (meshHolder != null) {
                            if (meshHolder.id != -1 && meshHolder.length != 0) {
                                matrixStack.updateFrustum();
                                if (matrixStack.frustumIntersection.testAab((x) * chunkWidth, (y) * chunkHeight, (z) * chunkWidth, (x + 1) * chunkWidth, (y + 1) * chunkHeight, (z + 1) * chunkWidth)) {
                                    matrixStack.push();
                                    matrixStack.translate(x * chunkWidth, y * chunkHeight, z * chunkWidth);

                                    engine.getDefaultImpl().uploadMatrix(graphicsContext, matrixStack, shaderSource);
                                    try (var $ = graphicsContext.getSection().startSection("submitChunkDrawCall")) {
                                        engine.getDefaultImpl().drawMesh(graphicsContext, matrixStack, meshHolder.getId(), meshHolder.index, meshHolder.length);
                                    }
                                    matrixStack.pop();
                                }
                            }
                        }
                    }
                }
            }

            public ConcurrentLinkedQueue<Tuple<IChunk, PrimitiveBuilder>> primitiveBuilders = new ConcurrentLinkedQueue<>();
            Long2BooleanOpenHashMap asyncedChunks = new Long2BooleanOpenHashMap();

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
                                    if (newState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != newState.getBlock()) || block.getBlock().blockProperties.alwaysRender || newState.getBlock().blockProperties.translucent) {
                                        block.getBlock().addVertices(textAtlas, primitiveBuilder, a, 1f, block, p.set(x, y, z), x, z);
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
                primitiveBuilder = getPrimitiveBuilder(chunk, primitiveBuilder);
                primitiveBuilder.setVertexFormat(shaderSource.vertexFormat);
                long meshID = window.getGraphicsEngine().getDefaultImpl().createMesh(graphicsContext, primitiveBuilder.toVertexMesh());
                MeshHolder old = meshes.setChunk(chunk.getX(),chunk.getY(),chunk.getZ(),new MeshHolder().set(meshID,primitiveBuilder.indices.size()));
                if(old != null) {
                    window.getGraphicsEngine().getDefaultImpl().destroyMesh(graphicsContext, old.id);
                }
                primitiveBuilder.size = 0;
                primitiveBuilder.vertices.size = 0;
                primitiveBuilder.indices.size = 0;
            }

            @Override
            public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
                super.load(gameInstance, graphicsEngine, graphicsContext);
            }

            @Override
            public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
                super.cleanup(gameInstance, graphicsEngine, graphicsContext);
                meshes.forEach(meshHolder -> graphicsEngine.getDefaultImpl().destroyMesh(graphicsContext, meshHolder.id));
            }

            @Override
            public PipelineState getPipelineState() {
                return new PipelineState().setDepth(true);
            }
        };
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        textAtlas = new TextAtlas(gameInstance);
        for(Block block : gameInstance.getBlocks()) {
            if(!block.blockProperties.translucent) {
                block.generateTextures(textAtlas);
            }
        }
        textAtlas.upload(graphicsEngine);
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        graphicsEngine.getDefaultImpl().destroyTexture(graphicsContext, textAtlas.texture);
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        shaderSource = gameInstance.SHADERS.get("ourcraft:world_shader");
    }
}