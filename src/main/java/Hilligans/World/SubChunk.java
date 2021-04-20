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
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class SubChunk {

    int id = -1;
    int verticesCount = -1;
    World world;
    int y;
    int x;
    int z;

    BlockState[][][] blocks = new BlockState[16][16][16];

    public SubChunk(World world, int X, int Y, int Z) {
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

    public void createMesh() {
        ArrayList<Vector5f> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
       // int spot = 0;
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    BlockState block = blocks[x][y][z];
                    for(int a = 0; a < 6; a++) {
                        if(block.getBlock() != Blocks.AIR) {
                            BlockState blockState = getBlock(new BlockPos(x, y, z).add(Block.getBlockPos(a)));
                            if (blockState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != blockState.getBlock())) {
                                Vector5f[] vector5fs = block.getBlock().getVertices(a,block, new BlockPos(x + this.x,y + this.y,z + this.z));
                                indices.addAll(Arrays.asList(block.getBlock().getIndices(a,vertices.size())));
                                for(Vector5f vector5f : vector5fs) {
                                    vertices.add(vector5f.addX(x).addY(y + this.y).addZ(z));
                                }

                            }
                        }
                    }
                    Vector5f[] vector5fs = block.getBlock().getVertices(6,block, new BlockPos(x + this.x,y + this.y,z + this.z));
                    indices.addAll(Arrays.asList(block.getBlock().getIndices(6,vertices.size())));
                    for(Vector5f vector5f : vector5fs) {
                        vertices.add(vector5f.addX(x).addY(y + this.y).addZ(z));
                    }
                }
            }
        }



        float[] wholeMesh = new float[vertices.size() * 9];
        int[] wholeIndices = new int[indices.size()];
        int x = 0;
        for(Vector5f vector5f : vertices) {
            vector5f.addToList(wholeMesh,x * 9);
            x++;
        }
        x = 0;

        for(Integer a : indices) {
            wholeIndices[x] = a;
            x++;
        }
        verticesCount = indices.size();
        //id = VAOManager.createVAO(wholeMesh,wholeIndices);


        //float[] wholeMesh = VAOManager.convertVertices(vertices,true);
       // int[] wholeIndices = VAOManager.convertIndices(indices);
       // verticesCount = wholeMesh.length;
        id = VAOManager.createColorVAO(wholeMesh,wholeIndices);

    }

    public void createMesh1() {
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES,ShaderManager.worldShader);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    BlockState block = blocks[x][y][z];
                    for(int a = 0; a < 6; a++) {
                        if(block.getBlock() != Blocks.AIR) {
                            BlockState blockState = getBlock(new BlockPos(x, y, z).add(Block.getBlockPos(a)));
                            if (blockState.getBlock().blockProperties.transparent && (Settings.renderSameTransparent || block.getBlock() != blockState.getBlock())) {
                                block.getBlock().addVertices(primitiveBuilder,a,1.0f,block,new BlockPos(x + this.x,y + this.y,z + this.z),x,z);
                            }
                        }
                    }
                    block.getBlock().addVertices(primitiveBuilder,6,1.0f,block,new BlockPos(x + this.x,y + this.y,z + this.z),x,z);
                }
            }
        }
        verticesCount = primitiveBuilder.indices.size();
        id = VAOManager.createVAO(primitiveBuilder);
    }



    public void destroy() {
        if(id != -1) {
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

        if(id == -1) {
            createMesh1();
        }

      //  if(y == 64) {

            GL30.glBindVertexArray(id);
            matrixStack.push();
            //matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
            matrixStack.applyTransformation(ShaderManager.worldShader.shader);
            glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);
            matrixStack.pop();
      //    }
    }
}
