package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Camera;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.SliderWidget;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.ToggleWidget;
import dev.hilligans.ourcraft.Util.Settings;

public class SettingsScreen extends ScreenBase {

    public SettingsScreen(Client client) {
        super(client);
    }

    @Override
    public void buildContentForWindow(RenderWindow window) {
        super.buildContentForWindow(window);

        addWidget(new SliderWidget("settings.sensitivity",50, 50, 200, 40, 50, 300, (int) Camera.sensitivity, value -> Camera.sensitivity = value));
        addWidget(new SliderWidget("settings.fov",50, 150, 200, 40, 10, 110, Camera.fov, value -> Camera.fov = value));
        addWidget(new SliderWidget("settings.render_distance", 50, 250, 400, 40, 2, 512, Settings.renderDistance, value -> Settings.renderDistance = value));
        addWidget(new ToggleWidget(50, 350, 200, 40, "settings.transparent_textures", Settings.renderTransparency, value -> {
            Settings.renderTransparency = value;
            //TODO fix
            if(value) {
                //client.shaderManager.colorShader = client.shaderManager.transparentColorShader;
            } else {
                //client.shaderManager.colorShader = client.shaderManager.opaqueColorShader;
            }
        }));
        addWidget(new Button(300, 50, 200, 40, "settings.sound_options", () -> client.openScreen(new VolumeScreen(client))));
        addWidget(new Button(300,100,200,40,"settings.languages",() -> client.openScreen(new LanguageScreen(client))));
        addWidget(new Button(300,150,200,40,"Settings.advanced_settings",() -> client.openScreen(new AdvancedSettingsScreen(client))));
    }
}
