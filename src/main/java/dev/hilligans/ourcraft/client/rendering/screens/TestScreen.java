package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayout;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;

public class TestScreen extends ScreenBase {

    ILayoutEngine<?> layoutEngine;
    ILayout layout;

    @Override
    public void setWindow(RenderWindow renderWindow) {
        super.setWindow(renderWindow);
        System.out.println(renderWindow.getGraphicsEngine().getGameInstance().LAYOUT_ENGINES.ELEMENTS);
        layoutEngine = renderWindow.getGraphicsEngine().getGameInstance().LAYOUT_ENGINES.get("ourcraft:nk_layout_engine");
        layout = layoutEngine.parseLayout("");
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        super.drawScreen(window, matrixStack, graphicsContext);
        layout.drawLayout(window, graphicsContext, window.getGraphicsEngine(), matrixStack, window.getClient());
    }
}
