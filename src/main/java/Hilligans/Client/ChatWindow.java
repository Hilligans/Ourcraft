package Hilligans.Client;

import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Network.Packet.Client.CSendMessage;
import Hilligans.Client.Key.CharPress;
import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.Rendering.Screen;
import Hilligans.Network.ClientNetworkHandler;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

public class ChatWindow implements Screen {

    private static final int MESSAGE_LENGTH = 5000;

    private static int charHeight = 58;

    public static ArrayList<DoubleTypeWrapper<String,Long>> messages = new ArrayList<>();


    static String string = "";

    static boolean typing;

    public static void addMessage(String message) {
        DoubleTypeWrapper<String,Long> doubleTypeWrapper = new DoubleTypeWrapper<>(message,System.currentTimeMillis() + MESSAGE_LENGTH);
        messages.add(doubleTypeWrapper);
    }

    public static void render1(MatrixStack matrixStack) {
        for(int x = 0; x < messages.size(); x++) {
            if(messages.get(x).getTypeB() < System.currentTimeMillis()) {
                messages.remove(x);
                x--;
            }
        }

        int y = (int) (ClientMain.windowY - charHeight * 1.5);
        int size = messages.size();
        for(int x = 0; x < size; x++) {
            StringRenderer.drawString(matrixStack, messages.get(size - x - 1).getTypeA(),0,y,0.5f);
            y -= charHeight / 2;
            if(y <= 0 ) {
                break;
            }
        }
        if(!string.equals("")) {
            StringRenderer.drawString(matrixStack, string,0,ClientMain.windowY - charHeight / 2,0.5f);
        }
    }

    static {
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(ClientMain.screen == null) {
                    ClientMain.openScreen(new ChatWindow());
                    typing = true;
                }
            }
        },GLFW_KEY_T);
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(typing) {
                    ClientNetworkHandler.sendPacket(new CSendMessage(string));
                    string = "";
                    typing = false;
                    ClientMain.closeScreen();
                }
            }
        },KeyHandler.GLFW_KEY_ENTER);
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(string.length() != 0) {
                    string = string.substring(0,string.length() - 1);
                }
            }

            @Override
            public void onRepeat() {
                if(string.length() != 0) {
                    string = string.substring(0,string.length() - 1);
                }
            }
        }, KeyHandler.GLFW_KEY_BACKSPACE);
        KeyHandler.register(new CharPress() {
            @Override
            public void onPress(char key) {
                if(typing) {
                    string += key;
                }
            }
            @Override
            public void onRepeat(char key) {
                if(typing) {
                    string += key;
                }
            }
        });
    }

    @Override
    public void close(boolean replaced) {
        string = "";
        typing = false;
    }
}
