package dev.hilligans.ourcraft.Util.Registry;

import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Data.Descriptors.TagCollection;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Resource.ResourceLocation;

public interface IRegistryElement {

    default void load(GameInstance gameInstance) {}

    default void cleanup() {}

    String getResourceName();

    default String getIdentifierName() {
        return "ourcraft:" + getResourceName();
    }

    String getUniqueName();

    default ResourceLocation getResourceLocation(ModContent modContent) {
        return new ResourceLocation(getResourceName(),modContent);
    }

    default void assignModContent(ModContent modContent) {}

    default void setUniqueID(int id) {}

    default TagCollection getTagCollection() {
        return new TagCollection();
    }

    default void loadGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {}

    default void close() {}
}
