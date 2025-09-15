package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.ArrayList;

public class ShaderSource implements IRegistryElement, IGraphicsElement {

    public String format;
    public String name;
    public String vertexShader;
    public String fragmentShader;
    public String geometryShader;

    public VertexFormat vertexFormat;

    public ModContainer owner;

    public int program;

    public ArrayList<String> uniformNames = new ArrayList<>(4);
    public ArrayList<String> uniformTypes = new ArrayList<>(4);
    public int[] uniformIndexes;

    public ShaderSource(String name, String format, String vertexShader, String fragmentShader) {
        this.name = name;
        this.format = format;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public ShaderSource(String name, String format, String vertexShader, String geometryShader, String fragmentShader) {
        this.name = name;
        this.format = format;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.geometryShader = geometryShader;
    }

    public ShaderSource withUniform(String name, String type) {
        uniformNames.add(name);
        uniformTypes.add(type);
        return this;
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public void preLoad(GameInstance gameInstance) {
        vertexFormat = gameInstance.VERTEX_FORMATS.get(format);
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public String getResourceType() {
        return "shader";
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        if(program == 0) {
            program = (int) graphicsEngine.getDefaultImpl().createProgram(graphicsContext, this);
        }
        /*
        if(uniformNames != null) {
            System.out.println(name);
            uniformIndexes = new int[uniformNames.size()];
            for(int x = 0; x < uniformNames.size(); x++) {
                uniformIndexes[x] = (int) graphicsEngine.getDefaultImpl().getUniformIndex(null, uniformNames.get(x), uniformTypes.get(x), program);
            }
        }

         */
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        if(program != 0) {
            graphicsEngine.getDefaultImpl().destroyProgram(graphicsContext, program);
            program = 0;
        }
    }

    @Override
    public String toString() {
        return "ShaderSource{" +
                "name='" + name + '\'' +
                ", program=" + program +
                '}';
    }
}
