package dev.hilligans.ourcraft.World;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Util.Registry.IRegistryElement;

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
    public String getIdentifierName() {
        return modContent.getModID() + ':' + featureName;
    }

    @Override
    public String getUniqueName() {
        return "feature." + modContent.getModID() + "." + featureName;
    }

    public ArrayList<Block> getBlockList() {
        return new ArrayList<>();
    }
}
