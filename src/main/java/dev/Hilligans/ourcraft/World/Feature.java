package dev.Hilligans.ourcraft.World;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

import java.util.ArrayList;
import java.util.Collection;

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
