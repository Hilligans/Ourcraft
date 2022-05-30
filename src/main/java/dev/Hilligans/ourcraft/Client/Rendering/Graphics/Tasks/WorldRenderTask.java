package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Culling.CullingEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.MeshHolder;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import org.joml.*;

public class WorldRenderTask extends RenderTaskSource {

    public WorldRenderTask() {
        super("world_render_task", "ourcraft:solid_world_renderer");
    }

    public ShaderSource shaderSource;
    public CullingEngine cullingEngine;

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, IGraphicsEngine<?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack) {
                if(cullingEngine == null) {
                    cullingEngine = new CullingEngine(null) {
                        @Override
                        public boolean shouldRenderChunk(Chunk chunk, ICamera camera) {
                            return true;
                        }
                    };
                }

                if (shaderSource == null) {
                    System.out.println(engine.getGameInstance().SHADERS.ELEMENTS);
                    shaderSource = engine.getGameInstance().SHADERS.get("ourcraft:world_shader");
                }
                engine.getDefaultImpl().setState(window, new PipelineState().setDepth(true));
                Vector3d pos = window.camera.getPosition();
                ClientWorld world = client.clientWorld;
                Vector3i playerChunkPos = new Vector3i((int) pos.x >> 4, 0, (int) pos.z >> 4);
                if (client.renderWorld) {
                    for (int x = 0; x < Settings.renderDistance; x++) {
                        for (int z = 0; z < Settings.renderDistance; z++) {
                            drawChunk(window, client, engine, worldStack, playerChunkPos, getChunk(x + playerChunkPos.x, z + playerChunkPos.z, world));
                            if (x != 0) {
                                drawChunk(window, client, engine, worldStack, playerChunkPos, getChunk(-x + playerChunkPos.x, z + playerChunkPos.z, world));
                                if (z != 0) {
                                    drawChunk(window, client, engine, worldStack, playerChunkPos, getChunk(-x + playerChunkPos.x, -z + playerChunkPos.z, world));
                                }
                            }
                            if (z != 0) {
                                drawChunk(window, client, engine, worldStack, playerChunkPos, getChunk(x + playerChunkPos.x, -z + playerChunkPos.z, world));
                            }
                        }
                    }
                }
            }
        };
    }

    Long2BooleanOpenHashMap map = new Long2BooleanOpenHashMap();

    Chunk getChunk(int chunkX, int chunkY, ClientWorld world) {
        Chunk c = world.getChunk(chunkX, chunkY);

        if (c == null && !map.getOrDefault(((long) chunkX << 32) ^ chunkY, false)) {
            map.put(((long) chunkX << 32) ^ chunkY, true);
            world.requestChunk(chunkX, chunkY);
        }
        return c;
    }

    void drawChunk(RenderWindow window, Client client, IGraphicsEngine<?, ?> engine, MatrixStack matrixStack, Vector3i playerChunkPos, Chunk chunk) {
        if (chunk != null) {
            MeshHolder meshHolder = chunk.getSolidMesh();
            int meshId = meshHolder.getId();
            if (meshId != -1) {
                if (matrixStack.frustumIntersection.testAab(new Vector3f((chunk.x + playerChunkPos.x) * 16, -256, (chunk.z + playerChunkPos.z) * 16), new Vector3f((chunk.x + 1 + playerChunkPos.x) * 16, 256f, (chunk.z + 1 + playerChunkPos.z) * 16))) {
                    if(cullingEngine.shouldRenderChunk(chunk, window.camera)) {
                        matrixStack.push();
                        matrixStack.translate((chunk.x + playerChunkPos.x) * 16, 0, (chunk.z + playerChunkPos.z) * 16);
                        matrixStack.applyTransformation(shaderSource.program);
                        engine.getDefaultImpl().drawMesh(window, matrixStack, engine.getGraphicsData().getWorldTexture(), shaderSource.program, meshId, meshHolder.index, meshHolder.length);
                        matrixStack.pop();
                    }
                }
            } else {
                chunk.build(engine);
            }
        }
    }
}
