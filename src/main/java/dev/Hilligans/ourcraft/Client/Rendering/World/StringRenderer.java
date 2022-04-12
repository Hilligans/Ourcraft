package dev.Hilligans.ourcraft.Client.Rendering.World;

import dev.Hilligans.ourcraft.Client.Lang.Languages;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.TextureAtlas;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.Data.Primitives.IntList;
import dev.Hilligans.ourcraft.Data.Primitives.Triplet;
import dev.Hilligans.ourcraft.Ourcraft;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.Future;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class StringRenderer {

    public static StringRenderer instance = new StringRenderer();

    public HashMap<String, Tuple<Integer,Integer>> characterOffset = new HashMap<>();

    public int width;
    public int height;

    public int mappedCharacters = -1;

    public int stringHeight = 58;

    public static void drawString(MatrixStack matrixStack, String string, int x, int y, float scale) {
        instance.drawStringInternal(matrixStack, string, x, y, scale);
    }

    public static void drawString(MatrixStack matrixStack, String[] strings, int x, int y, float scale) {
        instance.drawStringInternal(matrixStack,strings,x,y,scale);
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

    public static void drawCenteredString(MatrixStack matrixStack, String string, int x, int y, float scale) {
        instance.drawCenteredStringInternal(matrixStack, Languages.getTranslated(string), x, y, scale);
    }

    public static void drawCenteredStringTranslated(MatrixStack matrixStack, String string, int y, float scale) {
        drawCenteredString(matrixStack, Languages.getTranslated(string),y,scale);
    }

    public Int2ObjectOpenHashMap<TextureAtlas> textureAtlases = new Int2ObjectOpenHashMap<>();
    public Char2IntOpenHashMap charMap = new Char2IntOpenHashMap();
    public Char2IntOpenHashMap idMap = new Char2IntOpenHashMap();
    public Int2BooleanArrayMap texturesBuilt = new Int2BooleanArrayMap();

    public void buildChars() {
        for(int x = 0; x < Short.MAX_VALUE / 128; x++) {
            int finalX = x;
            Future<TextureAtlas> atlasFuture = Ourcraft.EXECUTOR.submit(() -> {
                TextureAtlas textureAtlas = buildTextureAtlas(finalX);
                textureAtlases.put(finalX,textureAtlas);
                return textureAtlas;
            });
        }
    }

    public TextureAtlas buildTextureAtlas(int startPos) {
        TextureAtlas textureAtlas = new TextureAtlas(1024);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if (Character.isDefined(startPos * 256 + x * 16 + y)) {
                    char character = Character.toChars(startPos * 256 + x * 16 + y)[0];
                    BufferedImage bufferedImage = WorldTextureManager.stringToBufferedImage(character + "");
                    if (bufferedImage != null) {
                        put(character, bufferedImage.getWidth(), textureAtlas.addImage(bufferedImage, 64));
                    }
                }
            }
        }
        return textureAtlas;
    }

    public synchronized void put(char character, int width, int image) {
        charMap.put(character, width);
        idMap.put(character, image);
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
        if(textureAtlases.containsKey(character >> 8)) {
            TextureAtlas textureAtlas = textureAtlases.getOrDefault(character >> 8, null);
            if (textureAtlas == null) {
                return;
            }
            //TODO fix this
            int id;
            try {
                id = idMap.get(character);
            } catch (Exception ignored) {
                return;
            }
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
    }

    public void drawStringInternal(MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val, new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.guiShader)));
            int width = 0;
            for (int z = 0; z < string.length(); z++) {
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int) string.charAt(z) >> 8);
                addVertices(primitiveBuilder, string.charAt(z), x + width, y, scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            draw(matrixStack, vals, primitiveBuilders);
        } catch (Exception ignored) {}
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public void drawStringInternal(MatrixStack matrixStack, String[] strings, int x, int y, float scale) {
        int z = 0;
        for(String string : strings) {
            drawString(matrixStack,string,x, (int) (y + scale * z * stringHeight),scale);
            z++;
        }
    }

    public void drawStringWithBackgroundInternal(MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val, new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.guiShader)));
            int width = 0;
            for (int z = 0; z < string.length(); z++) {
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int) string.charAt(z) >> 8);
                addVertices(primitiveBuilder, string.charAt(z), x + width, y, scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            glUseProgram(ClientMain.getClient().shaderManager.shaderProgram);
            matrixStack.applyColor();
            matrixStack.applyTransformation();
        //    Textures.BACKGROUND.drawTexture(matrixStack, x, y, width, (int) (instance.stringHeight * scale));
            draw(matrixStack, vals, primitiveBuilders);
        } catch (Exception ignored) {}
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public void drawCenteredStringInternal(MatrixStack matrixStack, String string, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        try {
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
            primitiveBuilder.draw1(matrixStack);
        });
        } catch (Exception ignored) {}
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }

    public void drawCenteredStringInternal(MatrixStack matrixStack, String string,int x, int y, float scale) {
        matrixStack.push();
        glDisable(GL_DEPTH_TEST);
        glUseProgram(ShaderManager.guiShader.shader);
        try {
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
            primitiveBuilder.translate(x - finalWidth / 2f,0,0);
            primitiveBuilder.draw1(matrixStack);
        });
        } catch (Exception ignored) {}
        matrixStack.pop();
        glEnable(GL_DEPTH_TEST);
    }


    public IntList getAtlases(String string) {
        IntList intList = new IntList(1);
        if(string != null) {
            for (int x = 0; x < string.length(); x++) {
                char character = string.charAt(x);
                int val = character >> 8;
                if (!intList.containsValues(val)) {
                    intList.add(val);
                }
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
            primitiveBuilder.draw1(matrixStack);
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
