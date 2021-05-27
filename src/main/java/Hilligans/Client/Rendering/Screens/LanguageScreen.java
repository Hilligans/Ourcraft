package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Lang.Languages;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.Widgets.*;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;

public class LanguageScreen extends ScreenBase implements SelectorScreen {

    SelectorWidget selectorWidget;

    public LanguageScreen() {
        FolderWidget folderWidget = new InvisibleFolder(100,50,0,0,"");
        widgets.add(folderWidget);
        for(String string : Languages.languages) {
            folderWidget.addWidget(new SelectorWidget(0,0,200,40,string,this){
                @Override
                public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
                    if(enabled) {
                        Renderer.drawTexture(matrixStack, Textures.BUTTON, x, y, width, height);
                    } else {
                        Renderer.drawTexture(matrixStack, Textures.BUTTON_DARK,x,y,width,height);
                    }
                    StringRenderer.drawString(matrixStack,Languages.mappedNames.get(name),x,y,0.5f);
                }
            });
        }
        widgets.add(new Button(100, 0, 200, 40, "menu.done", () -> {
            if(selectorWidget != null) {
                Languages.setCurrentLanguage(selectorWidget.name);
            }
            ClientMain.getClient().openScreen(new SettingsScreen());
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
