package dev.hilligans.ourcraft.world.structure;

import dev.hilligans.engine.util.registry.IRegistryElement;

public interface IStructureBuilder extends IRegistryElement {

    IStructureTemplate parse(String input);

    @Override
    default String getResourceType() {
        return "structure_builder";
    }
}
