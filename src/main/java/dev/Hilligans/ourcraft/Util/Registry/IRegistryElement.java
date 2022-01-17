package dev.Hilligans.ourcraft.Util.Registry;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;

public interface IRegistryElement {

    void load(GameInstance gameInstance);

    String getResourceName();

    default ResourceLocation getResourceLocation(ModContent modContent) {
        return new ResourceLocation(getResourceName(),modContent);
    }
}
