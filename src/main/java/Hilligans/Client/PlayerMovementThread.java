package Hilligans.Client;

import Hilligans.ClientMain;

public class PlayerMovementThread implements Runnable {

    long window;

    public PlayerMovementThread(long window) {
        this.window = window;
    }

    @Override
    public void run() {
        ClientMain.processInput(window);
        Camera.tick();
    }
}
