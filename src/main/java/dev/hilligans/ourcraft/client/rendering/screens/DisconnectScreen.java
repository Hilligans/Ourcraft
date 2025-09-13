package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.util.Settings;

public class DisconnectScreen extends ScreenBase {

    String message;

    public DisconnectScreen(Client client, String message) {
        this.message = message;
    }

    @Override
    public void buildContentForWindow(RenderWindow window) {
        int windowX = window.getWindowWidth();
        int windowY = window.getWindowHeight();
        addWidget(new Button(windowX / 2 - 100, (int) (windowY / 2 + Settings.guiSize * 58), 200, 50, "menu.main_menu", () -> getClient().openScreen(new JoinScreen())));
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        window.getStringRenderer().drawCenteredStringTranslated(window, graphicsContext, matrixStack,message == null ? "" : message, (int) ((window.getWindowHeight() + window.getStringRenderer().stringHeight) / 2),1.0f);
        super.render(window, matrixStack, graphicsContext);
    }
}
