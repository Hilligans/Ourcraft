package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Camera;
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

    public SettingsScreen() {
        widgets.add(new SliderWidget("settings.sensitivity",50, 50, 200, 40, 50, 300, (int)Camera.sensitivity, value -> Camera.sensitivity = value));
        widgets.add(new SliderWidget("settings.fov",50, 150, 200, 40, 10, 110, Camera.fov, value -> Camera.fov = value));
        widgets.add(new SliderWidget("settings.render_distance", 50, 250, 200, 40, 2, 64, Settings.renderDistance, value -> Settings.renderDistance = value));
        widgets.add(new ToggleWidget(50, 350, 200, 40, "settings.transparent_textures", Settings.renderTransparency, value -> {
            Settings.renderTransparency = value;
            if(value) {
                ClientMain.getClient().shaderManager.colorShader = ClientMain.getClient().shaderManager.transparentColorShader;
            } else {
                ClientMain.getClient().shaderManager.colorShader = ClientMain.getClient().shaderManager.opaqueColorShader;
            }
        }));
        widgets.add(new Button(300, 50, 200, 40, "settings.sound_options", () -> ClientMain.getClient().openScreen(new VolumeScreen())));
        widgets.add(new Button(300,100,200,40,"settings.languages",() -> ClientMain.getClient().openScreen(new LanguageScreen())));
        //widgets.add(new SliderWidget(300,50,200,40,0,100,100,value -> {}));
    }
}
