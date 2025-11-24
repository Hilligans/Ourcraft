package dev.hilligans.engine.templates;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderPipeline;
import dev.hilligans.engine.client.graphics.RenderTarget;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.util.Array;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.ArrayList;
import java.util.List;

public class RenderPipelineTemplate implements Template<RenderPipeline> {

    public static final RenderPipelineTemplate instance = new RenderPipelineTemplate();

    @Override
    public String getResourceName() {
        return "render_pipeline_template";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public List<IRegistryElement> parse(GameInstance gameInstance, Schema.Data data, String filename, String owner) {

        Array<Schema.Data> elements = data.getObjects("tasks");
        String pipelineName = data.getString("name");

        List<IRegistryElement> loadedElements = new ArrayList<>(elements.length() + 1);
        loadedElements.add(new RenderPipeline(pipelineName));

        int x = 0;
        String previousName = null;
        for (Schema.Data line : elements) {
            String task = line.getString("task");
            boolean depth = line.optBoolean("depth", false);

            String name = "target" + x++;
            RenderTarget renderTarget = new RenderTarget(name, owner + ":" + pipelineName, task);

            if(previousName != null) {
                renderTarget.afterTarget(previousName, owner);
            }

            renderTarget.setPipelineState(new PipelineState().setDepth(depth));

            previousName = name;
            loadedElements.add(renderTarget);
        }

        return loadedElements;
    }
}
