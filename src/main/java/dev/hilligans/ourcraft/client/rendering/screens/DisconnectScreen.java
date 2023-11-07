package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.util.Settings;

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
