package dev.hilligans.engine2d.world;

import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.engine.data.IBoundingBox;
import dev.hilligans.engine.entity.EntityType;
import dev.hilligans.engine2d.client.Camera2D;
import dev.hilligans.engine2d.client.sprite.Sprite;
import org.joml.Intersectionf;
import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.List;

public class PlayerEntity extends SpriteEntity {

    public Camera2D camera;

    public PlayerEntity(EntityType entityType, Sprite sprite, Camera2D camera) {
        super(entityType, sprite);
        this.camera = camera;
        this.width = 10;
        this.height = 10;
        this.velX = 60f;
        this.velY = 6.9f;
    }

    public double deadlossFrametime = 0;

    @Override
    public void tick() {
        World2D world = getWorld();

        List<Scene.SceneSection> sectionList = world.getScene().getOverlappingSections(getEntityBoundingBox());

        Vector2d vector2d = new Vector2d();

        this.deadlossFrametime += world.lastFrametime;
        if(deadlossFrametime < 0.01) {
            return;
        }

        double x = getX();
        double y = getY();
        double velX = (getVelX() * deadlossFrametime);
        double velY = (getVelY() * deadlossFrametime);

        deadlossFrametime = 0;

        double testX = getWidth() / velX;
        double testY = getHeight() / velY;

        IBoundingBox myBox = getEntityBoundingBox();

        for(Scene.SceneSection section : sectionList) {
            for(BoundingBox boundingBox : section.section().boundingBoxes) {
                double distance = boundingBox.intersectsRay(x, y, 0, velX, velY, 0, vector2d);

                if(distance != -1 && (distance < testX || distance < testY)) {
                    boolean iX = boundingBox.intersectsX(myBox);
                    boolean iY = boundingBox.intersectsY(myBox);

                    if(iX && iY) {
                        continue;
                    }
                }

                this.x += velX;
                this.y += velY;
            }
        }
    }
}
