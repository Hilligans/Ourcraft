package Hilligans.World;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Data.Other.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Util.Settings;
import Hilligans.Util.Vector5f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class SubChunk {

    int id = -2;
    int verticesCount = -1;
    World world;
    long y;
    long x;
    long z;

    BlockState[][][] blocks = new BlockState[16][16][16];

    public SubChunk(World world, long X, long Y, long Z) {
        this.world = world;
        this.y = Y;
        this.x = X;
        this.z = Z;

        for(int x = 0; x < 16; x++) {
            for(int i = 0; i < 16; i++) {
                for(int z = 0; z < 16; z++) {
                    blocks[x][i][z] = Blocks.AIR.getDefaultState();
                }
            }
        }
    }

    public void createMesh1() {
        PrimitiveBuilder primitiveBuilder = getMeshBuilder();
        verticesCount = primitiveBuilder.indices.size();
        id = VAOManager.createVAO(primitiveBuilder);
    }

    public PrimitiveBuilder getMeshBuilder() {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES,ShaderManager.worldShader);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    BlockState block = blocks[x][y][z];
                    for(int a = 0; a < 6; a++) {
                        if(block.getBlock() != Blocks.AIR) {
                            BlockState blockState = getBlock(new BlockPos(x, y, z).add(Block.getBlockPos(block.getBlock().getSide(block,a))));
                            if (blockState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != blockState.getBlock())) {
                                block.getBlock().addVertices(primitiveBuilder,a,1.0f,block,new BlockPos(x + this.x,y + this.y,z + this.z),x,z);
                            }
                        }
                    }
                }
            }
        }
        return primitiveBuilder;
    }



    public void destroy() {
        if(id != -1 && id != -2 && id != -3) {
            VAOManager.destroyBuffer(id);
            id = -1;
        }
    }

    public BlockState getBlock(BlockPos pos) {
        if(pos.isSubChunkValid()) {
            return blocks[pos.x][pos.y][pos.z];
        }
        pos.y += y;
        pos.x += x;
        pos.z += z;

        return world.getBlockState(pos);
    }

    public BlockState getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }


    public void setBlockState(int x, int y, int z, BlockState blockState) {
        blocks[x & 15][y & 15][z & 15].getBlock().onBreak(world,new BlockPos(x,y,z));
        blocks[x & 15][y & 15][z & 15] = blockState;
    }

    public void updateBlock(BlockPos pos) {
        blocks[pos.x & 15][pos.y & 15][pos.z & 15].getBlock().onUpdate(world,pos);
    }

    public void renderMesh(MatrixStack matrixStack) {

        if (id == -2) {
            if (world instanceof ClientWorld) {
                ((ClientWorld) world).queuedChunks.add(this);
                id = -3;
            } else {
                id = -1;
            }
        }
        if (id == -1) {
            createMesh1();
        }

        if (verticesCount != 0) {
            GL30.glBindVertexArray(id);
            matrixStack.push();
            matrixStack.applyTransformation(ShaderManager.worldShader.shader);
            glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);
            matrixStack.pop();
        }
    }
}
