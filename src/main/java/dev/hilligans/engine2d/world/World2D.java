package dev.hilligans.engine2d.world;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine2d.client.sprite.ISpriteEntity;
import dev.hilligans.engine.entity.IEntity;

import java.util.ArrayList;
import java.util.List;

public class World2D {

    public ArrayList<IEntity> entities = new ArrayList<>();
    public ArrayList<ISpriteEntity> renderableEntities = new ArrayList<>();

    public GameInstance gameInstance;
    public Scene scene;

    public double lastFrametime;

    public World2D(GameInstance gameInstance, String scene) {
        this.gameInstance = gameInstance;
        this.scene = gameInstance.getExcept(scene, Scene.class);
    }

    public void addEntity(Entity2D entity) {
        entity.setWorld(this);
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

    public Scene getScene() {
        return scene;
    }

    public List<ISpriteEntity> getRenderableEntities() {
        return renderableEntities;
    }

    public void tick() {
        for(IEntity entity : entities) {
            entity.tick();
        }
    }
}
