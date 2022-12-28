package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

public class ShaderSource implements IRegistryElement {

    public String format;
    public String name;
    public String vertexShader;
    public String fragmentShader;
    public String geometryShader;

    public VertexFormat vertexFormat;

    public ModContent modContent;

    public int program;

    public String[] uniformNames;
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

    public ShaderSource uniformNames(String... names) {
        this.uniformNames = names;
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
    public String getIdentifierName() {
        return modContent.getModID() + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "shader." + modContent.getModID() + "." + name;
    }

    @Override
    public void loadGraphics(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        System.err.println("LOADING");
        program = (int) graphicsEngine.getDefaultImpl().createProgram(null,this);
        if(uniformNames != null) {
            System.out.println(name);
            uniformIndexes = new int[uniformNames.length];
            for(int x = 0; x < uniformNames.length; x++) {
                uniformIndexes[x] = (int) graphicsEngine.getDefaultImpl().getUniformIndex(null, uniformNames[x], program);
            }
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
