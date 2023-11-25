package dev.hilligans.ourcraft.util.usage;

import dev.hilligans.ourcraft.util.registry.IRegistryElement;

public interface IUsageTracker extends IRegistryElement {

    @Override
    default String getResourceType() {
        return "usage_tracker";
    }
}
