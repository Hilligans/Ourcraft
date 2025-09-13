package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface ILayoutEngine<T extends ILayout> extends IRegistryElement, IGraphicsElement {

    T parseLayout(String layout);

    @Override
    default String getResourceType() {
        return "layout_engine";
    }
}
