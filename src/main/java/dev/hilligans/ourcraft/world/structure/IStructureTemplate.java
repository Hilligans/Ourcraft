package dev.hilligans.ourcraft.world.structure;

import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IStructureTemplate extends IRegistryElement {

    String serialize();

    @Override
    default String getResourceType() {
        return "structure_template";
    }
}
