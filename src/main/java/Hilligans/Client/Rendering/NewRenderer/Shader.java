package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Shader {

    public int shader;

    public ArrayList<ShaderElement> shaderElements = new ArrayList<>();
    public int shaderElementCount = 0;

    public Shader(int shader) {
        this.shader = shader;
    }

    public Shader(String vertexShader, String fragmentShader) {
        shader = ShaderManager.registerShader(vertexShader, fragmentShader);
    }

    public Shader(String vertexShader, String geometryShader, String fragmentShader) {
        shader = ShaderManager.registerShader(vertexShader, geometryShader, fragmentShader);
        System.out.println(shader + "b");
    }

    public Shader addShaderElement(int type, int count, boolean normalised) {
        shaderElements.add(new ShaderElement(type,count,normalised));
        shaderElementCount += count;
        return this;
    }

    static class ShaderElement {

        public int type;
        public int count;
        public boolean normalised;

        public ShaderElement(int type, int count, boolean normalised) {
            this.type = type;
            this.count = count;
            this.normalised = normalised;
        }
    }





}
