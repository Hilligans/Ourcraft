package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.InvisibleFolder;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.SelectorScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.SelectorWidget;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.ToggleWidget;
import dev.Hilligans.ourcraft.Util.Util;

public class ModListScreen extends ScreenBase implements SelectorScreen {

    InvisibleFolder folder = new InvisibleFolder(20,20,100,1000,"folder");
    SelectorWidget selectorWidget = null;
    ToggleWidget toggleWidget;

    public ModListScreen(Client client) {
        super(client);
        widgets.add(folder);
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
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);
        if(selectorWidget != null) {
            ModContent modContent = client.gameInstance.CONTENT_PACK.mods.get(selectorWidget.name);
            StringRenderer.drawString(matrixStack,new String[]{modContent.modID,"Version: " + modContent.version, "Dependencies: " + Util.toString(modContent.getDependencies()), " ", modContent.description},350,100,0.5f);
        }
    }
}
