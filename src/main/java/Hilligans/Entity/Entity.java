package Hilligans.Entity;

import Hilligans.Client.MatrixStack;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.PacketData;
import org.joml.Matrix4f;

import java.util.ArrayList;

public abstract class Entity {

    public float x,y,z,pitch,yaw;

    public int id;
    public int type = 0;

    public Entity(int id) {
        this.id = id;
        x = 0;
        y = 0;
        z = 0;
    }

    public Entity(float x, float y, float z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public Entity(PacketData packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        id = packetData.readInt();
    }

    public Entity setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Entity setRot(float pitch, float yaw) {
       this.pitch = pitch;
       this.yaw = yaw;
        return this;
    }

    public void writeData(PacketData packetData) {
        packetData.writeInt(type);
        packetData.writeFloat(x);
        packetData.writeFloat(y);
        packetData.writeFloat(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(id);
    }

    public abstract void render(MatrixStack matrixStack);

    public void destroy() {}

    static int iD = 0;

    public static int getNewId() {
        return iD++;
    }

    public static ArrayList<EntityFetcher> entities = new ArrayList<>();

    public static void register() {
        entities.add(PlayerEntity::new);
        entities.add(ItemEntity::new);
      //  entities.add(PlayerEntity::new);

    }



}
