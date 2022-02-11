package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import org.joml.*;

import java.lang.Math;

public interface ICamera {

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
        addMotion((float) (Math.cos(getYaw()) * amount), 0, (float) -(Math.sin(getYaw()) * amount));
    }

    default void moveBackWard(float amount) {
        addMotion((float) (Math.cos(getYaw()) * amount), 0, (float) (Math.sin(getYaw()) * amount));
    }

    default void moveForeLeft(float amount) {
        addMotion((float) (Math.sin(getYaw()) * amount), 0, (float) (Math.cos(getYaw()) * amount));
    }

    default void moveForeRight(float amount) {
        addMotion((float) (Math.sin(getYaw()) * amount), 0, (float) -(Math.cos(getYaw()) * amount));
    }

    default MatrixStack getMatrix() {
        return getMatrixStack(1,1,0,0);
    }

    default Vector3d getCameraPos() {
        Vector3d vector3d = getPosition();
        double x = vector3d.x - ((int)vector3d.x >> 4);
        double y = vector3d.y - ((int)vector3d.y >> 4);
        double z = vector3d.z - ((int)vector3d.z >> 4);

        return new Vector3d(x,y,z);
    }

    MatrixStack getMatrixStack(int W, int H, int x, int y);

    Matrix4d getPerspective(int W, int H, int x, int y);

    Matrix4d getView();

    Vector3d getLookVector();

    default Vector3d cameraUp() {
        return new Vector3d(0.0f, 1.0f, 0.0f);
    }
}
