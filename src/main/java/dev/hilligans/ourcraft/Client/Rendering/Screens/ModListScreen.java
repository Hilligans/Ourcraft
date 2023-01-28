package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.InvisibleFolder;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.SelectorScreen;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.SelectorWidget;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.ToggleWidget;
import dev.hilligans.ourcraft.Util.Util;

public class ModListScreen extends ScreenBase implements SelectorScreen {

    InvisibleFolder folder = new InvisibleFolder(20,20,100,1000,"folder");
    SelectorWidget selectorWidget = null;
    ToggleWidget toggleWidget;

    public ModListScreen(Client client) {
        super(client);
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
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        super.drawScreen(window, matrixStack);
        if(selectorWidget != null) {
            ModContent modContent = client.gameInstance.CONTENT_PACK.mods.get(selectorWidget.name);
            window.getStringRenderer().drawStringInternal(window, matrixStack,new String[]{modContent.getModID() + "Version: " + modContent.version, "Dependencies: " + Util.toString(modContent.getDependencies()), " ", modContent.description},350,100,0.5f);
        }
    }
}
