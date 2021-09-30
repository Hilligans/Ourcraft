package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Client;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.SliderChange;
import Hilligans.Client.Rendering.Widgets.SliderWidget;
import Hilligans.Client.Rendering.Widgets.ToggleWidget;
import Hilligans.Util.Settings;

import java.util.Set;

public class AdvancedSettingsScreen extends ScreenBase {

    public AdvancedSettingsScreen(Client client) {
        super(client);
        addWidget(new ToggleWidget(100,100,400,50,"Pull Resources From Unloaded Mods", Settings.pullResourcesFromUnloadedMods,value -> Settings.pullResourcesFromUnloadedMods = value).setPercentages(50,50));
        addWidget(new ToggleWidget(100,100,400,50,"Advanced Mesh Building", Settings.optimizeMesh, value -> Settings.optimizeMesh = value).setPercentages(50,40));
        addWidget(new SliderWidget("Chunk destroy distance", 100, 100, 400, 50, -1, 128, Settings.destroyChunkDistance, value -> Settings.destroyChunkDistance = value));
    }



}
