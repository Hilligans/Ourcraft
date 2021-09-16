package Hilligans.Client.Rendering.NewRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.NativeType;

public class GLRenderer {

    public static int drawCalls = 0;
    public static int vertices = 0;
    public static int count = 0;


    public static void glDrawElements(int mode, int count, int type, long indices) {
        vertices += indices / 3;
        drawCalls++;
        GLRenderer.count += count / 6 * 4;
        GL11.glDrawElements(mode,count,type,indices);
    }

    public static void resetFrame() {
        drawCalls = 0;
        vertices = 0;
        count = 0;
    }

}
