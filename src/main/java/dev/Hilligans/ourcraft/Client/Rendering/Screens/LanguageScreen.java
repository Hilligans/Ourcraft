package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Lang.Languages;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.*;

public class LanguageScreen extends ScreenBase implements SelectorScreen {

    SelectorWidget selectorWidget;

    public LanguageScreen(Client client) {
        super(client);
        FolderWidget folderWidget = new InvisibleFolder(100,50,0,0,"");
        widgets.add(folderWidget);
        for(String string : Languages.languages) {
            folderWidget.addWidget(new SelectorWidget(0,0,200,40,string,this){
                @Override
                public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
                    if(enabled) {
                      //  Textures.BUTTON.drawTexture(matrixStack, x, y, width, height);
                    } else {
                      //  Textures.BUTTON_DARK.drawTexture(matrixStack,x,y,width,height);
                    }
                    StringRenderer.drawString(matrixStack,Languages.mappedNames.get(name),x,y,0.5f);
                }
            });
        }
        widgets.add(new Button(100, 0, 200, 40, "menu.done", () -> {
            if(selectorWidget != null) {
                Languages.setCurrentLanguage(selectorWidget.name);
            }
            client.openScreen(new SettingsScreen(client));
        }));
    }


    @Override
    public void setActive(SelectorWidget selectorWidget) {
        if(this.selectorWidget != null) {
            this.selectorWidget.enabled = true;
        }
        this.selectorWidget = selectorWidget;
    }
}
