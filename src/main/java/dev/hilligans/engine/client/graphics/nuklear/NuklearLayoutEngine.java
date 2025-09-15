package dev.hilligans.engine.client.graphics.nuklear;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.resource.ResourceLocation;
import org.lwjgl.nuklear.*;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.nuklear.Nuklear.NK_UTF_INVALID;
import static org.lwjgl.nuklear.Nuklear.nnk_utf_decode;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class NuklearLayoutEngine implements ILayoutEngine<NuklearLayout> {

    public ArrayList<NuklearLayout> layouts = new ArrayList<>();
    public ShaderSource nkProgram;
    public NkUserFont default_font;
    public NkDrawNullTexture null_texture;
    public VertexFormat vertexFormat;
    NkQueryFontGlyphCallback queryFontCallback;
    NkTextWidthCallback textWidthCallback;

    public GameInstance gameInstance;

    @Override
    public NuklearLayout parseLayout(String layout) {
        return new NuklearLayout(this).setup();
    }

    STBTTFontinfo fontInfo;
    STBTTPackedchar.Buffer cdata;

    public void load(IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext, ByteBuffer ttf) {
        default_font = NkUserFont.malloc();
        null_texture = NkDrawNullTexture.malloc();

        int BITMAP_W = 1024;
        int BITMAP_H = 1024;

        int FONT_HEIGHT = 18;
        int fontTexID;

        fontInfo = STBTTFontinfo.create();
        cdata    = STBTTPackedchar.create(95);

        float scale;
        float descent;

        try (MemoryStack stack = stackPush()) {
            stbtt_InitFont(fontInfo, ttf);
            scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

            IntBuffer d = stack.mallocInt(1);
            stbtt_GetFontVMetrics(fontInfo, null, d, null);
            descent = d.get(0) * scale;

            ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

            STBTTPackContext pc = STBTTPackContext.malloc(stack);
            stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
            stbtt_PackSetOversampling(pc, 4, 4);
            stbtt_PackFontRange(pc, ttf, 0, FONT_HEIGHT, 32, cdata);
            stbtt_PackEnd(pc);

            // Convert R8 to RGBA8
            ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
            for (int i = 0; i < bitmap.capacity(); i++) {
                texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
            }
            texture.flip();

            fontTexID = (int) graphicsEngine.getDefaultImpl().createTexture(graphicsContext, texture, BITMAP_W, BITMAP_H, 4);

           // glBindTexture(GL_TEXTURE_2D, fontTexID);
           // glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            memFree(texture);
            memFree(bitmap);
        }

        queryFontCallback = new NkQueryFontGlyphCallback() {
            @Override
            public void invoke(long handle, float font_height, long glyph, int codepoint, int next_codepoint) {
                try (MemoryStack stack = stackPush()) {
                    FloatBuffer x = stack.floats(0.0f);
                    FloatBuffer y = stack.floats(0.0f);

                    STBTTAlignedQuad q = STBTTAlignedQuad.malloc(stack);
                    IntBuffer advance = stack.mallocInt(1);

                    stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
                    stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

                    NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

                    ufg.width(q.x1() - q.x0());
                    ufg.height(q.y1() - q.y0());
                    ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
                    ufg.xadvance(advance.get(0) * scale);
                    ufg.uv(0).set(q.s0(), q.t0());
                    ufg.uv(1).set(q.s1(), q.t1());
                }
            }
        };

        textWidthCallback = new NkTextWidthCallback() {

            @Override
            public float invoke(long handle, float h, long text, int len) {
                float text_width = 0;
                try (MemoryStack stack = stackPush()) {
                    IntBuffer unicode = stack.mallocInt(1);
                    //IntBuffer unicode = MemoryUtil.memAllocInt(1);

                    int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
                    int text_len = glyph_len;

                    if (glyph_len == 0) {
                        return 0;
                    }

                    IntBuffer advance = stack.mallocInt(1);
                    while (text_len <= len && glyph_len != 0) {
                        if (unicode.get(0) == NK_UTF_INVALID) {
                            break;
                        }

                        /* query currently drawn glyph information */
                        stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
                        text_width += advance.get(0) * scale;

                        /* offset next glyph */
                        glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
                        text_len += glyph_len;
                    }
                }
                return text_width;
            }
        };

        default_font
                .width(textWidthCallback)
                .height(FONT_HEIGHT)
                .query(queryFontCallback)
                .texture(it -> it
                        .id(fontTexID));

        try (MemoryStack stack = stackPush()) {
            int texture = (int) graphicsEngine.getDefaultImpl().createTexture(graphicsContext, stack.bytes((byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF), 1, 1, 4);
            null_texture.texture().id(texture);
            null_texture.uv().set(0.5f, 0.5f);
        }
    }

    @Override
    public String getResourceName() {
        return "nk_layout_engine";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public void load(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        vertexFormat = gameInstance.VERTEX_FORMATS.get("ourcraft:position2_texture_color");
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        if(nkProgram == null) {
            nkProgram = gameInstance.SHADERS.get("ourcraft:nk_shader");
            load(graphicsEngine, graphicsContext, gameInstance.getResourceDirect(new ResourceLocation("Roboto-Medium.ttf", "ourcraft")));
        }
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        graphicsEngine.getDefaultImpl().destroyTexture(graphicsContext, null_texture.texture().id());
        graphicsEngine.getDefaultImpl().destroyTexture(graphicsContext, default_font.texture().id());
        default_font.free();
        null_texture.free();
        queryFontCallback.free();
        textWidthCallback.free();
        nkProgram = null;
    }
}
