package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Lang.Languages;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.Widgets.*;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.ModHandler.ModLoader;
import Hilligans.Ourcraft;
import Hilligans.Util.Util;

public class ModListScreen extends ScreenBase implements SelectorScreen {

    InvisibleFolder folder = new InvisibleFolder(20,20,100,1000,"folder");
    SelectorWidget selectorWidget = null;
    ToggleWidget toggleWidget;

    public ModListScreen() {
        widgets.add(folder);
        for(String string : Ourcraft.MOD_LOADER.contentPack.mods.keySet()) {
            folder.addWidget(new SelectorWidget(0,0,200,50,string,this));
        }
        folder.update();
        //widgets.add(new ToggleWidget("Enabled"))
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
            ModContent modContent = Ourcraft.MOD_LOADER.contentPack.mods.get(selectorWidget.name);
            StringRenderer.drawString(matrixStack,new String[]{modContent.modID,"Version: " + modContent.version, "Dependencies: " + Util.toString(modContent.getDependencies()), " ", modContent.description},350,100,0.5f);
        }
    }
}
