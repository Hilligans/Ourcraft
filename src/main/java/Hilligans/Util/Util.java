package Hilligans.Util;

import Hilligans.Client.Rendering.World.TextureManager;

public class Util {

    public static String shader = "#version 330 core\n " +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec2 aTexCoord;\n" +
            "uniform mat4 transform;\n" +
            "out vec2 Tex;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = transform * vec4(aPos, 1.0);\n" +
            "   Tex = aTexCoord;\n" +
            "}\0";

    public static String fragmentShader = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 Tex;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "     FragColor = texture(ourTexture,Tex);\n" +
            "}\n\0";


    public static String shader1 = "#version 330 core\n " +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec2 aTexCoord;\n" +
            "layout (location = 2) in vec4 color;\n" +
            "uniform mat4 transform;\n" +
            "out vec2 Tex;\n" +
            "out vec4 rgba;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = transform * vec4(aPos, 1.0);\n" +
            "   Tex = aTexCoord;\n" +
            "}\0";

    public static String fragmentShader1 = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 Tex;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   vec4 texColor = texture(ourTexture, Tex);\n" +
            "   if(texColor.a < 0.1) {\n" +
            "       discard;}\n" +
            "   FragColor = texColor;\n" +
            "}\n\0";


    public static String imageShader = "#version 330 core\n " +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec2 aTexCoord;\n" +
            "out vec2 Tex;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = vec4(aPos, 1.0);\n" +
            "   Tex = aTexCoord;\n" +
            "}\0";

    public static String imageFragment = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 Tex;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   vec4 texColor = texture(ourTexture, Tex);\n" +
            //"   if(texColor.a < 0.1) {\n" +
            //"       discard;}\n" +
            "   FragColor = texColor;\n" +
            "}\n\0";

    public static void generateCharacters() {
        String vals = "!\"#$%&'()*+,-./0123456789:;<=>?@[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        String vals1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

       // TextureManager.saveImage(TextureManager.stringToBufferedImage(), "\"");

        for(int x = -0; x < vals.length(); x++) {
            try {
                String s = "" + vals.charAt(x);
                TextureManager.saveImage(TextureManager.stringToBufferedImage(s), s);
            } catch (Exception ignored) {}
        }


        for(int x = -0; x < vals1.length(); x++) {
            try {
                String s = "" + vals1.charAt(x);
                TextureManager.saveImage(TextureManager.stringToBufferedImage(s), s + vals1.charAt(x));
            } catch (Exception ignored) {}
        }
    }

}
