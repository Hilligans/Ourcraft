package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
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

    @Override
    public void move(float x, float y, float z) {

    }
}
