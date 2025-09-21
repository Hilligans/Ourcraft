package dev.hilligans.engine2d.client;

import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.ICamera;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;

public class Camera2D implements ICamera {

    RenderWindow window;

    double x, y;
    float yaw;

    public float worldWidth;
    public float worldHeight;

    public Camera2D(RenderWindow window, float worldWidth, float worldHeight) {
        this.window = window;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public static final float maxYaw = (float) (2 * Math.PI);

    @Override
    public @NotNull RenderWindow getWindow() {
        return window;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(float x, float y, float z) {
        this.x += x;
        this.y += y;
    }

    @Override
    public Vector3d getPosition() {
        return new Vector3d(x, y, 0);
    }

    @Override
    public void setRotation(float pitch, float yaw) {
        this.yaw = yaw;
    }

    @Override
    public void addRotation(float pitch, float yaw) {
        this.yaw += yaw;
        while(yaw > maxYaw) {
            yaw -= maxYaw;
        }
        while(yaw < maxYaw) {
            yaw += maxYaw;
        }
    }

    @Override
    public int getSensitivity() {
        return 0;
    }

    @Override
    public void setSensitivity(int val) {

    }

    @Override
    public int getFOV() {
        return 0;
    }

    @Override
    public void setFOV(int fov) {

    }

    @Override
    public Vector2f getRotation() {
        return new Vector2f(0, yaw);
    }

    @Override
    public float getPitch() {
        return 0;
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

    }

    @Override
    public void addMotion(float velX, float velY, float velZ) {

    }

    @Override
    public Vector3f getMotion() {
        return null;
    }

    @Override
    public MatrixStack getMatrixStack(int W, int H, int x, int y) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.translate(W - 1 - 2*x, H - 1 - 2*y, 0).scale(W, H, 1).ortho(0, getWindowWidth(), getWindowHeight(),0,-1,20000);

        float scale = getScale();

        int distanceX = (int)getInsetX();
        int distanceY = (int)getInsetY();

        matrix4f.translate(distanceX, distanceY, 0);
        matrix4f.scale(scale);

        return new MatrixStack(matrix4f);
    }

    public float getInsetX() {
        return (getWindowWidth()- worldWidth*getScale())/2;
    }

    public float getInsetY() {
        return (getWindowHeight()- worldHeight*getScale())/2;
    }

    public float getScale() {
        float scaleX = getWindowWidth() / worldWidth;
        float scaleY = getWindowHeight() / worldHeight;

        return Math.min(scaleX, scaleY);
    }

    @Override
    public Matrix4d getView() {
        return null;
    }

    @Override
    public Vector3d getLookVector() {
        return null;
    }

    @Override
    public void savePosition(Vector3d vector3d) {

    }

    @Override
    public Vector3d getSavedPosition() {
        return null;
    }
}
