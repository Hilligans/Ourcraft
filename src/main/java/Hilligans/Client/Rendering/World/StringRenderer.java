package Hilligans.Client.Rendering.World;

import Hilligans.Client.MatrixStack;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Util.Vector5f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL20.*;

public class StringRenderer {

    public static StringRenderer instance = new StringRenderer();

    public HashMap<String, DoubleTypeWrapper<Integer,Integer>> characterOffset = new HashMap<>();

    public int mappedCharacters = -1;

    public void loadCharacters1() {
        String vals = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ";
        int val = 0;
        BufferedImage img = new BufferedImage(vals.length() * 48,58,BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < vals.length(); x++) {
            String s = "" + vals.charAt(x);
            BufferedImage bufferedImage = TextureManager.stringToBufferedImage(s);
            DoubleTypeWrapper<Integer, Integer> data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
            characterOffset.put(s,data);
            for(int z = 0; z < bufferedImage.getWidth(); z++) {
                for(int y = 0; y < bufferedImage.getHeight(); y++) {
                    img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
                }
            }
            val++;
        }
        mappedCharacters = TextureManager.registerTexture(img);
    }

    public static void drawString(MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        int width = 0;
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for(int z = 0; z < string.length(); z++) {
            vector5fs.addAll(Arrays.asList(getVertices("" + string.charAt(z),x + width ,y,scale)));
            width += instance.characterOffset.get("" + string.charAt(z)).getTypeA() * scale;
            indices.addAll(Arrays.asList(getIndices(z * 4)));
        }
        int id = VAOManager.createVAO(VAOManager.convertVertices(vector5fs,false),VAOManager.convertIndices(indices));
        glUseProgram(ClientMain.shaderProgram);
        matrixStack.applyColor();
        matrixStack.applyTransformation();
        draw(id,vector5fs.size());
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawColoredString(MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        int width = 0;
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for(int z = 0; z < string.length(); z++) {
            vector5fs.addAll(Arrays.asList(Vector5f.color(getVertices("" + string.charAt(z),x + width ,y,scale),1f / string.length() * z,1.0f,0.0f,1.0f)));
            width += instance.characterOffset.get("" + string.charAt(z)).getTypeA() * scale;

            indices.addAll(Arrays.asList(getIndices(z * 4)));
        }
        int id = VAOManager.createColorVAO(VAOManager.convertVertices(vector5fs,true),VAOManager.convertIndices(indices));
        glUseProgram(ClientMain.colorShader);
        matrixStack.applyColor(ClientMain.colorShader);
        matrixStack.applyTransformation(ClientMain.colorShader);
        draw(id,vector5fs.size());
        matrixStack.pop();
    }

    private static void draw(int id, int size) {
        glDisable(GL_CULL_FACE);
        GL30.glBindTexture(GL_TEXTURE_2D,instance.mappedCharacters);
        GL30.glBindVertexArray(id);
        glDrawElements(GL_TRIANGLES, size * 3 / 2,GL_UNSIGNED_INT,0);
        glEnable(GL_CULL_FACE);
        VAOManager.destroyBuffer(id);
    }

    private static Vector5f[] getVertices(String character, int x, int y, float scale) {
        int width = instance.characterOffset.get(character).getTypeA();
        int height = (int) (58 * scale);
        int offset = 48 * instance.characterOffset.get(character).getTypeB();
        return new Vector5f[]{new Vector5f(x, y, 0,(float)offset / (instance.characterOffset.size() * 48),0),new Vector5f(x,y + height,0,(float)offset / (instance.characterOffset.size() * 48),1), new Vector5f(x + width * scale,y,0,(float)(width + offset) / (instance.characterOffset.size() * 48),0), new Vector5f(x + width * scale, y + height, 0,(float)(width + offset) / (instance.characterOffset.size()  * 48),1)};
    }

    private static Integer[] getIndices(int offset) {
        return new Integer[] {offset,offset + 1,offset + 2,offset + 3,offset + 1,offset + 2};
    }
}
