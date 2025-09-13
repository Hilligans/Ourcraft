package dev.hilligans.ourcraft.entity.living.entities;

import dev.hilligans.ourcraft.client.rendering.world.managers.VAOManager;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.LivingEntity;
import dev.hilligans.engine.network.IPacketByteArray;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.util.Vector5f;
import org.joml.Vector3d;

public class PlayerEntity extends LivingEntity {

    int textureId = -1;
    int verticesCount;

    public Inventory inventory;

    public static int imageId;

    public BoundingBox itemPickupBox = new BoundingBox(-1.3f,-1.9f,-1.3f,1.3f,0.0f,1.3f);
    public ServerPlayerData serverPlayerData;

    public PlayerEntity(float x, float y, float z,int id) {
        super(x,y,z,id,20);
        type = 0;
        boundingBox =  new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);
        inventory = new Inventory(Settings.playerInventorySize);
    }

    public PlayerEntity(IPacketByteArray packetData) {
        super(packetData);
        boundingBox =  new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);
    }

    @Override
    public void tick() {
        boolean updateInventory = false;

        /*
        for(Entity entity : ServerMain.getWorld(dimension).entities.values()) {
            if(entity instanceof ItemEntity) {
                if (entity.boundingBox.intersectsBox(itemPickupBox, new Vector3d(entity.getX(), entity.getY(), entity.getZ()), new Vector3d(getX(), getY(), getZ()))) {
                    if(((ItemEntity)entity).pickupDelay == 0) {
                        ItemStack itemStack = ((ItemEntity) entity).itemStack;
                        int count = itemStack.count;
                        if (inventory.addItem(itemStack)) {
                            ServerMain.getWorld(dimension).removeEntity(entity.id);
                        }
                        if (count != itemStack.count) {
                            updateInventory = true;
                        }
                    }
                }
            }
        }
         */
        if(updateInventory) {
            inventory.age++;
        //    ServerMain.getServer().sendPacket(new SUpdateInventory(inventory),this);
        }
    }

    public Vector3d getForeWard() {
        return new Vector3d((float) (Math.cos(yaw) * Math.cos(pitch)),(float)(Math.sin(pitch)),(float)(Math.sin(yaw) * Math.cos(pitch)));
    }

    public PlayerEntity setPlayerData(ServerPlayerData serverPlayerData) {
        this.serverPlayerData = serverPlayerData;
        return this;
    }

    public ServerPlayerData getPlayerData() {
        return serverPlayerData;
    }

    @Override
    public void destroy() {
        if(id != -1) {
            VAOManager.destroyBuffer(id);
        }
        super.destroy();
    }

    private Vector5f[] getVertices(int side) {

        int id = 4;
        float minX = 0.25f;
        float maxX = 0.5f;
        float minY = 0;
        float maxY = 1;

        if(side == 2) {
            minX = 0;
            maxX = 0.25f;
        }

        switch (side)  {
            case 0:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,-0.5f,maxX,maxY),
                        new Vector5f(0.5f, -0.5f, -0.5f, maxX,minY),
                        new Vector5f(-0.5f,-0.5f,-0.5f,minX,minY),
                        new Vector5f(-0.5f,0.5f,-0.5f,minX,maxY)};
            case 1:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,0.5f,maxX,maxY),
                        new Vector5f(0.5f, -0.5f, 0.5f, maxX,minY),
                        new Vector5f(-0.5f,-0.5f,0.5f,minX,minY),
                        new Vector5f(-0.5f,0.5f,0.5f,minX,maxY)};
            case 2:
                return new Vector5f[] { new Vector5f(-0.5f,0.5f,0.5f,maxX,maxY),
                        new Vector5f(-0.5f,-0.5f,0.5f,maxX,minY),
                        new Vector5f(-0.5f,-0.5f,-0.5f,minX,minY),
                        new Vector5f(-0.5f,0.5f,-0.5f,minX,maxY)};
            case 3:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,0.5f,maxX,maxY),
                        new Vector5f(0.5f,-0.5f,0.5f,maxX,minY),
                        new Vector5f(0.5f,-0.5f,-0.5f,minX,minY),
                        new Vector5f(0.5f,0.5f,-0.5f,minX,maxY)};
            case 5:
                return new Vector5f[] { new Vector5f(0.5f,0.5f,0.5f,minX,minY),
                        new Vector5f(0.5f,0.5f,-0.5f,maxX,minY),
                        new Vector5f(-0.5f,0.5f,-0.5f,maxX,maxY),
                        new Vector5f(-0.5f,0.5f,0.5f,minX,maxY)};
            default:
                return new Vector5f[] { new Vector5f(0.5f,-0.5f,0.5f,minX,minY),
                        new Vector5f(0.5f,-0.5f,-0.5f,maxX,minY),
                        new Vector5f(-0.5f,-0.5f,-0.5f,maxX,maxY),
                        new Vector5f(-0.5f,-0.5f,0.5f,minX,maxY)};
        }
    }

    public Integer[] getIndices(int side, int spot) {
        switch (side) {
            case 0:
            case 5:
            case 3:
                return new Integer[] {spot,spot + 1,spot + 2,spot,spot + 2,spot + 3};
            default:
                return new Integer[]{spot,spot + 2, spot + 1, spot, spot + 3, spot + 2};
        }
    }


}
