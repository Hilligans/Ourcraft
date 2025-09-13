package dev.hilligans.ourcraft.client;

import dev.hilligans.ourcraft.client.rendering.Screen;
import dev.hilligans.engine.client.graphics.RenderWindow;

import java.util.ArrayList;

public class ChatWindow implements Screen {

    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static int messageIndex = -1;

    static String string = "";

    static boolean typing;

    public RenderWindow renderWindow;

    public ChatWindow() {
        messageIndex = -1;
    }

    public static String getString() {
        return messageIndex == -1 ? string : sentMessages.size() > messageIndex ? sentMessages.get(sentMessages.size() - messageIndex - 1) : "";
    }

    @Override
    public void setWindow(RenderWindow renderWindow) {
        this.renderWindow = renderWindow;
    }

    @Override
    public RenderWindow getWindow() {
        return this.renderWindow;
    }

    @Override
    public void close(boolean replaced) {
        string = "";
        messageIndex = -1;
        typing = false;
    }
}
