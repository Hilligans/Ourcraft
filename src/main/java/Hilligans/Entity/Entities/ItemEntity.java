package Hilligans.Entity.Entities;

import Hilligans.Blocks.Block;
import Hilligans.Blocks.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Client.Rendering.World.VAOManager;
import Hilligans.Entity.Entity;
import Hilligans.Network.PacketData;
import Hilligans.Util.Vector5f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ItemEntity extends Entity {

    Block block;
    int id = -1;
    int verticesCount;

    public ItemEntity(float x, float y, float z, int id, Block block) {
        super(x, y, z, id);
        this.block = block;
        type = 1;
    }

    public ItemEntity(PacketData packetData) {
        super(packetData);
        type = 1;
        block = Blocks.getBlockWithID(packetData.readInt());
    }

    @Override
    public void writeData(PacketData packetData) {
        super.writeData(packetData);
        packetData.writeInt(block.id);
    }

    @Override
    public void render(MatrixStack matrixStack) {
       // System.out.print("rendering");
        matrixStack.push();
        if(id == -1) {
            createMesh();
        }
        matrixStack.translate(x,y,z);
        matrixStack.applyTransformation();
        glBindVertexArray(id);
        glDrawElements(GL_TRIANGLES, verticesCount * 3 / 2,GL_UNSIGNED_INT,0);


        matrixStack.pop();
    }

    @Override
    public void destroy() {
        if(id != -1) {
            VAOManager.destroyBuffer(id);
        }
        super.destroy();
    }

    public void createMesh() {
        ArrayList<Vector5f> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        for(int x = 0; x < 6; x++) {
            vertices.addAll(Arrays.asList(CubeManager.getVertices(block.blockTextureManager,x,0.25f)));
            indices.addAll(Arrays.asList(block.getIndices(x,4 * x)));
        }
        verticesCount = vertices.size();
        id = VAOManager.createVAO(VAOManager.convertVertices(vertices,false),VAOManager.convertIndices(indices));

    }





}
