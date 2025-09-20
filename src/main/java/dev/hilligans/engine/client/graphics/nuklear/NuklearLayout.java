package dev.hilligans.engine.client.graphics.nuklear;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayout;
import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class NuklearLayout implements ILayout {

    private NkContext ctx;

    public NuklearLayoutEngine engine;
    private NkBuffer cmds;

    public int width;
    public int height;

    public NuklearLayout(NuklearLayoutEngine engine) {
        this.engine = engine;
        width = 500;
        height = 500;
    }

    protected NuklearLayout setup() {
        ctx = NkContext.create();
        cmds = NkBuffer.calloc();
        nk_init(ctx, ALLOCATOR, null);
        nk_buffer_init(cmds, ALLOCATOR, 1024);
        nk_style_set_font(ctx, engine.default_font);
        return this;
    }

    @Override
    public void drawLayout(RenderWindow renderWindow, GraphicsContext graphicsContext, IGraphicsEngine<?, ?, ?> engine, MatrixStack matrixStack, IClientApplication client) {
        int AA = NK_ANTI_ALIASING_ON;

        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_SCISSOR_TEST);

        IDefaultEngineImpl<?, ?, ?> impl = engine.getDefaultImpl();
        layout(ctx, width, height);

        ByteBuffer vertices = MemoryUtil.memCalloc(10000);
        //vertices.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer elements = MemoryUtil.memCalloc(10000);
        //elements.order(ByteOrder.LITTLE_ENDIAN);
        try (MemoryStack stack = stackPush()) {
            // fill convert configuration
            NkConvertConfig config = NkConvertConfig.calloc(stack)
                    .vertex_layout(VERTEX_LAYOUT)
                    .vertex_size(20)
                    .vertex_alignment(4)
                    .tex_null(this.engine.null_texture)
                    .circle_segment_count(22)
                    .curve_segment_count(22)
                    .arc_segment_count(22)
                    .global_alpha(1.0f)
                    .shape_AA(AA)
                    .line_AA(AA);

            // setup buffers to load vertices and elements
            NkBuffer vbuf = NkBuffer.malloc(stack);
            NkBuffer ebuf = NkBuffer.malloc(stack);

            //nk_buffer_init(vbuf, ALLOCATOR, 1024);
            //nk_buffer_init(ebuf, ALLOCATOR, 1024);
            //vertices = vbuf.memory().ptr();
            //elements = ebuf.memory().ptr();

            nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
            nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
            //nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
            //nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
            nk_convert(ctx, cmds, vbuf, ebuf, config);

            for(int x = 0; x < 100; x++) {
                //System.out.println(Short.toUnsignedInt(elements.getShort(x*2)));
            }
            //System.exit(0);


            FloatBuffer f = stack.floats(
                    2.0f / width, 0.0f, 0.0f, 0.0f,
                    0.0f, -2.0f / height, 0.0f, 0.0f,
                    0.0f, 0.0f, -1.0f, 0.0f,
                    -1.0f, 1.0f, 0.0f, 1.0f
            );
            impl.uploadData(graphicsContext, f, this.engine.nkProgram.uniformIndexes[0], "4fv", this.engine.nkProgram.program, this.engine.nkProgram);

        } catch (Exception e) {
            throw new RuntimeException("Failed to render nk layout", e);
        }
        if (vertices == null) {
            throw new NullPointerException("vertex buffer is null");
        }
        if (elements == null) {
            throw new NullPointerException("index buffer is null");
        }
        //glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
        //glUnmapBuffer(GL_ARRAY_BUFFER);

        //VertexMesh vertexMesh = new VertexMesh(this.engine.vertexFormat).addData(elements, vertices);
        //vertexMesh.elementSize = 2;
        //long mesh = impl.createMesh(graphicsContext, vertexMesh);
        long mesh = 0;

        // iterate over and execute each draw command
        float fb_scale_x = (float) renderWindow.getWindowWidth() / (float) width;
        float fb_scale_y = (float) renderWindow.getWindowHeight() / (float) height;
        long offset = NULL;
        for (NkDrawCommand cmd = nk__draw_begin(ctx, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, ctx)) {
            if (cmd.elem_count() == 0) {
                continue;
            }
            //System.out.println(cmd.elem_count());
            impl.bindTexture(graphicsContext, cmd.texture().id());
            //glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
            NkRect rect = cmd.clip_rect();
            int x = (int) (rect.x() * fb_scale_x);
            int y = (int) ((height - (int) (rect.y() + rect.h())) * fb_scale_y);
            int width = (int) (rect.w() * fb_scale_x);
            int height = (int) (rect.h() * fb_scale_y);
            impl.setScissor(graphicsContext, x, y, width, height);

            impl.drawMesh(graphicsContext, matrixStack, mesh, offset, cmd.elem_count());

            //glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);

            offset += cmd.elem_count() * 2L;
        }
        impl.destroyMesh(graphicsContext, mesh);
        impl.defaultScissor(graphicsContext, renderWindow);
        nk_clear(ctx);
        nk_buffer_clear(cmds);
    }

    void layout(NkContext ctx, int x, int y) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.malloc();

            if (nk_begin(
                    ctx,
                    "Demo",
                    nk_rect(x, y, 230, 250, rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE)) {
                nk_label(ctx, "some rando text", NK_LEFT);


                nk_layout_row_static(ctx, 30, 80, 1);
                if (nk_button_label(ctx, "button")) {
                    System.out.println("button pressed");
                }

                nk_layout_row_dynamic(ctx, 30, 2);
                if (nk_option_label(ctx, "easy", op == EASY)) {
                    op = EASY;
                }
                if (nk_option_label(ctx, "hard", op == HARD)) {
                    op = HARD;
                }

                nk_layout_row_dynamic(ctx, 25, 1);
                nk_property_int(ctx, "Compression:", 0, compression, 100, 10, 1);

                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "background:", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 25, 1);
                if (nk_combo_begin_color(ctx, nk_rgb_cf(background, NkColor.malloc()), NkVec2.malloc().set(nk_widget_width(ctx), 400))) {
                    nk_layout_row_dynamic(ctx, 120, 1);
                    nk_color_picker(ctx, background, NK_RGBA);
                    nk_layout_row_dynamic(ctx, 25, 1);
                    background
                            .r(nk_propertyf(ctx, "#R:", 0, background.r(), 1.0f, 0.01f, 0.005f))
                            .g(nk_propertyf(ctx, "#G:", 0, background.g(), 1.0f, 0.01f, 0.005f))
                            .b(nk_propertyf(ctx, "#B:", 0, background.b(), 1.0f, 0.01f, 0.005f))
                            .a(nk_propertyf(ctx, "#A:", 0, background.a(), 1.0f, 0.01f, 0.005f));
                    nk_combo_end(ctx);
                }
            }

            nk_end(ctx);
        }
    }

    private static final int EASY = 0;
    private static final int HARD = 1;

    NkColorf background = NkColorf.create()
            .r(0.10f)
            .g(0.18f)
            .b(0.24f)
            .a(1.0f);

    private int op = EASY;

    private IntBuffer compression = BufferUtils.createIntBuffer(1).put(0, 20);

    @Override
    public void setField(String fieldName, String data) {

    }

    @Override
    public void setField(String fieldName, int data) {

    }

    public static NkAllocator ALLOCATOR = NkAllocator.create()
            .alloc((handle, old, size) -> nmemAllocChecked(size))
            .mfree((handle, ptr) -> nmemFree(ptr));

    public static NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4)
            .position(0).attribute(NK_VERTEX_POSITION).format(NK_FORMAT_FLOAT).offset(0)
            .position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT).offset(8)
            .position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16)
            .position(3).attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0)
            .flip();
}

