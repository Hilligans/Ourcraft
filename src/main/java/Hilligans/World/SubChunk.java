package Hilligans.World;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.VAOManager;
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
        int spot = 0;
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    BlockState block = blocks[x][y][z];
                    for(int a = 0; a < 6; a++) {
                        if(block.getBlock() != Blocks.AIR) {
                            BlockState blockState = getBlock(new BlockPos(x, y, z).add(Block.getBlockPos(a)));
                            if (blockState.getBlock().transparentTexture && (Settings.renderSameTransparent || block.getBlock() != blockState.getBlock())) {
                                indices.addAll(Arrays.asList(block.getBlock().getIndices(a,spot * 4)));
                                Vector5f[] vector5fs = block.getBlock().getVertices(a,block);
                                for(Vector5f vector5f : vector5fs) {
                                    vertices.add(vector5f.addX(x).addY(y + this.y).addZ(z));
                                }
                                spot++;

                            }
                        }
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
        verticesCount = wholeMesh.length / 9 * 5;
        //id = VAOManager.createVAO(wholeMesh,wholeIndices);


        //float[] wholeMesh = VAOManager.convertVertices(vertices,true);
       // int[] wholeIndices = VAOManager.convertIndices(indices);
       // verticesCount = wholeMesh.length;
        id = VAOManager.createColorVAO(wholeMesh,wholeIndices);

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

    public void renderMesh(MatrixStack matrixStack) {

        if(id == -1) {
            createMesh();
        }

      //  if(y == 64) {

            GL30.glBindVertexArray(id);
            matrixStack.push();
            matrixStack.applyTransformation(ClientMain.colorShader);
            glDrawElements(GL_TRIANGLES, verticesCount * 3 / 10, GL_UNSIGNED_INT, 0);
            matrixStack.pop();
      //    }
    }
}
