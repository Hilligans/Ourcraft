package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

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

    int getSensitivity();

    void setSensitivity(int val);

    int getFOV();

    void setFOV(int fov);

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

    default void moveForward(float amount) {
        move((float) (Math.cos(getYaw()) * amount), 0, (float) (Math.sin(getYaw()) * amount));
    }

    default void moveBackward(float amount) {
        move((float) (-Math.cos(getYaw()) * amount), 0, (float) (-Math.sin(getYaw()) * amount));
    }

    default void moveLeft(float amount) {
        move((float) (Math.sin(getYaw()) * amount), 0, (float) (-Math.cos(getYaw()) * amount));
    }

    default void moveRight(float amount) {
        move((float) (-Math.sin(getYaw()) * amount), 0, (float) (Math.cos(getYaw()) * amount));
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
        return getPosition().get(new Vector3d());
    }

    MatrixStack getMatrixStack(int W, int H, int x, int y);

    default MatrixStack getScreenStack(int W, int H, int x, int y) {
        return new MatrixStack(new Matrix4d().translate(W - 1 - 2*x, H - 1 - 2*y, 0).scale(W, H, 1).ortho(0, getWindowWidth(),getWindowHeight(),0,-1,20000));
    }

    default Matrix4d getPerspective(int W, int H, int x, int y, float fov, float aspectRatio, float zNear, float zFar) {
        return new Matrix4d().translate(W - 1 - 2*x, H - 1 - 2*y, 0).scale(W, H, 1).perspective((float) Math.toRadians(fov), aspectRatio,zNear,zFar);
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
