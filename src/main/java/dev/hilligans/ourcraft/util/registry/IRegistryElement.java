package dev.hilligans.ourcraft.util.registry;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.resource.ResourceLocation;

public interface IRegistryElement {

    default void load(GameInstance gameInstance) {}

    default void cleanup() {}

    String getResourceName();

    String getResourceOwner();

    String getResourceType();

    default String getIdentifierName() {
        return getResourceOwner() + ":" + getResourceName();
    }

    default String getUniqueName() {
        return getResourceType() + "." + getResourceOwner() + "." + getIdentifierName();
    }

    default ResourceLocation getResourceLocation(ModContent modContent) {
        return new ResourceLocation(getResourceName(),modContent);
    }

    default void assignModContent(ModContent modContent) {}

    default void setUniqueID(int id) {}

    default TagCollection getTagCollection() {
        return new TagCollection();
    }

    default void cleanupGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {}
}
