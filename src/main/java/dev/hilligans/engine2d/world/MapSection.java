package dev.hilligans.engine2d.world;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.client.graphics.resource.ImageInfo;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;
import org.json.JSONArray;
import org.json.JSONObject;

public class MapSection implements IGraphicsElement, IRegistryElement {

    public ModContainer owner;
    public String name;
    public String dataPath;
    public ImageInfo info;

    public int width;
    public int height;

    public BoundingBox[] boundingBoxes;

    public MapSection(String name, String dataPath) {
        this.name = name;
        this.dataPath = dataPath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
    public String getResourceType() {
        return "map_section";
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        JSONObject jsonObject = gameInstance.getResource(dataPath, JSONObject.class);
        JSONArray hitboxes = jsonObject.getJSONArray("hitboxes");

        this.boundingBoxes = new BoundingBox[hitboxes.length()];
        for(int x = 0; x < this.boundingBoxes.length; x++) {
            this.boundingBoxes[x] = new BoundingBox(hitboxes.getJSONObject(x));
        }

        Image image = gameInstance.getResource(jsonObject.getString("image_path"), Image.class);
        this.info = image.upload(graphicsEngine.getDefaultImpl(), graphicsContext);
        this.width = this.info.width();
        this.height = this.info.height();
        image.free();
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        graphicsEngine.getDefaultImpl().destroyTexture(graphicsContext, this.info.imageID());
    }
}
