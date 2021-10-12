package dev.Hilligans.ourcraft.Client.Rendering.Widgets;

import dev.Hilligans.ourcraft.Client.Key.CharPress;
import dev.Hilligans.ourcraft.Client.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.Key.KeyPress;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class InputField extends Widget {

    public String string = "";

    public InputField(int x, int y, int width, int height) {
        super(x, y, width, height);
        KeyHandler.register(keyPress,KeyHandler.GLFW_KEY_BACKSPACE);
        KeyHandler.register(charPress);
    }

    public InputField(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        this.name = name;
        KeyHandler.register(keyPress,KeyHandler.GLFW_KEY_BACKSPACE);
        KeyHandler.register(charPress);
    }


    @Override
    public void screenClose() {
        super.screenClose();
        KeyHandler.remove(keyPress);
        KeyHandler.remove(charPress);
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Textures.OUTLINE.drawTexture(matrixStack,x,y,width,height);
        StringRenderer.drawStringTranslated(matrixStack, name, x, y, 0.5f);
        StringRenderer.drawString(matrixStack, string,x,y + height / 2,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    CharPress charPress = new CharPress() {
        @Override
        public void onPress(char key) {
            if(isFocused) {
                if(key == 'v' && KeyHandler.keyPressed[GLFW.GLFW_KEY_LEFT_CONTROL]) {
                    try {
                        //System.out.println("adding clipboard");
                        string += GLFW.glfwGetClipboardString(ClientMain.getClient().window);
                        //string += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                } else {
                    string += key;
                }
            }
        }

        @Override
        public void onRepeat(char key) {
            if(isFocused) {
                string += key;
            }
        }
    };

    KeyPress keyPress = new KeyPress() {
        @Override
        public void onPress() {
            if(isFocused) {
                if (string.length() != 0) {
                    string = string.substring(0, string.length() - 1);
                }
            }
        }

        @Override
        public void onRepeat() {
            if(isFocused) {
                if (string.length() != 0) {
                    string = string.substring(0, string.length() - 1);
                }
            }
        }
    };
}
