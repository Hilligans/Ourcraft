package dev.hilligans.engine.client.graphics.tasks;

import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.util.StringRenderer;
import dev.hilligans.engine.util.sections.ProfiledSection;

//import static dev.hilligans.ourcraft.util.FormattedString.FSTR;

public class GUIRenderTask extends RenderTaskSource {

    public GUIRenderTask() {
        super("gui_render_task");
    }

    @Override
    public RenderTask<IClientApplication> getDefaultTask() {
        return new RenderTask<IClientApplication>() {

            public int counter = 0;
            public ProfiledSection.StackFrame monitoredFrame;

            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?,?> engine, IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IDefaultEngineImpl<?,?,?> impl = engine.getDefaultImpl();

                StringRenderer stringRenderer = engine.getStringRenderer();
                counter++;

                if (client.getOpenScreen() != null) {
                    client.getOpenScreen().render(window, screenStack, graphicsContext);
                }
            }

            public void recursiveDraw(StringRenderer stringRenderer, RenderWindow renderWindow, GraphicsContext graphicsContext, MatrixStack matrixStack, ProfiledSection.StackFrame stackFrame, int[] y, long time) {
                //stringRenderer.drawStringInternal(renderWindow, graphicsContext, matrixStack, FSTR."\{stackFrame.getIndentLevel()}\{stackFrame.sectionName}: %2.2f%%\{(double)stackFrame.totalTime/time*100}\n", 0, y[0], 0.5f);
                y[0] += 29;
                for(ProfiledSection.StackFrame child : stackFrame.frames) {
                    recursiveDraw(stringRenderer, renderWindow, graphicsContext, matrixStack, child, y, time);
                }
            }

            public void recursiveDrawTimes(StringRenderer stringRenderer, RenderWindow renderWindow, GraphicsContext graphicsContext, MatrixStack matrixStack, ProfiledSection.StackFrame stackFrame, int[] y) {
                //stringRenderer.drawStringInternal(renderWindow, graphicsContext, matrixStack, STR."\{stackFrame.getIndentLevel()}\{stackFrame.sectionName}: \{Ourcraft.getConvertedTime(stackFrame.totalTime)}\n", 0, y[0], 0.5f);
                y[0] += 29;
                for(ProfiledSection.StackFrame child : stackFrame.frames) {
                    recursiveDrawTimes(stringRenderer, renderWindow, graphicsContext, matrixStack, child, y);
                }
            }

            @Override
            public PipelineState getPipelineState() {
                return new PipelineState();
            }
        };
    }
}
