package dev.hilligans.engine2d.world;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.engine.entity.EntityType;

public class SpriteEntity extends Entity2D implements ISpriteEntity {

    public Sprite sprite;
    public int spriteIndex;

    public SpriteEntity(EntityType entityType, Sprite sprite) {
        super(entityType);
        this.sprite = sprite;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getSpriteIndex() {
        return spriteIndex;
    }

    @Override
    public float getWidth() {
        return 32;
    }

    @Override
    public float getHeight() {
        return 32;
    }

    @Override
    public void tickVisuals(GraphicsContext graphicsContext) {
        long time = graphicsContext.getFrameStartTime();
        spriteIndex = (int) ((time / 400) % 4);
    }
}
