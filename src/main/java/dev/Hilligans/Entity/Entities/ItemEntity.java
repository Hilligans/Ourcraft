package dev.Hilligans.Entity.Entities;

import dev.Hilligans.Block.Block;
import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ClientMain;
import dev.Hilligans.Data.Other.BlockPos;
import dev.Hilligans.Data.Other.BoundingBox;
import dev.Hilligans.Entity.Entity;
import dev.Hilligans.Item.BlockItem;
import dev.Hilligans.Item.Item;
import dev.Hilligans.Item.ItemStack;
import dev.Hilligans.Item.Items;
import dev.Hilligans.Network.PacketData;
import dev.Hilligans.Ourcraft;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
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
            this.block = Ourcraft.GAME_INSTANCE.MAPPED_BLOCKS.get(itemStack.item.name);
        }
        pickupDelay = 40;
    }

    public ItemEntity(PacketData packetData) {
        super(packetData);
        type = 1;
        Item item = Items.getItem(packetData.readInt());
        if(item instanceof BlockItem) {
            this.block = Ourcraft.GAME_INSTANCE.MAPPED_BLOCKS.get(item.name);
        }
        boundingBox = new BoundingBox(-0.25f,-0.25f,-0.25f,0.25f,0.25f,0.25f);
        itemStack = new ItemStack(item,(byte)1);
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
        move();
    }

    @Override
    public void writeData(PacketData packetData) {
        super.writeData(packetData);
        packetData.writeInt(itemStack.item.id);
    }

    @Override
    public void render(MatrixStack matrixStack) {
        matrixStack.push();
        if(id == -1) {
            createMesh();
        }
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        glBindVertexArray(id);
        glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);
        matrixStack.translateMinusOffset(x,y,z);
        matrixStack.rotate((float) Math.toRadians(ClientMain.getClient().getRenderTime() / 3f),new Vector3f(0,1,0));
        matrixStack.translate(-0.25f,0,-0.25f);
        if((ClientMain.getClient().getRenderTime() % 400) > 200) {
            matrixStack.translate(0, (float) (-0.00125 * (200 - (ClientMain.getClient().getRenderTime() % 200)) + 0.125f), 0);
        } else {
            matrixStack.translate(0, (float) (-0.00125 * (ClientMain.getClient().getRenderTime() % 200) + 0.125f), 0);
        }
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        glDrawElements(GL_TRIANGLES, verticesCount,GL_UNSIGNED_INT,0);
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
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
        if(block != null) {
            for (int x = 0; x < 6; x++) {
                block.addVertices(primitiveBuilder,x,0.5f,block.getDefaultState(),new BlockPos(0,0,0),0,0);
            }
            verticesCount = primitiveBuilder.indices.size();
            id = VAOManager.createVAO(primitiveBuilder);
        }
    }





}
