package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.SliderWidget;
import dev.hilligans.ourcraft.client.rendering.widgets.ToggleWidget;
import dev.hilligans.ourcraft.util.Settings;

public class AdvancedSettingsScreen extends ScreenBase {

    public AdvancedSettingsScreen(Client client) {
        super(client);
        addWidget(new ToggleWidget(100,100,400,50,"Pull Resources From Unloaded Mods", Settings.pullResourcesFromUnloadedMods, value -> Settings.pullResourcesFromUnloadedMods = value).setPercentages(50,50));
        addWidget(new ToggleWidget(100,100,400,50,"Advanced Mesh Building", Settings.optimizeMesh, value -> Settings.optimizeMesh = value).setPercentages(50,40));
        addWidget(new SliderWidget("Chunk destroy distance", 100, 100, 400, 50, -1, 128, Settings.destroyChunkDistance, value -> Settings.destroyChunkDistance = value));
    }



}
