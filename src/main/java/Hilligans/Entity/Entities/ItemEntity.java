package Hilligans.Entity.Entities;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Entity.Entity;
import Hilligans.Item.BlockItem;
import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;
import Hilligans.Network.PacketData;
import Hilligans.Util.Vector5f;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class ItemEntity extends Entity {

    Block block;
    int id = -1;
    int verticesCount;
    public ItemStack itemStack;
    public int pickupDelay;


    public ItemEntity(float x, float y, float z, int id, Block block) {
        super(x, y, z, id);
        this.block = block;
        type = 1;
       // boundingBox = new BoundingBox(-0.125f,-0.125f,-0.125f,0.125f,0.125f,0.125f);
        velY = 0.30f;
        itemStack = new ItemStack(Items.HASHED_ITEMS.get(block.name),(byte)1);
        boundingBox = new BoundingBox(-0.25f,-0.25f,-0.25f,0.25f,0.25f,0.25f);
        pickupDelay = 10;
    }

    public ItemEntity(float x, float y, float z, int id, ItemStack itemStack) {
        super(x,y,z,id);
        this.type = 1;
        this.itemStack = itemStack;
        boundingBox = new BoundingBox(-0.25f,-0.25f,-0.25f,0.25f,0.25f,0.25f);
        if(itemStack.item instanceof BlockItem) {
            this.block = Blocks.MAPPED_BLOCKS.get(itemStack.item.name);
        }
        pickupDelay = 40;
    }

    public ItemEntity(PacketData packetData) {
        super(packetData);
        type = 1;
        block = Blocks.getBlockWithID(packetData.readInt());
        boundingBox = new BoundingBox(-0.25f,-0.25f,-0.25f,0.25f,0.25f,0.25f);
        itemStack = new ItemStack(Items.HASHED_ITEMS.get(block.name),(byte)1);
        //velY = 1f;
    }

    @Override
    public void tick() {
        //this.velX = -0.2f;
        if(pickupDelay > 0) {
            pickupDelay--;
        }

        this.velY += -0.07f;
        if(this.velY < -0.4) {
            this.velY = -0.4f;
        }
        //this.velZ = -0.1f;
       // System.out.println("x " + x + " z " + z);
        move();
    }

    @Override
    public void writeData(PacketData packetData) {
        super.writeData(packetData);
        packetData.writeInt(block.id);
    }

    @Override
    public void render(MatrixStack matrixStack) {
        if(id == -1) {
            createMesh();
        }
        glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);
        matrixStack.translateMinusOffset(x,y,z);
        matrixStack.applyTransformation();
        glBindVertexArray(id);
        glDrawElements(GL_TRIANGLES, verticesCount * 3 / 2,GL_UNSIGNED_INT,0);
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
            vertices.addAll(Arrays.asList(CubeManager.getVertices(block.blockProperties.blockTextureManager,x,0.25f)));
            indices.addAll(Arrays.asList(block.getIndices(x,4 * x)));
        }
        verticesCount = vertices.size();
        id = VAOManager.createVAO(VAOManager.convertVertices(vertices,false),VAOManager.convertIndices(indices));

    }





}
