package dev.hilligans.engine.schema;

import dev.hilligans.engine.util.registry.IRegistryElement;

public interface Schema extends IRegistryElement {


    default String getResourceType() {
        return "schema";
    }
}
