package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.input.key.CharPress;
import dev.hilligans.engine.client.input.key.KeyHandler;
import dev.hilligans.engine.client.input.key.KeyPress;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
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
    public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, graphicsContext, matrixStack, xOffset, yOffset);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
     //   Textures.OUTLINE.drawTexture(matrixStack,x,y,width,height);
        window.getStringRenderer().drawStringTranslated(window, graphicsContext, matrixStack, name, x, y, 0.5f);
        window.getStringRenderer().drawStringInternal(window, graphicsContext, matrixStack, string,x,y + height / 2,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    CharPress charPress = new CharPress() {
        @Override
        public void onPress(char key) {
            if(isFocused) {
                if(key == 'v' && KeyHandler.keyPressed[GLFW.GLFW_KEY_LEFT_CONTROL]) {
                    try {
                        //System.out.println("adding clipboard");
                        //TODO fix
                        //string += GLFW.glfwGetClipboardString(ClientMain.getClient().window);
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
