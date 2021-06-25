package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.ToggleWidget;
import Hilligans.Util.Settings;

public class AdvancedSettingsScreen extends ScreenBase {

    public AdvancedSettingsScreen() {
        addWidget(new ToggleWidget(100,100,400,50,"Pull Resources From Unloaded Mods", Settings.pullResourcesFromUnloadedMods,value -> Settings.pullResourcesFromUnloadedMods = value).setPercentages(50,50));

    }



}
