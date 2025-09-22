package dev.hilligans.engine2d.world;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.client.graphics.resource.ImageInfo;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;

public class MapSection implements IGraphicsElement, IRegistryElement {

    public ModContainer owner;
    public String name;
    public String imagePath;
    public ImageInfo info;

    public int width;
    public int height;

    public MapSection(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
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
        Image image = gameInstance.getResource(imagePath, Image.class);
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
