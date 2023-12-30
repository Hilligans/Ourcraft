package dev.hilligans.ourcraft.client.rendering.graphics.tasks;

import dev.hilligans.ourcraft.client.Camera;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ICamera;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.data.primitives.FloatList;
import dev.hilligans.ourcraft.util.Loops;
import dev.hilligans.ourcraft.util.TriConsumer;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

import java.awt.*;
import java.util.function.Consumer;

public class ChunkDebugRenderTask extends RenderTaskSource {

    public ShaderSource shaderSource;

    public ChunkDebugRenderTask() {
        super("chunk_debug_render_task", "ourcraft:debug_world_renderer");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                if (client.getPlayerData().debugChunkRendering) {
                    int renderYDist = client.renderYDistance;

                    Vector3d pos = window.getCamera().getCameraPos();
                    IWorld world = client.getWorld();

                    int chunkWidth = world.getChunkWidth();
                    int chunkHeight = world.getChunkHeight();

                    float minX = 0;
                    float minY = 0;
                    float minZ = 0;
                    float maxX = chunkWidth;
                    float maxY = chunkHeight;
                    float maxZ = chunkWidth;

                    PrimitiveBuilder meshBuilder = new PrimitiveBuilder(shaderSource);
                    IDefaultEngineImpl<?, ?> impl = engine.getDefaultImpl();
                    meshBuilder.addBoundingBox(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ), floatList -> floatList.add(1, 1, 1, 1));

                    long mesh = impl.createMesh(graphicsContext, meshBuilder.toVertexMesh());

                    Vector3i playerChunkPos = new Vector3i(Math.floorDiv((int)pos.x, chunkWidth), Math.floorDiv((int)pos.y, chunkHeight), Math.floorDiv((int)pos.z, chunkWidth));

                    int rDist = Math.min(client.renderDistance, 4);
                    int rYDist = Math.min(client.renderYDistance, 3);

                    Loops.furthestToNearestLoop(rDist, rYDist, rDist, playerChunkPos, (chunkX, chunkY, chunkZ) -> {
                        IChunk chunk = getChunk(chunkX, chunkY, chunkZ, client.getWorld());
                        worldStack.push();
                        worldStack.translate((chunkX) * chunkWidth, (chunkY) * chunkHeight, (chunkZ) * chunkWidth);
                        if (chunk != null) {
                            if(chunkX-playerChunkPos.x == 0 && chunkY-playerChunkPos.y == 0 && chunkZ-playerChunkPos.z == 0) {
                                worldStack.setColor(0, 255, 0, 255);
                            } else {
                                worldStack.setColor(255, 255, 0, 255);
                            }
                            impl.uploadMatrix(graphicsContext, worldStack, shaderSource);
                            impl.drawMesh(graphicsContext, worldStack, mesh, 0, meshBuilder.indices.size());
                        }
                        worldStack.pop();
                    });
                    impl.destroyMesh(graphicsContext, mesh);
                }
            }

            IChunk getChunk(int chunkX, int chunkY, int chunkZ, IWorld world) {
                return world.getChunk((long) chunkX * world.getChunkContainer().getChunkWidth(), (long) chunkY * world.getChunkContainer().getChunkHeight(), (long) chunkZ * world.getChunkContainer().getChunkWidth());
            }

            @Override
            public PipelineState getPipelineState() {
                return null;
            }
        };
    }

    @Override
    public void load(GameInstance gameInstance) {
        super.load(gameInstance);
        shaderSource = gameInstance.SHADERS.getExcept("ourcraft:position_color_lines_shader");
    }
}
