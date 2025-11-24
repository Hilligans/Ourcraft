package dev.hilligans.engine.templates;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.List;

public interface ITemplate<R extends IRegistryElement> extends IRegistryElement {

    List<IRegistryElement> parse(GameInstance gameInstance, Schema.Data data, String filename, String owner);

    @Override
    default String getResourceType() {
        return "template";
    }
}
