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
        widgets.add(new Button(ClientMain.getWindowX() / 2 - 100, (int) (ClientMain.getWindowY() / 2 + Settings.guiSize * 58), 200, 50, "menu.main_menu", new ButtonAction() {
            @Override
            public void onPress() {
                ClientMain.getClient().openScreen(new JoinScreen());
            }
        }));
    }

    @Override
    public void render(MatrixStack matrixStack) {
        StringRenderer.drawCenteredStringTranslated(matrixStack,message == null ? "" : message, (ClientMain.getWindowY() + StringRenderer.instance.stringHeight) / 2,1.0f);
        super.render(matrixStack);
    }
}
