package dev.hilligans.engine2d.world;

import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine2d.client.sprite.Sprite;
import dev.hilligans.ourcraft.entity.EntityType;

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
        return 100;
    }

    @Override
    public float getHeight() {
        return 100;
    }
}
