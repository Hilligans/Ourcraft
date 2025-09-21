package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.lang.Languages;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.data.IntList;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.awt.image.BufferedImage;

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

    public void drawStringTranslated(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int x, int y, float scale) {
        drawStringInternal(window, graphicsContext, matrixStack, Languages.getTranslated(string),x,y,scale);
    }

    public void drawCenteredStringTranslated(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int y, float scale) {
        drawCenteredStringInternal(window, graphicsContext, matrixStack, Languages.getTranslated(string),y,scale);
    }

    public void drawStringWithBackgroundTranslated(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int x, int y, float scale, Texture texture) {
        drawStringWithBackgroundInternal(window, graphicsContext, matrixStack, Languages.getTranslated(string),x,y,scale, texture);
    }

    public void drawStringInternal(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int x, int y, float scale) {
        int width = 0;
        IntList vals = getAtlases(string);
        Int2ObjectOpenHashMap<IMeshBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
        vals.forEach((val) -> primitiveBuilders.put(val, window.getEngineImpl().getMeshBuilder(shaderSource.vertexFormat)));

        ensureTexturesBuilt(vals, window);
        for(int z = 0; z < string.length(); z++) {
            IMeshBuilder builder = primitiveBuilders.get((int) string.charAt(z) >> 8);
            addVertices(builder, string.charAt(z), x + width, y, scale);
            width += getCharWidth(string.charAt(z)) * scale;
        }

        draw(window,graphicsContext,matrixStack,vals,primitiveBuilders);
    }

    public void drawCenteredStringInternal(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int y, float scale) {
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<IMeshBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val,window.getEngineImpl().getMeshBuilder(shaderSource.vertexFormat)));
            int width = 0;
            for(int z = 0; z < string.length(); z++) {
                IMeshBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
                addVertices(primitiveBuilder,string.charAt(z),width,y,scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            int finalWidth = width;
            ensureTexturesBuilt(vals, window);
            int[] strideIndex = new int[]{(shaderSource.vertexFormat.getOffset("position"))};
            vals.forEach((val) -> {
                TextureAtlas textureAtlas = textureAtlases.get(val);
                if(textureAtlas == null) {
                    return;
                }
                if(textureAtlas.glTextureId != -1) {
                    IMeshBuilder mesh = primitiveBuilders.get(val);
                    mesh.transform(window.getWindowWidth() / 2f - finalWidth / 2f, strideIndex[0]);
                    draw2(window, graphicsContext, matrixStack, mesh, textureAtlas.glTextureId);
                }
            });
        } catch (Exception ignored) {}
    }

    public void drawCenteredStringInternal(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int x, int y, float scale) {
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<IMeshBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val,window.getEngineImpl().getMeshBuilder(shaderSource.vertexFormat)));
            int width = 0;
            for(int z = 0; z < string.length(); z++) {
                IMeshBuilder primitiveBuilder = primitiveBuilders.get((int)string.charAt(z) >> 8);
                addVertices(primitiveBuilder,string.charAt(z),width,y,scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            int finalWidth = width;
            ensureTexturesBuilt(vals, window);
            int[] strideIndex = new int[]{(shaderSource.vertexFormat.getOffset("position"))};
            vals.forEach((val) -> {
                IMeshBuilder primitiveBuilder = primitiveBuilders.get(val);
                primitiveBuilder.transform(x - finalWidth / 2f, strideIndex[0]);
            });
            draw(window,graphicsContext,matrixStack,vals,primitiveBuilders);
        } catch (Exception ignored) {}
    }

    public void drawStringWithBackgroundInternal(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String string, int x, int y, float scale, Texture texture) {
        try {
            IntList vals = getAtlases(string);
            Int2ObjectOpenHashMap<IMeshBuilder> primitiveBuilders = new Int2ObjectOpenHashMap<>();
            vals.forEach((val) -> primitiveBuilders.put(val, window.getEngineImpl().getMeshBuilder(shaderSource.vertexFormat)));
            int width = 0;
            for (int z = 0; z < string.length(); z++) {
                IMeshBuilder primitiveBuilder = primitiveBuilders.get((int) string.charAt(z) >> 8);
                addVertices(primitiveBuilder, string.charAt(z), x + width, y, scale);
                width += getCharWidth(string.charAt(z)) * scale;
            }
            texture.drawTexture(window, graphicsContext, matrixStack, x, y, width, (int) (stringHeight * scale));
            draw(window,graphicsContext,matrixStack, vals,primitiveBuilders);
        } catch (Exception ignored) {}
    }

    public void draw(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, IntList vals, Int2ObjectOpenHashMap<IMeshBuilder> meshes) {
        vals.forEach((val) -> {
            TextureAtlas textureAtlas = textureAtlases.get(val);
            if(textureAtlas == null) {
                return;
            }
            if(textureAtlas.glTextureId != -1) {
                IMeshBuilder mesh = meshes.get(val);
                draw2(window, graphicsContext, matrixStack, mesh, textureAtlas.glTextureId);
            }
        });
    }

    public void draw2(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, IMeshBuilder mesh, int textureId) {
        IDefaultEngineImpl<?,?,?> impl = window.getEngineImpl();
        impl.uploadMatrix(graphicsContext, matrixStack, shaderSource);
        impl.bindPipeline(graphicsContext, shaderSource.program);
        impl.bindTexture(graphicsContext, textureId);
        impl.drawAndDestroyMesh(graphicsContext, matrixStack, mesh);
    }

    public final Int2ObjectOpenHashMap<TextureAtlas> textureAtlases = new Int2ObjectOpenHashMap<>();
    public Char2IntOpenHashMap charMap = new Char2IntOpenHashMap();
    public Char2IntOpenHashMap idMap = new Char2IntOpenHashMap();
    public Int2BooleanArrayMap texturesBuilt = new Int2BooleanArrayMap();

    public void buildChars() {
        GameInstance gameInstance = graphicsEngine.getGameInstance();
        for(int x = 0; x < Short.MAX_VALUE / 256; x++) {
            int finalX = x;
            gameInstance.THREAD_PROVIDER.execute(() -> {
                TextureAtlas textureAtlas = buildTextureAtlas(gameInstance, finalX);
                //System.out.println("DONZO "+ finalX);
                synchronized (textureAtlases) {
                    textureAtlases.put(finalX, textureAtlas);
                }
            });
        }
    }

    public TextureAtlas buildTextureAtlas(GameInstance gameInstance, int startPos) {
        TextureAtlas textureAtlas = new TextureAtlas(gameInstance, 1024);
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
    public void addVertices(IMeshBuilder primitiveBuilder, char character, int x, int y, float scale) {
        float z = 0;
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
            float minY = textureAtlas.minY(id) + 12f / textureAtlas.height;
            float maxX = textureAtlas.maxX(id);
            float maxY = textureAtlas.maxY(id);

            maxX = (maxX - minX) * (getCharWidth(character) / 64.0f) + minX;
            maxY = (maxY - minY) + minY;

            int width = getCharWidth(character);
            int height = stringHeight;
            primitiveBuilder.addQuad(
                    x, y,  minX, minY,
                    x + width * scale, y + height * scale, maxX, maxY,
                    z);
        }
    }


    public void drawStringInternal(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, String[] strings, int x, int y, float scale) {
        int z = 0;
        for(String string : strings) {
            drawStringInternal(window, graphicsContext, matrixStack,string,x, (int) (y + scale * z * stringHeight),scale);
            z++;
        }
    }

    public void close(IDefaultEngineImpl<?,?,?> impl, GraphicsContext graphicsContext) {
        for(TextureAtlas textureAtlas : textureAtlases.values()) {
            if(textureAtlas.glTextureId != -1) {
                impl.destroyTexture(graphicsContext, textureAtlas.glTextureId);
            }
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
