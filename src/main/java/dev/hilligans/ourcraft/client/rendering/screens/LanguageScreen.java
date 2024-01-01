package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.lang.Languages;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.widgets.*;

public class LanguageScreen extends ScreenBase implements SelectorScreen {

    SelectorWidget selectorWidget;

    public LanguageScreen() {
        FolderWidget folderWidget = new InvisibleFolder(100,50,0,0,"");
        addWidget(folderWidget);
        for(String string : Languages.languages) {
            folderWidget.addWidget(new SelectorWidget(0,0,200,40,string,this){
                @Override
                public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
                    if(enabled) {
                      //  Textures.BUTTON.drawTexture(matrixStack, x, y, width, height);
                    } else {
                      //  Textures.BUTTON_DARK.drawTexture(matrixStack,x,y,width,height);
                    }
                    window.getStringRenderer().drawStringInternal(window, graphicsContext, matrixStack,Languages.mappedNames.get(name),x,y,0.5f);
                }
            });
        }
        addWidget(new Button(100, 0, 200, 40, "menu.done", () -> {
            if(selectorWidget != null) {
                Languages.setCurrentLanguage(selectorWidget.name);
            }
            getClient().openScreen(new SettingsScreen());
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
