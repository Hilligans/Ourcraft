package dev.hilligans.ourcraft.world;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.content.ModContainer;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.ArrayList;

public class Feature implements IRegistryElement {

    public String featureName;
    public ModContainer modContainer;

    public Feature(String featureName) {
        this.featureName = featureName;
    }

    public void setModContainer(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    @Override
    public String getResourceName() {
        return featureName;
    }

    @Override
    public String getResourceOwner() {
        return modContainer.getModID();
    }

    @Override
    public String getResourceType() {
        return "feature";
    }

    public ArrayList<Block> getBlockList() {
        return new ArrayList<>();
    }
}
