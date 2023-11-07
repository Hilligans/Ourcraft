package dev.hilligans.ourcraft.client.rendering.world.managers;

import dev.hilligans.ourcraft.client.rendering.newrenderer.Shader;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL41;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class ShaderManager {

    //public int shaderProgram;
    //public int colorShader;

    public static Shader worldShader = new Shader(readShader("/Shaders/WorldVertexShader.glsl"),readShader("/Shaders/WorldFragmentShader.glsl")).addShaderElement(GL_FLOAT,3,false).addShaderElement(GL_FLOAT,4,false).addShaderElement(GL_FLOAT,2,false);
    //  .addShaderElement(GL_FLOAT,1,false);
    //public static Shader guiShader = new Shader(readShader("/Shaders/VertexShader.glsl"),readShader("/Shaders/FragmentShader.glsl")).addShaderElement(GL_FLOAT,3,false).addShaderElement(GL_FLOAT,2,false);

    public ShaderManager() {
        //shaderProgram = ShaderManager.registerShader(Util.shader,Util.fragmentShader1);
        //colorShader = ShaderManager.registerShader(Util.coloredShader,Util.fragmentShader1);
        //transparentColorShader = colorShader;
        //opaqueColorShader = ShaderManager.registerShader(Util.coloredShader,Util.fragmentShader2);
        //lineShader = ShaderManager.registerShader(Util.lineShader, Util.lineFragment);
    }

    public static String readShader(String source) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream stream = ShaderManager.class.getResourceAsStream(source);
        if(stream == null) {
            System.err.println("Cant read file " + source);
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString() + "\n\0";
    }

    public static int registerShader(String vertexShader, String fragmentShader) {
        int vertex =  GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        int fragment = GL30.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        glShaderSource(vertex, vertexShader);
        glShaderSource(fragment,fragmentShader);
        glCompileShader(vertex);
        glCompileShader(fragment);

        if(GL30.glGetShaderi(vertex, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile vertex shader \n" + vertexShader);
        }
        if(GL30.glGetShaderi(fragment, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile fragment shader \n" + fragmentShader);
        }
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertex);
        glAttachShader(shaderProgram,fragment);
        glLinkProgram(shaderProgram);
        glDetachShader(shaderProgram,vertex);
        glDetachShader(shaderProgram,fragment);
        glDeleteShader(vertex);
        glDeleteShader(fragment);

        return shaderProgram;
    }

    public static int registerShader(String vertexShader, String fragmentShader, String tessControlShader, String tessEvalShader) {
        int vertex =  GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        int fragment = GL30.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        int tess_control = GL41.glCreateShader(GL41.GL_TESS_CONTROL_SHADER);
        int tess_eval = GL41.glCreateShader(GL41.GL_TESS_EVALUATION_SHADER);

        glShaderSource(vertex, vertexShader);
        glShaderSource(fragment,fragmentShader);
        glShaderSource(tess_control, tessControlShader);
        glShaderSource(tess_eval, tessEvalShader);

        glCompileShader(vertex);
        glCompileShader(fragment);
        glCompileShader(tess_control);
        glCompileShader(tess_eval);

        if(GL30.glGetShaderi(vertex, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile vertex shader \n" + vertexShader);
        }
        if(GL30.glGetShaderi(fragment, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile fragment shader \n" + fragmentShader);
        }
        if(GL30.glGetShaderi(tess_control, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile tessellation control shader \n" + tessControlShader);
        }
        if(GL30.glGetShaderi(tess_eval, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile tessellation evaluation shader \n" + tessEvalShader);
        }

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertex);
        glAttachShader(shaderProgram,fragment);
        glAttachShader(shaderProgram,tess_control);
        glAttachShader(shaderProgram,tess_eval);
        glLinkProgram(shaderProgram);

        glDetachShader(shaderProgram,vertex);
        glDetachShader(shaderProgram,fragment);
        glDetachShader(shaderProgram,tess_control);
        glDetachShader(shaderProgram,tess_eval);

        glDeleteShader(vertex);
        glDeleteShader(fragment);
        glDeleteShader(tess_control);
        glDeleteShader(tess_eval);

        return shaderProgram;
    }

    public static int registerShader(String vertexShader, String geometryShader, String fragmentShader) {
        int vertex =  GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        int geometry = GL30.glCreateShader(GL_GEOMETRY_SHADER);
        int fragment = GL30.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        glShaderSource(vertex, vertexShader);
        glShaderSource(geometry,geometryShader);
        glShaderSource(fragment,fragmentShader);
        glCompileShader(vertex);
        if(GL30.glGetShaderi(vertex, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile vertex shader \n" + vertexShader);
        }
        glCompileShader(geometry);
        if(GL30.glGetShaderi(geometry, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile geometry shader \n" + geometryShader);
        }
        glCompileShader(fragment);
        if(GL30.glGetShaderi(fragment, GL20.GL_COMPILE_STATUS) == 0) {
            System.out.println("Failed to compile fragment shader \n" + fragmentShader);
        }
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertex);
        glAttachShader(shaderProgram,geometry);
        glAttachShader(shaderProgram,fragment);
        glLinkProgram(shaderProgram);
        glDetachShader(shaderProgram,vertex);
        glDetachShader(shaderProgram,geometry);
        glDetachShader(shaderProgram,fragment);
        glDeleteShader(vertex);
        glDeleteShader(geometry);
        glDeleteShader(fragment);
        return shaderProgram;
    }


}
