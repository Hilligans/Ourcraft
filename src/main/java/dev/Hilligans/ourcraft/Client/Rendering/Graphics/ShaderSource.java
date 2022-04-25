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
    public void loadGraphics(IGraphicsEngine<?, ?> graphicsEngine) {
        program = graphicsEngine.getDefaultImpl().createProgram(this);
    }
}
