package dev.hilligans.engine2d.client.sprite;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IIndirectBuilder;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.client.graphics.resource.ImageInfo;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.entity.IEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnimatedSprite implements Sprite {

    public ModContainer owner;

    public String name;
    public String texturePath;
    public String animationInfoPath;

    JSONObject animationInfo;
    ImageInfo info;

    public VertexFormat vertexFormat;

    public AnimatedSprite(String name, String texturePath, String animationInfoPath) {
        this.name = name;
        this.texturePath = texturePath;
        this.animationInfoPath = animationInfoPath;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public void load(GameInstance gameInstance) {
        animationInfo = gameInstance.getResource(animationInfoPath, JSONObject.class);
        vertexFormat = gameInstance.get("ourcraft:position_texture", VertexFormat.class);
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        Image image = gameInstance.getResource(texturePath, Image.class);
        this.info = image.upload(graphicsEngine.getDefaultImpl(), graphicsContext);
        image.free();
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        this.info.cleanup(graphicsEngine.getDefaultImpl(), graphicsContext);
        this.info = null;
    }

    @Override
    public void build(IMeshBuilder builder) {
        JSONArray array = animationInfo.getJSONArray("frames");

        for(int x = 0; x < array.length(); x++) {
            JSONObject object = array.getJSONObject(x);
            float minX = object.getInt("minX") / (float)info.width();
            float minY = object.getInt("minY") / (float)info.height();
            float maxX = object.getInt("maxX") / (float)info.width();
            float maxY = object.getInt("maxY") / (float)info.height();
        }
    }

    @Override
    public void draw(ISpriteEntity entity, IIndirectBuilder builder) {

    }

    @Override
    public void draw(ISpriteEntity entity, IGraphicsEngine<?, ?, ?> engine, GraphicsContext graphicsContext, MatrixStack matrixStack) {
        IMeshBuilder builder = engine.getDefaultImpl().getMeshBuilder(vertexFormat);

        JSONObject object = animationInfo.getJSONArray("frames").getJSONObject(entity.getSpriteIndex());

        float minTexX = object.getInt("minX") / (float)info.width();
        float minTexY = object.getInt("minY") / (float)info.height();
        float maxTexX = object.getInt("maxX") / (float)info.width();
        float maxTexY = object.getInt("maxY") / (float)info.height();

        float x = (float) entity.getX();
        float y = (float) entity.getY();

        builder.addQuad(x, y, minTexX, minTexY, x + entity.getWidth(), y + entity.getHeight(), maxTexX, maxTexY, 1);
        engine.getDefaultImpl().bindTexture(graphicsContext, info.imageID());
        engine.getDefaultImpl().drawAndDestroyMesh(graphicsContext, matrixStack, builder);
    }
}
