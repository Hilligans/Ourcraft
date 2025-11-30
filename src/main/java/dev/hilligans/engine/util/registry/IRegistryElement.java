package dev.hilligans.engine.util.registry;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.ModContent;
import dev.hilligans.engine.resource.ResourceLocation;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface IRegistryElement {

    default void load(GameInstance gameInstance) {}

    default void preLoad(GameInstance gameInstance) {}

    default void unload(GameInstance gameInstance) {}

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

    default int hashcode() {
        return getUniqueName().hashCode();
    }


    ConcurrentHashMap<IRegistryElement, Throwable> objectAllocationTrack = new ConcurrentHashMap<>();
    boolean tracking = true;
    default void track() {
        track(this);
    }

    static void track(IRegistryElement element) {
        if(tracking) {
            Throwable throwable = new Throwable();
            StackTraceElement[] trace = throwable.getStackTrace();
            StackTraceElement[] subSet = new StackTraceElement[trace.length - 2];
            for(int x = 0; x < subSet.length; x++) {
                subSet[x] = trace[x+2];
            }
            throwable.setStackTrace(subSet);

            objectAllocationTrack.put(element, throwable);
        }
    }
}
