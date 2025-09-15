package dev.hilligans.ourcraft.world;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.engine.mod.handler.content.ModContent;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.ArrayList;

public class Feature implements IRegistryElement {

    public String featureName;
    public ModContent modContent;

    public Feature(String featureName) {
        this.featureName = featureName;
    }

    public Feature setModContent(ModContent modContent) {
        this.modContent = modContent;
        return this;
    }

    @Override
    public void load(GameInstance gameInstance) {

    }

    @Override
    public String getResourceName() {
        return featureName;
    }

    @Override
    public String getResourceOwner() {
        return modContent.getModID();
    }

    @Override
    public String getResourceType() {
        return "feature";
    }

    public ArrayList<Block> getBlockList() {
        return new ArrayList<>();
    }
}
