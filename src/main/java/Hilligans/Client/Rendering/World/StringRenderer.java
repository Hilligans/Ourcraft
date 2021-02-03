package Hilligans.Client.Rendering.World;

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

    public void loadCharacters() {
        String vals = "!#$%&'()*+,-./0123456789:;<=>?@[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        String vals1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        BufferedImage img = new BufferedImage(vals.length() * 48 + vals1.length() * 48,58,BufferedImage.TYPE_INT_ARGB);

        int val = 0;

        BufferedImage bufferedImage = TextureManager.loadImage("/characters/quote.png");
        DoubleTypeWrapper<Integer, Integer> data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
        characterOffset.put("\"",data);
        for(int z = 0; z < bufferedImage.getWidth(); z++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
            }
        }

        val++;


        for(int x = 0; x < vals.length(); x++) {
            String s = "" + vals.charAt(x);
            System.out.println(s);
            bufferedImage = TextureManager.loadImage("/characters/" + s + ".png");
            data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
            characterOffset.put(s,data);
            for(int z = 0; z < bufferedImage.getWidth(); z++) {
                for(int y = 0; y < bufferedImage.getHeight(); y++) {
                    img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
                }
            }
            val++;
        }


        for(int x = 0; x < vals1.length(); x++) {
            String s = "" + vals1.charAt(x) + vals1.charAt(x);
            bufferedImage = TextureManager.loadImage("/characters/" + s + ".png");
            data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
            characterOffset.put("" + vals1.charAt(x),data);
            for(int z = 0; z < bufferedImage.getWidth(); z++) {
                for(int y = 0; y < bufferedImage.getHeight(); y++) {
                    img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
                }
            }
            val++;
        }

        mappedCharacters = TextureManager.registerTexture(img);
    }

    public void loadCharacters1() {
        String vals = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ";

        int val = 0;

        BufferedImage img = new BufferedImage(vals.length() * 48,58,BufferedImage.TYPE_INT_ARGB);


        for(int x = 0; x < vals.length(); x++) {
            String s = "" + vals.charAt(x);
            //System.out.println(s);
            BufferedImage bufferedImage = TextureManager.stringToBufferedImage(s);
            //BufferedImage bufferedImage = TextureManager.loadImage("/characters/" + s + ".png");
            DoubleTypeWrapper<Integer, Integer> data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
            characterOffset.put(s,data);
            for(int z = 0; z < bufferedImage.getWidth(); z++) {
                for(int y = 0; y < bufferedImage.getHeight(); y++) {
                    img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
                }
            }
            val++;
        }


        //TextureManager.saveImage(img,"img");

        mappedCharacters = TextureManager.registerTexture(img);
       // System.out.println(mappedCharacters);
    }

    public static void  drawString(String string, int x, int y, float scale) {

        int width = 0;
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for(int z = 0; z < string.length(); z++) {
            vector5fs.addAll(Arrays.asList(getVertices("" + string.charAt(z),x + width ,y,scale)));
            width += instance.characterOffset.get("" + string.charAt(z)).getTypeA() * scale;

            indices.addAll(Arrays.asList(getIndices(z * 4)));
        }

        float[] wholeMesh = new float[vector5fs.size() * 5];
        int[] wholeIndices = new int[indices.size()];
        int a = 0;
        for(Vector5f vector5f : vector5fs) {
            vector5f.addToList(wholeMesh,a * 5);
            a++;
        }
        a = 0;
        for(Integer b : indices) {
            wholeIndices[a] = b;
            //System.out.println(b);
            a++;
        }


        glUseProgram(ClientMain.shaderProgram);


        int id = VAOManager.createVAO(wholeMesh,wholeIndices);


        //int id = VAOManager.createVAO(wholeMesh,wholeIndices);

        Matrix4f matrix4f = new Matrix4f();
        matrix4f.ortho(0,ClientMain.windowX,ClientMain.windowY,0,-1,1);

        //System.out.println(wholeMesh.length + " " + wholeIndices.length);

        GL30.glBindTexture(GL_TEXTURE_2D,instance.mappedCharacters);
        GL30.glBindVertexArray(id);

        int trans = 0;
        trans = glGetUniformLocation(ClientMain.shaderProgram, "transform");
        glDisable(GL_CULL_FACE);
        float[] floats = new float[16];
        glUniformMatrix4fv(trans,false,matrix4f.get(floats));
        glDrawElements(GL_TRIANGLES, vector5fs.size() * 3 / 2,GL_UNSIGNED_INT,0);
        glEnable(GL_CULL_FACE);

        VAOManager.destroyBuffer(id);


    }

    private static Vector5f[] getVertices(String character, int x, int y, float scale) {


        int width = instance.characterOffset.get(character).getTypeA();
        int height = (int) (58 * scale);
        int offset = 48 * instance.characterOffset.get(character).getTypeB();
       // System.out.println(offset);
        //System.out.println((float)offset / (instance.characterOffset.size() * 48) + "   " + (float)(width + offset) / (instance.characterOffset.size()  * 48));
        return new Vector5f[]{new Vector5f(x, y, 0,(float)offset / (instance.characterOffset.size() * 48),0),new Vector5f(x,y + height,0,(float)offset / (instance.characterOffset.size() * 48),1), new Vector5f(x + width * scale,y,0,(float)(width + offset) / (instance.characterOffset.size() * 48),0), new Vector5f(x + width * scale, y + height, 0,(float)(width + offset) / (instance.characterOffset.size()  * 48),1)};
    }

    private static Integer[] getIndices(int offset) {
        return new Integer[] {offset,offset + 1,offset + 2,offset + 3,offset + 1,offset + 2};
    }


}
