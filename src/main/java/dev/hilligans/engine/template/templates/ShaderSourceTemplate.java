package dev.hilligans.engine.template.templates;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.ShaderSource;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.template.Template;
import dev.hilligans.engine.util.Array;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.ArrayList;
import java.util.List;

public class ShaderSourceTemplate implements Template<ShaderSource> {

    public static final ShaderSourceTemplate instance = new ShaderSourceTemplate();

    public ShaderSourceTemplate() {
        track();
    }

    @Override
    public List<IRegistryElement> parse(GameInstance gameInstance, Schema.Data data, String filename, String owner) {
        Array<Schema.Data> shaders = data.getObjects("shaders");
        ArrayList<IRegistryElement> shaderSources = new ArrayList<>(shaders.length());

        for(Schema.Data shader : shaders) {
            ShaderSource shaderSource = new ShaderSource(shader.getString("name"), shader.getString("format"), shader.getString("vertex_shader"), shader.getString("fragment_shader"));

            Schema.Data uniforms = shader.optObject("uniforms", null);

            if(uniforms != null) {
                for(String s : uniforms.getKeys()) {
                    shaderSource.withUniform(s, uniforms.getString(s));
                }
            }

            shaderSources.add(shaderSource);
        }

        return shaderSources;
    }

    @Override
    public String getResourceName() {
        return "shader_source_template";
    }

    @Override
    public String getResourceOwner() {
        return Engine.ENGINE_NAME;
    }
}
