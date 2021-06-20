package Hilligans.Util;

import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Data.Primitives.DoubleTypeWrapper;

import java.awt.image.BufferedImage;

public class Util {

    public static String shader = "#version 330 core\n " +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec2 aTexCoord;\n" +
            "uniform mat4 transform;\n" +
            "uniform vec4 color;\n" +
            "out vec2 Tex;\n" +
            "out vec4 rgba;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = transform * vec4(aPos, 1.0);\n" +
            "   Tex = aTexCoord;\n" +
            "   rgba = color;\n"  +
            "}\0";

    public static String fragmentShader1 = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 Tex;\n" +
            "in vec4 rgba;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   vec4 texColor = texture(ourTexture, Tex);\n" +
            "   if(texColor.a < 0.1) {\n" +
            "       discard;}\n" +
            "   FragColor = texColor * rgba;\n" +
            "}\n\0";

    public static String fragmentShader2 = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 Tex;\n" +
            "in vec4 rgba;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   vec4 texColor = texture(ourTexture, Tex);\n" +
            "   FragColor = texColor * rgba;\n" +
            "}\n\0";

    public static String coloredShader = "#version 330 core\n " +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 rgb;\n" +
            "layout (location = 2) in vec2 aTexCoord;\n" +
            "uniform mat4 transform;\n" +
            "uniform vec4 color;\n" +
            "out vec2 Tex;\n" +
            "out vec4 rgba;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = transform * vec4(aPos, 1.0);\n" +
            "   Tex = aTexCoord;\n" +
            "   rgba = color * rgb;\n"  +
            "}\0";

    public static String lineShader = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "uniform mat4 transform;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = transform * vec4(aPos, 1.0);\n" +
            "}\0";

    public static String lineFragment = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "void main()" +
            "{\n" +
            "   FragColor = vec4(0.0,0.0,0.0,1.0);\n" +
            "}\0";

    public static String particleVertexShader = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec2 texCoord;\n" +
            //"layout (location = 2) in float size;\n" +
            "uniform mat4 transform;\n" +
            "uniform vec3 pos;\n" +
            "out vec2 Tex;\n" +
            "void main()\n" +
            "{\n" +

            "   gl_Position = transform * vec4(aPos, 1.0);\n" +
            "   gl_PointSize = 128 / gl_Position.w;\n" +
            "   float size = 0.1;\n" +
            "   Tex = texCoord;\n" +
            "}\0";

    public static String particleFragmentShader = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 texCoord;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   FragColor = texture(ourTexture, texCoord);\n" +
            "}\n\0";

    public static String particleGeometryShader1 = "#version 330 core\n" +
            "layout (points) in;\n" +
            "layout (triangle_strip, max_vertices = 4) out;\n" +
            "layout in vec4 tex[1];\n" +
            "layout in mat4 transform[1];\n" +
            "layout in float size[1];\n" +
            "out vec2 Tex;\n" +
            "void main() {    \n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(-size/2, -size/2, 0.0, 0.0)) * transform[0];\n" +
            "    Tex = vec2(tex.x,tex.y);\n" +
            "    EmitVertex();\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(-size/2, size/2, 0.0, 0.0)) * transform[0];\n" +
            "    Tex = vec2(tex.x,tex.w);\n" +
            "    EmitVertex();\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(size/2, -size/2, 0.0, 0.0)) * transform[0];\n" +
            "    Tex = vec2(tex.z,tex.y);\n" +
            "    EmitVertex();\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(size/2, size/2, 0.0, 0.0)) * transform[0];\n" +
            "    Tex = vec2(tex.z,tex.w);\n" +
            "    EmitVertex();\n" +
            "}\0 ";

    public static String geom = "#version 120\n" +
            "layout (points) in;\n" +
            "layout (triangle_strip, max_vertices = 4) out;\n" +
            "in vec4 texOut[1];\n" +
            "in mat4 transformOut[1];\n" +
            "in float sizeOut[1];\n" +
            "out vec2 Tex;\n" +
            "void main() {\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(-sizeOut[0]/2, -sizeOut[0]/2, 0.0, 0.0)) * transformOut[0];\n" +
            "    Tex = vec2(texOut[0].x, texOut[0].y);\n" +
            "    EmitVertex();\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(-sizeOut[0]/2, sizeOut[0]/2, 0.0, 0.0)) * transformOut[0];\n" +
            "    Tex = vec2(texOut[0].x, texOut[0].w);\n" +
            "    EmitVertex();\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(sizeOut[0]/2, -sizeOut[0]/2, 0.0, 0.0)) * transformOut[0];\n" +
            "    Tex = vec2(texOut[0].z, texOut[0].y);\n" +
            "    EmitVertex();\n" +
            "    gl_Position = (gl_in[0].gl_Position + vec4(sizeOut[0]/2, sizeOut[0]/2, 0.0, 0.0)) * transformOut[0];\n" +
            "    Tex = vec2(texOut[0].z, texOut[0].w);\n" +
            "    EmitVertex();\n" +
            "}";

    public static String particleFragmentShader1 = "#version 410 core\n" +
            "out vec4 FragColor;\n" +
            "in vec2 texCoord;\n" +
            "uniform sampler2D ourTexture;\n" +
            "void main()\n" +
            "{\n" +
            "   FragColor = texture(ourTexture, texCoord);\n" +
            "}\n\0";


    public static void generateCharacters() {
        String vals = "!\"#$%&'()*+,-./0123456789:;<=>?@[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        String vals1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

       // TextureManager.saveImage(TextureManager.stringToBufferedImage(), "\"");

        for(int x = -0; x < vals.length(); x++) {
            try {
                String s = "" + vals.charAt(x);
                WorldTextureManager.saveImage(WorldTextureManager.stringToBufferedImage(s), s);
            } catch (Exception ignored) {}
        }


        for(int x = -0; x < vals1.length(); x++) {
            try {
                String s = "" + vals1.charAt(x);
                WorldTextureManager.saveImage(WorldTextureManager.stringToBufferedImage(s), s + vals1.charAt(x));
            } catch (Exception ignored) {}
        }
    }

    public void loadCharacters() {
        String vals = "!#$%&'()*+,-./0123456789:;<=>?@[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        String vals1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        BufferedImage img = new BufferedImage(vals.length() * 48 + vals1.length() * 48,58,BufferedImage.TYPE_INT_ARGB);

        int val = 0;

        BufferedImage bufferedImage = WorldTextureManager.loadImage("characters/quote.png");
        DoubleTypeWrapper<Integer, Integer> data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
       // characterOffset.put("\"",data);
        for(int z = 0; z < bufferedImage.getWidth(); z++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
            }
        }

        val++;


        for(int x = 0; x < vals.length(); x++) {
            String s = "" + vals.charAt(x);
            //System.out.println(s);
            bufferedImage = WorldTextureManager.loadImage("characters/" + s + ".png");
            data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
        //    characterOffset.put(s,data);
            for(int z = 0; z < bufferedImage.getWidth(); z++) {
                for(int y = 0; y < bufferedImage.getHeight(); y++) {
                    img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
                }
            }
            val++;
        }


        for(int x = 0; x < vals1.length(); x++) {
            String s = "" + vals1.charAt(x) + vals1.charAt(x);
            bufferedImage = WorldTextureManager.loadImage("characters/" + s + ".png");
            data = new DoubleTypeWrapper<>(bufferedImage.getWidth(),val);
        //    characterOffset.put("" + vals1.charAt(x),data);
            for(int z = 0; z < bufferedImage.getWidth(); z++) {
                for(int y = 0; y < bufferedImage.getHeight(); y++) {
                    img.setRGB(z + val * 48,y,bufferedImage.getRGB(z,y));
                }
            }
            val++;
        }

      //  mappedCharacters = TextureManager.registerTexture(img);
    }

    public static String toString(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : strings) {
            stringBuilder.append(string).append(" ");
        }
        return stringBuilder.toString();
    }

}
