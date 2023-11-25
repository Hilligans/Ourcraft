package dev.hilligans.ourcraft.world.structure;

import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IStructureBuilder extends IRegistryElement {

    IStructureTemplate parse(String input);

    @Override
    default String getResourceType() {
        return "structure_builder";
    }
}
