package dev.hilligans.ourcraft.Client.Rendering.Widgets.NewWidgets;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.FrameTracker;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.ShaderSource;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.Client.Rendering.Textures;
import dev.hilligans.ourcraft.Client.Rendering.VertexMesh;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.Widget;
import dev.hilligans.ourcraft.Ourcraft;

public class FrameTimeWidget extends Widget {

    public FrameTracker frameTracker;
    public int matrixIndex;
    public int barPixelWidth;
    public ShaderSource shaderSource;

    public FrameTimeWidget(int x, int y, int width, int height, int barPixelWidth) {
        super(x, y, width, height);
        this.barPixelWidth = barPixelWidth;
    }

    public FrameTimeWidget(float percentX, float percentY, int width, int height) {
        super(percentX, percentY, width, height);
    }

    @Override
    public void addSource(RenderWindow renderWindow) {
        super.addSource(renderWindow);
        this.frameTracker = renderWindow.frameTracker;
        this.shaderSource = renderWindow.getGraphicsEngine().getGameInstance().SHADERS.get("ourcraft:position_color_shader");
    }

    public PrimitiveBuilder buildFrame() {
        VertexFormat vertexFormat = Ourcraft.position_color;
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(vertexFormat);
        int barCount = width / barPixelWidth;
        for(int i = 0; i < barCount; i++) {
            float val = frameTracker.getFrame(i);
            float ratio = 1f/1000*33.33f;
            float r = (Math.min(1,val * ratio));
            float g = (Math.min(1, ratio * (1000 * ratio - val)));
            int x = getX() + barPixelWidth * i;
            primitiveBuilder.addSolidQuad(x,getY() + height - (val * ratio * height),x + barPixelWidth, getY() + height,r,g,0,1);
        }
        return primitiveBuilder;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        VertexMesh mesh = buildFrame().toVertexMesh();

       // Textures.BACKFILL.drawTexture(window,matrixStack,getX(),getY(),width,height);
      //  window.getEngineImpl().setState(window,null,new PipelineState().setDepth(false));

        IDefaultEngineImpl<?, ?> impl = window.getEngineImpl();
        impl.bindPipeline(window, null, shaderSource.program);
        impl.bindTexture(window, null, 0);

        //window.getEngineImpl().drawAndDestroyMesh(window,null,matrixStack,mesh,0,shaderSource.program);
        //window.getEngineImpl().drawAndDestroyMesh(window,null,matrixStack,mesh,0,shaderSource.program);
       // Textures.FRAME_TIME.drawTexture(window,matrixStack,getX(),getY(),width,height);
        Textures.FRAME_TIME.drawTexture(window,matrixStack,getX(),getY(),width,height);
    }

}