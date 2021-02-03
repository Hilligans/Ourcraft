package Hilligans.Client.Rendering.World;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class ShaderManager {


    public static int registerShader(String vertexShader, String fragmentShader) {

        int shaderProgram;


        int shader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        glShaderSource(shader, vertexShader);
        glCompileShader(shader);

        System.out.println(GL30.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 1 ? "shader compiled successfully" : "failed to compile shader");

        int fragment = GL30.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        glShaderSource(fragment,fragmentShader);
        glCompileShader(fragment);

        System.out.println(GL30.glGetShaderi(fragment, GL20.GL_COMPILE_STATUS) == 1 ? "fragment shader compiled successfully" : "fragment shader failed to compile shader");


        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,shader);
        glAttachShader(shaderProgram,fragment);
        glLinkProgram(shaderProgram);

        glDeleteShader(shader);
        glDeleteShader(fragment);

        return shaderProgram;
    }




}
