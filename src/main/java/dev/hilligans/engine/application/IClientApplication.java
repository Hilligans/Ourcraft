package dev.hilligans.engine.application;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.Screen;

public interface IClientApplication extends IApplication {

    Screen getOpenScreen();

    void openScreen(Screen screen);

    RenderWindow getRenderWindow();

    GameInstance getGameInstance();
}
