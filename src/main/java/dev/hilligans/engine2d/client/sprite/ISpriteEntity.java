package dev.hilligans.engine2d.client.sprite;

import dev.hilligans.ourcraft.entity.IEntity;

public interface ISpriteEntity extends IEntity {

    Sprite getSprite();
    int getSpriteIndex();

    float getWidth();
    float getHeight();

}
