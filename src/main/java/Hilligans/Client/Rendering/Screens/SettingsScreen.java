package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Camera;
import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.SliderWidget;
import Hilligans.Client.Rendering.Widgets.ToggleWidget;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Util.Settings;

public class SettingsScreen extends ScreenBase {

    public SettingsScreen(Client client) {
        super(client);
        addWidget(new SliderWidget("settings.sensitivity",50, 50, 200, 40, 50, 300, (int)Camera.sensitivity, value -> Camera.sensitivity = value));
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
