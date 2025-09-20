package dev.hilligans.engine2d;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.Screen;

public class Client2D implements IClientApplication {


    @Override
    public Screen getOpenScreen() {
        return null;
    }

    @Override
    public void openScreen(Screen screen) {

    }

    @Override
    public RenderWindow getRenderWindow() {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return null;
    }

    @Override
    public void postCoreStartApplication(GameInstance gameInstance) {

    }

    @Override
    public void startApplication(GameInstance gameInstance) {

    }

    @Override
    public String getResourceName() {
        return "";
    }

    @Override
    public String getResourceOwner() {
        return "";
    }
}
