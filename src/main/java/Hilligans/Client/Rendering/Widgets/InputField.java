package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.Key.CharPress;
import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import org.lwjgl.opengl.GL11;

public class InputField extends Widget {

    public String string = "";

    public InputField(int x, int y, int width, int height) {
        super(x, y, width, height);
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
    public void render(MatrixStack matrixStack) {
        super.render(matrixStack);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Renderer.drawTexture1(matrixStack, ClientMain.outLine,x,y,width,height);
        StringRenderer.drawString(matrixStack, string,x,y,0.5f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    CharPress charPress = new CharPress() {
        @Override
        public void onPress(char key) {
            if(isFocused) {
                string += key;
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
