package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.ButtonAction;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Util.Settings;

public class DisconnectScreen extends ScreenBase {

    String message;

    public DisconnectScreen(Client client, String message) {
        super(client);
        this.message = message;
        addWidget(new Button(ClientMain.getWindowX() / 2 - 100, (int) (ClientMain.getWindowY() / 2 + Settings.guiSize * 58), 200, 50, "menu.main_menu", new ButtonAction() {
            @Override
            public void onPress() {
                ClientMain.getClient().openScreen(new JoinScreen(client));
            }
        }));
    }

    @Override
    public void render(MatrixStack matrixStack) {
        StringRenderer.drawCenteredStringTranslated(matrixStack,message == null ? "" : message, (ClientMain.getWindowY() + StringRenderer.instance.stringHeight) / 2,1.0f);
        super.render(matrixStack);
    }
}
