package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Util.Settings;

public class DisconnectScreen extends ScreenBase {

    String message;

    public DisconnectScreen(String message) {
        this.message = message;
        widgets.add(new Button(ClientMain.windowX / 2 - 100, (int) (ClientMain.windowY / 2 + Settings.guiSize * 58), 200, 50, "Main Menu", new ButtonAction() {
            @Override
            public void onPress() {
                ClientMain.openScreen(new JoinScreen());
            }
        }));
    }

    @Override
    public void render(MatrixStack matrixStack) {
        StringRenderer.drawCenteredString(matrixStack,message, (ClientMain.windowY + StringRenderer.instance.stringHeight) / 2,1.0f);
        super.render(matrixStack);
    }
}
