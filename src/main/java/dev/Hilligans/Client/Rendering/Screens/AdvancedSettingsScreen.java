package dev.Hilligans.Client.Rendering.Screens;

import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.Rendering.ScreenBase;
import dev.Hilligans.Client.Rendering.Widgets.SliderWidget;
import dev.Hilligans.Client.Rendering.Widgets.ToggleWidget;
import dev.Hilligans.Util.Settings;

public class AdvancedSettingsScreen extends ScreenBase {

    public AdvancedSettingsScreen(Client client) {
        super(client);
        addWidget(new ToggleWidget(100,100,400,50,"Pull Resources From Unloaded Mods", Settings.pullResourcesFromUnloadedMods, value -> Settings.pullResourcesFromUnloadedMods = value).setPercentages(50,50));
        addWidget(new ToggleWidget(100,100,400,50,"Advanced Mesh Building", Settings.optimizeMesh, value -> Settings.optimizeMesh = value).setPercentages(50,40));
        addWidget(new SliderWidget("Chunk destroy distance", 100, 100, 400, 50, -1, 128, Settings.destroyChunkDistance, value -> Settings.destroyChunkDistance = value));
    }



}
