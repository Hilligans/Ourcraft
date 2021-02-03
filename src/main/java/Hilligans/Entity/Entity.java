package Hilligans.Entity;

import Hilligans.Client.MatrixStack;
import org.joml.Matrix4f;

public abstract class Entity {

    public float x,y,z,pitch,yaw;

    public int id;

    public Entity(int id) {
        this.id = 0;
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

    static int iD = 0;

    public static int getNewId() {
        return iD++;
    }


    public abstract void render(MatrixStack matrixStack);






}
