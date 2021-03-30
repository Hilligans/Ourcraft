package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.SliderChange;
import Hilligans.Client.Rendering.Widgets.SliderWidget;
import Hilligans.Client.Rendering.Widgets.ToggleAction;
import Hilligans.Client.Rendering.Widgets.ToggleWidget;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Entity.Entity;
import Hilligans.Util.Settings;
import Hilligans.Util.Util;

import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL11.glEnable;

public class SettingsScreen extends ScreenBase {

    public SettingsScreen() {
        widgets.add(new SliderWidget(50, 50, 200, 40, 50, 200, (int)Camera.sensitivity, value -> Camera.sensitivity = value));
        widgets.add(new SliderWidget(50, 150, 200, 40, 10, 110, Camera.fov, value -> Camera.fov = value));
        widgets.add(new SliderWidget(50, 250, 200, 40, 2, 64, Settings.renderDistance, value -> Settings.renderDistance = value));
        widgets.add(new ToggleWidget(50, 350, 200, 40, "Transparent textures ", Settings.renderTransparency, value -> {
            Settings.renderTransparency = value;
            if(value) {
                ClientMain.colorShader = ClientMain.transparentColorShader;
            } else {
                ClientMain.colorShader = ClientMain.opaqueColorShader;
            }
        }));
    }

    @Override
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);

        StringRenderer.drawString(matrixStack, "Sensitivity",50,14,0.5f);
        StringRenderer.drawString(matrixStack, "FOV",50,114,0.5f);
        StringRenderer.drawString(matrixStack,"Render Distance",50,214,0.5f);
    }
}
