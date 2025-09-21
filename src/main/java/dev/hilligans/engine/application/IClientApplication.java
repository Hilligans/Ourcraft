package dev.hilligans.engine.application;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.Screen;
import dev.hilligans.engine.util.ThreadContext;

public interface IClientApplication extends IApplication {

    Screen getOpenScreen();

    void tick(ThreadContext threadContext);

    void openScreen(Screen screen);

    RenderWindow getRenderWindow();

    GameInstance getGameInstance();
}
