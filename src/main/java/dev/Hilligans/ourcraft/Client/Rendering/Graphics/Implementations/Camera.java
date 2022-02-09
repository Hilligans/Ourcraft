package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import org.joml.Vector2f;
import org.joml.Vector3d;

public abstract class Camera implements ICamera {

    public double x;
    public double y;
    public double z;

    public float pitch;
    public float yaw;

    @Override
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3d getPosition() {
        return new Vector3d(x,y,z);
    }

    @Override
    public void setRotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public Vector2f getRotation() {
        return new Vector2f(pitch,yaw);
    }
}
