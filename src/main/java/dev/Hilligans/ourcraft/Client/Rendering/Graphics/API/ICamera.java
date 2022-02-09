package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

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
}
