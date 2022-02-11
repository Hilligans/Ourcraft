package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public abstract class WorldCamera implements ICamera {

    public double x;
    public double y;
    public double z;

    public float pitch;
    public float yaw;

    public float velX;
    public float velY;
    public float velZ;

    /**
     0 = normal camera
     1 = camera behind player
     2 = camera looking at player
     */
    public int thirdPersonMode = 0;

    @Override
    public void move(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

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

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public void tick() {

    }

    @Override
    public void setMotion(float velX, float velY, float velZ) {
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }

    @Override
    public void addMotion(float velX, float velY, float velZ) {

    }

    @Override
    public Vector3f getMotion() {
        return new Vector3f(velX,velY,velZ);
    }

    public Vector3d getLookVector() {
        return new Vector3d((Math.cos(Camera.yaw) * Math.cos(Camera.pitch)), (Math.sin(Camera.pitch)), (Math.sin(Camera.yaw) * Math.cos(Camera.pitch)));
    }

    @Override
    public MatrixStack getMatrixStack(int W, int H, int x, int y) {
        Matrix4d perspective = getPerspective(W, H, x, y);
        perspective.mul(getView());
        if(thirdPersonMode == 2) {
            perspective.lookAt(getCameraPos().add(getLookVector().negate()), null, cameraUp());
        } else {
            perspective.lookAt(getCameraPos().add(getLookVector()), null, cameraUp());
        }
        return new MatrixStack(perspective);
    }

    @Override
    public Matrix4d getPerspective(int W, int H, int x, int y) {
        return null;
    }

    @Override
    public Matrix4d getView() {
        return null;
    }
}
