package dev.hilligans.ourcraft.client;

import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.mod.handler.events.client.ClientSendMessageEvent;
import dev.hilligans.ourcraft.network.packet.client.CSendMessage;
import dev.hilligans.ourcraft.client.input.key.CharPress;
import dev.hilligans.ourcraft.client.input.key.KeyHandler;
import dev.hilligans.ourcraft.client.input.key.KeyPress;
import dev.hilligans.ourcraft.client.rendering.Screen;
import dev.hilligans.ourcraft.Ourcraft;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class ChatWindow implements Screen {

    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static int messageIndex = -1;

    static String string = "";

    static boolean typing;

    public RenderWindow renderWindow;

    public ChatWindow() {
        messageIndex = -1;
    }

    public ChatWindow(String message) {
        messageIndex = -1;
        string = message;
    }

    /*
    static {
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(ClientMain.getClient().screen == null) {
                    ClientMain.getClient().openScreen(new ChatWindow());
                    typing = true;
                }
            }
        },GLFW_KEY_T);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(ClientMain.getClient().screen == null) {
                    ClientMain.getClient().openScreen(new ChatWindow("/"));
                    typing = true;
                }
            }
        },GLFW_KEY_SLASH);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(typing) {
                    if(messageIndex < sentMessages.size() - 1) {
                        messageIndex++;
                    }
                }
            }

            @Override
            public void onRepeat() {
                if(typing) {
                    if(messageIndex < sentMessages.size() - 1) {
                        messageIndex++;
                    }
                }
            }
        },GLFW_KEY_UP);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(typing) {
                    if(messageIndex > -1) {
                        messageIndex--;
                    }
                }
            }

            @Override
            public void onRepeat() {
                if(typing) {
                    if(messageIndex > -1) {
                        messageIndex--;
                    }
                }
            }
        },GLFW_KEY_DOWN);



        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(typing) {
                    string = getString();
                    if(sentMessages.size() == 0 || !sentMessages.get(sentMessages.size() - 1).equals(string)) {
                        sentMessages.add(string);
                    }
                    messageIndex = -1;
                    if(!getString().equals("")) {
                        ClientMain.getClient().sendPacket(new CSendMessage(string));
                        Ourcraft.GAME_INSTANCE.EVENT_BUS.postEvent(new ClientSendMessageEvent(string));
                        string = "";
                        typing = false;
                        ClientMain.getClient().closeScreen();
                    }
                }
            }
        },KeyHandler.GLFW_KEY_ENTER);
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                string = getString();
                messageIndex = -1;
                if(string.length() != 0) {
                    string = string.substring(0,string.length() - 1);
                }
            }

            @Override
            public void onRepeat() {
                string = getString();
                messageIndex = -1;
                if(string.length() != 0) {
                    string = string.substring(0,string.length() - 1);
                }
            }
        }, KeyHandler.GLFW_KEY_BACKSPACE);
        KeyHandler.register(new CharPress() {
            @Override
            public void onPress(char key) {
                string = getString();
                messageIndex = -1;
                if(typing) {
                    string += key;
                }
            }
            @Override
            public void onRepeat(char key) {
                string = getString();
                messageIndex = -1;
                if(typing) {
                    string += key;
                }
            }
        });
    }

     */

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
