package dev.hilligans.ourcraft.Entity.Entities;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Item.BlockItem;
import dev.hilligans.ourcraft.Item.Item;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Ourcraft;

import static org.lwjgl.opengl.GL11.*;

public class ItemEntity extends Entity {

    Block block;
    int id = -1;
    int verticesCount;
    public ItemStack itemStack;
    public int pickupDelay;


    public ItemEntity(double x, double y, double z, int id, Block block) {
        super(x, y, z, id);
        this.block = block;
        type = 1;
        velY = 0.30f;
        itemStack = new ItemStack(Ourcraft.GAME_INSTANCE.ITEMS.MAPPED_ELEMENTS.get(block.name),(byte)1);
        boundingBox = new BoundingBox(-0.25f,-0.25f,-0.25f,0.25f,0.25f,0.25f);
        pickupDelay = 10;
    }

    public ItemEntity(double x, double y, double z, int id, ItemStack itemStack) {
        super(x,y,z,id);
        this.type = 1;
        this.itemStack = itemStack;
        boundingBox = new BoundingBox(-0.25f,-0.25f,-0.25f,0.25f,0.25f,0.25f);
        if(itemStack.item instanceof BlockItem) {
            this.block = Ourcraft.GAME_INSTANCE.getBlock(itemStack.item.name);
        }
        pickupDelay = 40;
    }

    public ItemEntity(PacketData packetData) {
        super(packetData);
        type = 1;
        Item item = Ourcraft.GAME_INSTANCE.getItem(packetData.readInt());
        if(item instanceof BlockItem) {
            this.block = Ourcraft.GAME_INSTANCE.getBlock(item.name);
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

        this.velY -= 0.07f;
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
        /*matrixStack.push();
        if(id == -1) {
            createMesh();
        }
        //glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        glBindVertexArray(id);
        glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);
        matrixStack.translateMinusOffset((float) getX(), (float) getY(), (float) getZ());
        matrixStack.rotate((float) Math.toRadians(ClientMain.getClient().getRenderTime() / 3f),new Vector3f(0,1,0));
        matrixStack.translate(-0.25f,0,-0.25f);
        if((ClientMain.getClient().getRenderTime() % 400) > 200) {
            matrixStack.translate(0, (float) (-0.00125 * (200 - (ClientMain.getClient().getRenderTime() % 200)) + 0.125f), 0);
        } else {
            matrixStack.translate(0, (float) (-0.00125 * (ClientMain.getClient().getRenderTime() % 200) + 0.125f), 0);
        }
        //matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        //glDrawElements(GL_TRIANGLES, verticesCount,GL_UNSIGNED_INT,0);
        matrixStack.pop();

         */
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
