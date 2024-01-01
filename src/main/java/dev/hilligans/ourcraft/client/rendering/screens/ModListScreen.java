package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.widgets.InvisibleFolder;
import dev.hilligans.ourcraft.client.rendering.widgets.SelectorScreen;
import dev.hilligans.ourcraft.client.rendering.widgets.SelectorWidget;
import dev.hilligans.ourcraft.client.rendering.widgets.ToggleWidget;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.Util;

public class ModListScreen extends ScreenBase implements SelectorScreen {

    InvisibleFolder folder = new InvisibleFolder(20,20,100,1000,"folder");
    SelectorWidget selectorWidget = null;
    ToggleWidget toggleWidget;

    public ModListScreen(Client client) {
        addWidget(folder);
        for(String string : client.gameInstance.CONTENT_PACK.mods.keySet()) {
            folder.addWidget(new SelectorWidget(0,0,200,50,string,this));
        }
        folder.update();
    }


    @Override
    public void setActive(SelectorWidget selectorWidget) {
        if(this.selectorWidget != null) {
            this.selectorWidget.enabled = true;
        }
        this.selectorWidget = selectorWidget;
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        super.drawScreen(window, matrixStack, graphicsContext);
        if(selectorWidget != null) {
            ModContent modContent = getClient().gameInstance.CONTENT_PACK.mods.get(selectorWidget.name);
            window.getStringRenderer().drawStringInternal(window, graphicsContext, matrixStack,new String[]{modContent.getModID() + "Version: " + modContent.version, "Dependencies: " + Util.toString(modContent.getDependencies()), " ", modContent.description},350,100,0.5f);
        }
    }
}
