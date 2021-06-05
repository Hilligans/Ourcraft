package Hilligans.Client.Rendering.World;

import Hilligans.Client.Lang.Language;
import Hilligans.Client.Lang.Languages;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Client.Rendering.NewRenderer.TextureAtlas;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Data.Primitives.IntList;
import Hilligans.Data.Primitives.TripleTypeWrapper;
import Hilligans.Ourcraft;
import Hilligans.Util.Vector5f;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.lwjgl.opengl.GL30;

import javax.script.ScriptEngineManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class StringRenderer {

    public static StringRenderer instance = new StringRenderer();

    public HashMap<String, DoubleTypeWrapper<Integer,Integer>> characterOffset = new HashMap<>();
    public HashMap<String, TripleTypeWrapper<Integer,Integer, Integer>> characterOffset1 = new HashMap<>();

    public int width;
    public int height;

    public int mappedCharacters = -1;

    public int stringHeight = 58;

    public static void drawString(MatrixStack matrixStack, String string, int x, int y, float scale) {
        instance.drawStringInternal(matrixStack,string,x,y,scale);
    }

    public static void drawStringTranslated(MatrixStack matrixStack, String string, int x, int y, float scale) {
        drawString(matrixStack, Languages.getTranslated(string),x,y,scale);
    }

    public static void drawStringWithBackground(MatrixStack matrixStack, String string, int x, int y, float scale) {
        instance.drawStringWithBackgroundInternal(matrixStack,string,x,y,scale);
    }

    public static void drawStringWithBackgroundTranslated(MatrixStack matrixStack, String string, int x, int y, float scale) {
        drawStringWithBackground(matrixStack, Languages.getTranslated(string),x,y,scale);
    }

    public static void drawCenteredString(MatrixStack matrixStack, String string, int y, float scale) {
        instance.drawCenteredStringInternal(matrixStack, string, y, scale);
    }

    public static void drawCenteredStringTranslated(MatrixStack matrixStack, String string, int y, float scale) {
        drawCenteredString(matrixStack, Languages.getTranslated(string),y,scale);
    }

    public static void drawColoredString(MatrixStack matrixStack, String string, int x, int y, float scale) {
      /*  matrixStack.push();
        int width = 0;
        ArrayList<Vector5f> vector5fs = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        for(int z = 0; z < string.length(); z++) {
            vector5fs.addAll(Arrays.asList(Vector5f.color(getVertices("" + string.charAt(z),x + width ,y,scale),1f / string.length() * z,1.0f,0.0f,1.0f)));
            width += instance.characterOffset.get("" + string.charAt(z)).getTypeA() * scale;

            indices.addAll(Arrays.asList(getIndices(z * 4)));
        }
        int id = VAOManager.createColorVAO(VAOManager.convertVertices(vector5fs,true),VAOManager.convertIndices(indices));
        glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.applyColor(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        //draw(id,vector5fs.size());
        matrixStack.pop();

       */

       // ScriptEngineManager
    }

    public Int2ObjectOpenHashMap<TextureAtlas> textureAtlases = new Int2ObjectOpenHashMap<>();
    public Char2IntOpenHashMap charMap = new Char2IntOpenHashMap();
    public Char2IntOpenHashMap idMap = new Char2IntOpenHashMap();
    public Int2BooleanArrayMap texturesBuilt = new Int2BooleanArrayMap();

    public void buildChars() {
        for(int x = 0; x < Short.MAX_VALUE / 128; x++) {
            int finalX = x;
            Future<TextureAtlas> atlasFuture = Ourcraft.executor.submit(() -> {
                TextureAtlas textureAtlas = buildTextureAtlas(finalX);
                textureAtlases.put(finalX,textureAtlas);
                return textureAtlas;
            });
        }
    }

    public TextureAtlas buildTextureAtlas(int startPos) {
        TextureAtlas textureAtlas = new TextureAtlas(1024);
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                if (Character.isDefined(startPos * 256 + x * 16 + y)) {
                    char character = Character.toChars(startPos * 256 + x * 16 + y)[0];
                    BufferedImage bufferedImage = WorldTextureManager.stringToBufferedImage(character + "");
                    if (bufferedImage != null) {
                        charMap.put(character,bufferedImage.getWidth());
                        idMap.put(character,textureAtlas.addImage(bufferedImage,64));
                    }
                }
            }
        }
        return textureAtlas;
    }

    public void ensureTexturesBuilt(IntList intList) {
        intList.forEach((val) -> {
            if(!texturesBuilt.getOrDefault(val,(Boolean)false)) {
                TextureAtlas textureAtlas = textureAtlases.get(val);
                if(textureAtlas != null) {
                    textureAtlas.buildAtlas();
                    texturesBuilt.put(val, (Boolean) true);
                }
            }
        });
    }

    public void addVertices(PrimitiveBuilder primitiveBuilder, char character, int x, int y, float scale) {
        TextureAtlas textureAtlas = textureAtlases.get(character >> 8);
        if (textureAtlas == null) {
            return;
        }
        int id = idMap.get(character);
        float minX = textureAtlas.minX(id);
        float minY = textureAtlas.minY(id);
        float maxX = textureAtlas.maxX(id);
        float maxY = textureAtlas.maxY(id);

        maxX = (maxX - minX) * (getCharWidth(character) / 64.0f) + minX;
        maxY = (maxY - minY) * (52 / 64.0f) + minY;

        int width = getCharWidth(character);
        int height = stringHeight;
        primitiveBuilder.addQuad(x, y, 0, minX, minY, x, y + height * scale, 0, minX, maxY, x + width * scale, y, 0, maxX, minY, x + width * scale, y + height * scale, 0, maxX, maxY);
    }

    public void drawStringInternal(MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        IntList vals = getAtlases(string);
        Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
        vals.forEach((val) -> primitiveBuilders.put(val,new PrimitiveBuilder(GL_TRIANGLES,ShaderManager.guiShader)));
        int width = 0;
        for(int z = 0; z < string.length(); z++) {
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
            addVertices(primitiveBuilder,string.charAt(z),x + width,y,scale);
            width += getCharWidth(string.charAt(z)) * scale;
        }
        draw(matrixStack,vals,primitiveBuilders);
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public void drawStringWithBackgroundInternal(MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        IntList vals = getAtlases(string);
        Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
        vals.forEach((val) -> primitiveBuilders.put(val,new PrimitiveBuilder(GL_TRIANGLES,ShaderManager.guiShader)));
        int width = 0;
        for(int z = 0; z < string.length(); z++) {
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
            addVertices(primitiveBuilder,string.charAt(z),x + width,y,scale);
            width += getCharWidth(string.charAt(z)) * scale;
        }
        glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
        matrixStack.applyColor();
        matrixStack.applyTransformation();
        Textures.BACKGROUND.drawTexture(matrixStack,x,y,width, (int) (instance.stringHeight * scale));
        draw(matrixStack,vals,primitiveBuilders);
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public void drawCenteredStringInternal(MatrixStack matrixStack, String string, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        IntList vals = getAtlases(string);
        Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
        vals.forEach((val) -> primitiveBuilders.put(val,new PrimitiveBuilder(GL_TRIANGLES,ShaderManager.guiShader)));
        int width = 0;
        for(int z = 0; z < string.length(); z++) {
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
            addVertices(primitiveBuilder,string.charAt(z),width,y,scale);
            width += getCharWidth(string.charAt(z)) * scale;
        }
        int finalWidth = width;
        ensureTexturesBuilt(vals);
        vals.forEach((val) -> {
            TextureAtlas textureAtlas = textureAtlases.get(val);
            if(textureAtlas == null) {
                return;
            }
            textureAtlas.bindTexture();
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get(val);
            primitiveBuilder.translate(ClientMain.getWindowX() / 2f - finalWidth / 2f,0,0);
            primitiveBuilder.draw(matrixStack);
        });
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }


    public IntList getAtlases(String string) {
        IntList intList = new IntList(1);
        for(int x = 0; x < string.length(); x++) {
            char character = string.charAt(x);
            int val = character >> 8;
            if(!intList.containsValues(val)) {
                intList.add(val);
            }
        }
        return intList;
    }

    public void draw(MatrixStack matrixStack, IntList vals, Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders) {
        glEnable(GL_MULTISAMPLE);
        ensureTexturesBuilt(vals);
        vals.forEach((val) -> {
            TextureAtlas textureAtlas = textureAtlases.get(val);
            if(textureAtlas == null) {
                return;
            }
            textureAtlas.bindTexture();
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get(val);
            primitiveBuilder.translate(1.0f,0,1.0f);
            primitiveBuilder.draw(matrixStack);
        });
        glDisable(GL_MULTISAMPLE);
    }

    public int getCharWidth(char character) {
        try {
            if (charMap.containsKey(character)) {
                return charMap.get(character);
            }
        } catch (Exception ignored) {}
        return height;
    }

}
