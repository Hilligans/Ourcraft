package dev.Hilligans.ourcraft.Client;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.ClientSendMessageEvent;
import dev.Hilligans.ourcraft.Network.Packet.Client.CSendMessage;
import dev.Hilligans.ourcraft.Client.Input.Key.CharPress;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.Hilligans.ourcraft.Client.Rendering.Screen;
import dev.Hilligans.ourcraft.Ourcraft;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class ChatWindow implements Screen {

    private static final int MESSAGE_LENGTH = 5000;

    public static ArrayList<Tuple<String,Long>> messages = new ArrayList<>();

    public static final int maxMessageCount = 20;

    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static int messageIndex = -1;

    static String string = "";

    static boolean typing;

    public ChatWindow() {
        messageIndex = -1;
    }

    public ChatWindow(String message) {
        messageIndex = -1;
        string = message;
    }

    public static void addMessage(String message) {
        Tuple<String,Long> tuple = new Tuple<>(message,System.currentTimeMillis() + MESSAGE_LENGTH);
        messages.add(tuple);
        if(messages.size() > maxMessageCount) {
            messages.remove(0);
        }
    }

    public static void render1(RenderWindow window, MatrixStack matrixStack) {
       // for(int x = 0; x < messages.size(); x++) {
        //    if(messages.get(x).getTypeB() < System.currentTimeMillis()) {
               // messages.remove(x);
                //x--;
      //      }
      //
        int biggest = 0;

        StringRenderer stringRenderer = window.getStringRenderer();

        int y = (int) (ClientMain.getWindowY() - stringRenderer.stringHeight * 1.5);
        int size = messages.size();
        for(int x = 0; x < size; x++) {
            if(messages.get(size - x - 1).getTypeB() < System.currentTimeMillis() && !typing) {
                break;
            }
            stringRenderer.drawStringInternal(window, matrixStack, messages.get(size - x - 1).getTypeA(),0,y,0.5f);
           // RenderableString renderableString = new RenderableString(messages.get(size - x - 1).getTypeA(),0,y,0.5f);
            //renderableStrings.add(renderableString);
            //if(biggest < renderableString.length) {
              //  biggest = renderableString.length;
            //}

            y -= stringRenderer.stringHeight / 2;
            if(y <= 0 ) {
                break;
            }
        }

        //Renderer.drawTexture(matrixStack, Textures.TRANSPARENT_BACKGROUND,0,y,biggest, (int) (ClientMain.getWindowY() - StringRenderer.instance.stringHeight * 1.5));


      //  for(RenderableString renderableString : renderableStrings) {
       //     renderableString.draw(matrixStack);
        //    renderableString.destroy();
        //}




        String val = getString();
        if(!val.equals("")) {
            stringRenderer.drawStringInternal(window, matrixStack, val,0,ClientMain.getWindowY() - stringRenderer.stringHeight / 2,0.5f);
        }
    }

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

    public static String getString() {
        return messageIndex == -1 ? string : sentMessages.size() > messageIndex ? sentMessages.get(sentMessages.size() - messageIndex - 1) : "";
    }

    @Override
    public void setWindow(RenderWindow renderWindow) {

    }

    @Override
    public void close(boolean replaced) {
        string = "";
        messageIndex = -1;
        typing = false;
    }
}
