package dev.hilligans.engine2d.world;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.engine.data.IBoundingBox;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Scene implements IRegistryElement {

    public String name;
    public String scenePath;

    public SceneSection[] sections;

    public VertexFormat vertexFormat;

    public Scene(String name, String scenePath) {
        this.name = name;
        this.scenePath = scenePath;
    }

    public List<SceneSection> getOverlappingSections(IBoundingBox boundingBox) {
        ArrayList<SceneSection> overlapping = new ArrayList<>();

        for(SceneSection section : sections) {
            if(boundingBox.intersects(section.getBoundingBox())) {
                overlapping.add(section);
            }
        }

        return overlapping;
    }

    public void draw(IGraphicsEngine<?, ?, ?> engine, GraphicsContext graphicsContext, MatrixStack matrixStack) {
        for(SceneSection section : sections) {
            IMeshBuilder builder = engine.getDefaultImpl().getMeshBuilder(vertexFormat);

            float x = section.x();
            float y = section.y();

            float width = section.section().getWidth();
            float height = section.section().getHeight();

            builder.addQuad(x, y, 0, 0, x + width, y + height, 1, 1, 0);
            engine.getDefaultImpl().bindTexture(graphicsContext, section.section().info.imageID());
            engine.getDefaultImpl().drawAndDestroyMesh(graphicsContext, matrixStack, builder);
        }
    }

    @Override
    public void load(GameInstance gameInstance) {
        JSONObject jsonObject = gameInstance.getResource(scenePath, JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("sections");

        sections = new SceneSection[jsonArray.length()];
        Registry<MapSection> SECTIONS = gameInstance.getRegistry("engine2D:map_section", MapSection.class);
        for(int x = 0; x < sections.length; x++) {
            JSONObject section = jsonArray.getJSONObject(x);

            sections[x] = new SceneSection(
                    SECTIONS.get(section.getString("name")),
                    section.getInt("x"),
                    section.getInt("y"));
        }

        vertexFormat = gameInstance.get("ourcraft:position_texture", VertexFormat.class);
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return "engine2D";
    }

    @Override
    public String getResourceType() {
        return "scene";
    }

    public record SceneSection(MapSection section, int x, int y) {

        public BoundingBox getBoundingBox() {
            return new BoundingBox(x, y, 0, x + section.getWidth(), y + section.getHeight(), 0);
        }
    }
}
