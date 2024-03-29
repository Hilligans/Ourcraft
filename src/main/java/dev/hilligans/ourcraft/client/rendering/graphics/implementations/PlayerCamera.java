package dev.hilligans.ourcraft.client.rendering.graphics.implementations;

import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerCamera extends WorldCamera {

    public PlayerEntity playerEntity;
    public BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);
    public int cameraMode;
    public boolean shifting = false;

    @Override
    public @NotNull RenderWindow getWindow() {
        return null;
    }

}
