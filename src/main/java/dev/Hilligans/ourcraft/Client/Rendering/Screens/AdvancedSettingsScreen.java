package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.SliderWidget;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.ToggleWidget;
import dev.Hilligans.ourcraft.Util.Settings;

public class AdvancedSettingsScreen extends ScreenBase {

    public AdvancedSettingsScreen(Client client) {
        super(client);
        addWidget(new ToggleWidget(100,100,400,50,"Pull Resources From Unloaded Mods", Settings.pullResourcesFromUnloadedMods, value -> Settings.pullResourcesFromUnloadedMods = value).setPercentages(50,50));
        addWidget(new ToggleWidget(100,100,400,50,"Advanced Mesh Building", Settings.optimizeMesh, value -> Settings.optimizeMesh = value).setPercentages(50,40));
        addWidget(new SliderWidget("Chunk destroy distance", 100, 100, 400, 50, -1, 128, Settings.destroyChunkDistance, value -> Settings.destroyChunkDistance = value));
    }



}
