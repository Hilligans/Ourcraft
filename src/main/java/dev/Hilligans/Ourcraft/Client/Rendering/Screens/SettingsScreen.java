package dev.Hilligans.Ourcraft.Client.Rendering.Screens;

import dev.Hilligans.Ourcraft.Client.Camera;
import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.Button;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.SliderWidget;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.ToggleWidget;
import dev.Hilligans.Ourcraft.Util.Settings;

public class SettingsScreen extends ScreenBase {

    public SettingsScreen(Client client) {
        super(client);
        addWidget(new SliderWidget("settings.sensitivity",50, 50, 200, 40, 50, 300, (int) Camera.sensitivity, value -> Camera.sensitivity = value));
        addWidget(new SliderWidget("settings.fov",50, 150, 200, 40, 10, 110, Camera.fov, value -> Camera.fov = value));
        addWidget(new SliderWidget("settings.render_distance", 50, 250, 400, 40, 2, 512, Settings.renderDistance, value -> Settings.renderDistance = value));
        addWidget(new ToggleWidget(50, 350, 200, 40, "settings.transparent_textures", Settings.renderTransparency, value -> {
            Settings.renderTransparency = value;
            if(value) {
                client.shaderManager.colorShader = client.shaderManager.transparentColorShader;
            } else {
                client.shaderManager.colorShader = client.shaderManager.opaqueColorShader;
            }
        }));
        addWidget(new Button(300, 50, 200, 40, "settings.sound_options", () -> client.openScreen(new VolumeScreen(client))));
        addWidget(new Button(300,100,200,40,"settings.languages",() -> client.openScreen(new LanguageScreen(client))));
        addWidget(new Button(300,150,200,40,"Settings.advanced_settings",() -> client.openScreen(new AdvancedSettingsScreen(client))));
    }
}
