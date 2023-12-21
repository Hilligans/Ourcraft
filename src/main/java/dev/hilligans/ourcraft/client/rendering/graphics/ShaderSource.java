package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsElement;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.ArrayList;

public class ShaderSource implements IRegistryElement, IGraphicsElement {

    public String format;
    public String name;
    public String vertexShader;
    public String fragmentShader;
    public String geometryShader;

    public VertexFormat vertexFormat;

    public ModContent modContent;

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
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
    }

    @Override
    public void load(GameInstance gameInstance) {
        vertexFormat = gameInstance.VERTEX_FORMATS.get(format);
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return modContent.getModID();
    }

    @Override
    public String getResourceType() {
        return "shader";
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        program = (int) graphicsEngine.getDefaultImpl().createProgram(graphicsContext,this);
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
        graphicsEngine.getDefaultImpl().destroyProgram(graphicsContext, program);
    }

    @Override
    public String toString() {
        return "ShaderSource{" +
                "name='" + name + '\'' +
                ", program=" + program +
                '}';
    }
}
