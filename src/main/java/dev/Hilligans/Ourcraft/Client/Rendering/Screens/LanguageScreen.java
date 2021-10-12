package dev.Hilligans.Ourcraft.Client.Rendering.Screens;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.Lang.Languages;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.Ourcraft.Client.Rendering.Textures;
import dev.Hilligans.Ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.*;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.*;

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
                        Textures.BUTTON.drawTexture(matrixStack, x, y, width, height);
                    } else {
                        Textures.BUTTON_DARK.drawTexture(matrixStack,x,y,width,height);
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
