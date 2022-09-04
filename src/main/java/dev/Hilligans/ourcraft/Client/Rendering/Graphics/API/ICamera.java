package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.ClientMain;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.lang.Math;

public interface ICamera {

    @NotNull
    RenderWindow getWindow();

    void setPosition(double x, double y, double z);

    default void setPosition(Vector3d pos) {
        setPosition(pos.x, pos.y, pos.z);
    }

    void move(float x, float y, float z);

    default void move(Vector3f vec) {
        move(vec.x,vec.y,vec.z);
    }

    Vector3d getPosition();

    void setRotation(float pitch, float yaw);

    default void setRotation(Vector2f rotation) {
        setRotation(rotation.x,rotation.y);
    }

    void addRotation(float pitch, float yaw);

    float getSensitivity();

    Vector2f getRotation();

    float getPitch();

    float getYaw();

    void tick();

    void setMotion(float velX, float velY, float velZ);

    default void setMotion(Vector3f vec) {
        setMotion(vec.x, vec.y, vec.z);
    }

    void addMotion(float velX, float velY, float velZ);

    Vector3f getMotion();

    default void moveForeWard(float amount) {
        move((float) (Math.cos(getYaw()) * amount), 0, (float) -(Math.sin(getYaw()) * amount));
    }

    default void moveBackWard(float amount) {
        move((float) (Math.cos(getYaw()) * amount), 0, (float) (Math.sin(getYaw()) * amount));
    }

    default void moveLeft(float amount) {
        move((float) (Math.sin(getYaw()) * amount), 0, (float) (Math.cos(getYaw()) * amount));
    }

    default void moveRight(float amount) {
        move((float) (Math.sin(getYaw()) * amount), 0, (float) -(Math.cos(getYaw()) * amount));
    }

    default void moveUp(float amount) {
        move(0,amount,0);
    }

    default MatrixStack getMatrix() {
        return getMatrixStack(1,1,0,0);
    }

    default MatrixStack getScreenStack() {
        return getScreenStack(1,1,0,0);
    }

    default Vector3d getCameraPos() {
        Vector3d vector3d = getPosition();
        double x = vector3d.x - ((int)vector3d.x >> 4);
        double y = vector3d.y - ((int)vector3d.y >> 4);
        double z = vector3d.z - ((int)vector3d.z >> 4);

        return new Vector3d(x,y,z);
    }

    MatrixStack getMatrixStack(int W, int H, int x, int y);

    default MatrixStack getScreenStack(int W, int H, int x, int y) {
        return new MatrixStack(new Matrix4d().translate(W - 1 - 2*x, H - 1 - 2*y, 0).scale(W, H, 1).ortho(0, getWindowWidth(),getWindowHeight(),0,-1,20000));
    }

    default Matrix4d getPerspective(int W, int H, int x, int y, float fov, float aspectRatio, float zNear, float zFar) {
        return new Matrix4d().translate(W - 1 - 2*x, H - 1 - 2*y, 0).scale(W, H, 1).perspective((float) Math.toRadians(fov), aspectRatio,zNear,zFar);
       // return new Matrix4d().perspective((float) Math.toRadians(fov), aspectRatio,zNear,zFar);
    }

    Matrix4d getView();

    Vector3d getLookVector();

    default Vector3d cameraUp() {
        return new Vector3d(0.0f, 1.0f, 0.0f);
    }

    void savePosition(Vector3d vector3d);

    Vector3d getSavedPosition();

    default float getWindowWidth() {
        return getWindow().getWindowWidth();
    }

    default float getWindowHeight() {
        return getWindow().getWindowHeight();
    }
}
