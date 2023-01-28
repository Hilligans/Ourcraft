package dev.hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
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
