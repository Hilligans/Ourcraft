package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Util.Settings;

public class DisconnectScreen extends ScreenBase {

    String message;

    public DisconnectScreen(Client client, String message) {
        super(client);
        this.message = message;
    }

    @Override
    public void buildContentForWindow(RenderWindow window) {
        int windowX = (int) window.getWindowWidth();
        int windowY = (int) window.getWindowHeight();
        addWidget(new Button(windowX / 2 - 100, (int) (windowY / 2 + Settings.guiSize * 58), 200, 50, "menu.main_menu", () -> ClientMain.getClient().openScreen(new JoinScreen(client))));
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack) {
        window.getStringRenderer().drawCenteredStringTranslated(window, matrixStack,message == null ? "" : message, (int) ((window.getWindowHeight() + window.getStringRenderer().stringHeight) / 2),1.0f);
        super.render(window, matrixStack);
    }
}
