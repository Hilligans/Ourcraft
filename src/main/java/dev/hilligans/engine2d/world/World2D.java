package dev.hilligans.engine2d.world;

import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine2d.client.sprite.ISpritePlayer;
import dev.hilligans.ourcraft.entity.IEntity;

import java.util.ArrayList;
import java.util.List;

public class World2D {

    public ArrayList<IEntity> entities = new ArrayList<>();
    public ArrayList<ISpriteEntity> renderableEntities = new ArrayList<>();

    public void addEntity(IEntity entity) {
        this.entities.add(entity);
        if(entity instanceof ISpriteEntity spriteEntity) {
            renderableEntities.add(spriteEntity);
        }
    }

    public void removeEntity(IEntity entity) {
        this.entities.remove(entity);
        if(entity instanceof ISpriteEntity spriteEntity) {
            renderableEntities.remove(spriteEntity);
        }
    }

    public List<ISpriteEntity> getRenderableEntities() {
        return renderableEntities;
    }
}
