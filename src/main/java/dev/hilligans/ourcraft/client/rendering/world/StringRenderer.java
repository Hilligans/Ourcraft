package dev.hilligans.ourcraft.client.rendering.world;

import dev.hilligans.ourcraft.client.lang.Languages;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IDefaultEngineImpl;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.graphics.opengl.OpenglDefaultImpl;
import dev.hilligans.ourcraft.client.rendering.newrenderer.Image;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.TextureAtlas;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.client.rendering.world.managers.WorldTextureManager;
import dev.hilligans.ourcraft.data.primitives.IntList;
import dev.hilligans.ourcraft.Ourcraft;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

public class StringRenderer {

    public int width;
    public int height;

    public int stringHeight = 58;

    public ShaderSource shaderSource;


    public IGraphicsEngine<?,?,?> graphicsEngine;

    public StringRenderer(IGraphicsEngine<?,?,?> graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
        shaderSource = graphicsEngine.getGameInstance().SHADERS.get("ourcraft:position_texture");
    }

    public void drawStringTranslated(RenderWindow window, MatrixStack matrixStack, String string, int x, int y, float scale) {
        drawStringInternal(window, matrixStack, Languages.getTranslated(string),x,y,scale);
    }

    public void drawCenteredStringTranslated(RenderWindow window, MatrixStack matrixStack, String string, int y, float scale) {
        drawCenteredStringInternal(window, matrixStack, Languages.getTranslated(string),y,scale);
    }

    public void drawStringWithBackgroundTranslated(RenderWindow window, MatrixStack matrixStack, String string, int x, int y, float scale) {
        drawStringWithBackgroundInternal(window, matrixStack, Languages.getTranslated(string),x,y,scale);
    }

    public void drawStringInternal(RenderWindow window, MatrixStack matrixStack, String string, int x, int y, float scale) {

        matrixStack.push();

        int width = 0;
        IntList vals = getAtlases(string);
        Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
        vals.forEach((val) -> primitiveBuilders.put(val, new PrimitiveBuilder(shaderSource.vertexFormat)));

        for(int z = 0; z < string.length(); z++) {
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int) string.charAt(z) >> 8);
            addVertices(primitiveBuilder, string.charAt(z), x + width, y, scale);
            width += getCharWidth(string.charAt(z)) * scale;
        }

        draw1(window,matrixStack,vals,primitiveBuilders);
        matrixStack.pop();
    }

    public void drawCenteredStringInternal(RenderWindow window, MatrixStack matrixStack, String string, int y, float scale) {
        matrixStack.push();
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val,new PrimitiveBuilder(shaderSource.vertexFormat)));
            int width = 0;
            for(int z = 0; z < string.length(); z++) {
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
                addVertices(primitiveBuilder,string.charAt(z),width,y,scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            int finalWidth = width;
            ensureTexturesBuilt(vals, window);
            vals.forEach((val) -> {
                TextureAtlas textureAtlas = textureAtlases.get(val);
                if(textureAtlas == null) {
                    return;
                }
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get(val);
                primitiveBuilder.translate(window.getWindowWidth() / 2f - finalWidth / 2f,0,0);
                draw1(window,matrixStack,vals,primitiveBuilders);
            });
        } catch (Exception ignored) {}
        matrixStack.pop();
    }

    public void drawCenteredStringInternal(RenderWindow window, MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val, new PrimitiveBuilder(shaderSource.vertexFormat)));
            int width = 0;
            for(int z = 0; z < string.length(); z++) {
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
                addVertices(primitiveBuilder,string.charAt(z),width,y,scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            int finalWidth = width;
            ensureTexturesBuilt(vals, window);
            IDefaultEngineImpl<?,?> impl = window.getEngineImpl();
            impl.uploadMatrix(null,matrixStack,shaderSource);
            vals.forEach((val) -> {
                TextureAtlas textureAtlas = textureAtlases.get(val);
                if(textureAtlas == null) {
                    return;
                }
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get(val);
                primitiveBuilder.translate(x - finalWidth / 2f,0,0);
                impl.bindPipeline(null,shaderSource.program);
                impl.bindTexture(null,textureAtlas.glTextureId);
                impl.drawAndDestroyMesh(null,matrixStack,primitiveBuilder.toVertexMesh());

            });
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        matrixStack.pop();
    }

    public void drawStringWithBackgroundInternal(RenderWindow window, MatrixStack matrixStack, String string, int x, int y, float scale) {
        matrixStack.push();
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val, new PrimitiveBuilder(shaderSource.vertexFormat)));
            int width = 0;
            for (int z = 0; z < string.length(); z++) {
                PrimitiveBuilder primitiveBuilder = primitiveBuilders.get((int) string.charAt(z) >> 8);
                addVertices(primitiveBuilder, string.charAt(z), x + width, y, scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            Textures.BACKGROUND.drawTexture(window, matrixStack, x, y, width, (int) (stringHeight * scale));
            draw1(window,matrixStack,vals,primitiveBuilders);
        } catch (Exception ignored) {}
        matrixStack.pop();
    }

    public void draw1(RenderWindow window, MatrixStack matrixStack, IntList vals, Int2ObjectOpenHashMap<PrimitiveBuilder> primitiveBuilders) {

        vals.forEach((val) -> {
            TextureAtlas textureAtlas = textureAtlases.get(val);
            if(textureAtlas == null) {
                return;
            }
            PrimitiveBuilder primitiveBuilder = primitiveBuilders.get(val);
            primitiveBuilder.translate(1.0f,0,1.0f);
            IDefaultEngineImpl<?,?> impl = window.getEngineImpl();
            impl.uploadMatrix(null,matrixStack,shaderSource);
            impl.bindPipeline(null,shaderSource.program);
            impl.bindTexture(null,textureAtlas.glTextureId);
            impl.drawAndDestroyMesh(null,matrixStack,primitiveBuilder.toVertexMesh());
        });
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
                    BufferedImage bufferedImage = WorldTextureManager.charToBufferedImage(character);
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

    public void ensureTexturesBuilt(IntList intList, RenderWindow window) {
        intList.forEach((val) -> {
            if(!texturesBuilt.getOrDefault(val,(Boolean)false)) {
                TextureAtlas textureAtlas = textureAtlases.get(val);
                if(textureAtlas != null) {
                    Image image = textureAtlas.toImage();
                    textureAtlas.glTextureId = (int) window.getEngineImpl().createTexture(null,image);
                    texturesBuilt.put(val, (Boolean) true);
                }
            }
        });
    }

    //TODO fix array index exception
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

    public void drawStringInternal(RenderWindow window, MatrixStack matrixStack, String[] strings, int x, int y, float scale) {
        int z = 0;
        for(String string : strings) {
            drawStringInternal(window, matrixStack,string,x, (int) (y + scale * z * stringHeight),scale);
            z++;
        }
    }

    public void close(IDefaultEngineImpl<?,?> impl, GraphicsContext graphicsContext) {
        for(TextureAtlas textureAtlas : textureAtlases.values()) {
            impl.destroyTexture(graphicsContext, textureAtlas.glTextureId);
        }
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

    public int getCharWidth(char character) {
        try {
            if (charMap.containsKey(character)) {
                return charMap.get(character);
            }
        } catch (Exception ignored) {}
        return height;
    }
}
