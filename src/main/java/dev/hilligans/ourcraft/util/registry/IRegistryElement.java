package dev.hilligans.ourcraft.util.registry;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.ModContent;
import dev.hilligans.engine.resource.ResourceLocation;

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

    default String getRegistryName() {
        return getResourceOwner() + ":" + getResourceType();
    }

    default ResourceLocation getResourceLocation(ModContent modContent) {
        return new ResourceLocation(getResourceName(),modContent);
    }

    default void assignOwner(ModContent modContent) {}

    default void assignOwner(ModContainer owner) {}

    default void setUniqueID(int id) {}

    default TagCollection getTagCollection() {
        return new TagCollection();
    }

    default void cleanupGraphics(IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext) {}

    default int hashcode() {
        return getUniqueName().hashCode();
    }
}
