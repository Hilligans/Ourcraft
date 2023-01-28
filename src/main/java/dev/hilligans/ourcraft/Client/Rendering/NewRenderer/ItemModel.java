package dev.hilligans.ourcraft.Client.Rendering.NewRenderer;

import dev.hilligans.ourcraft.Client.Rendering.World.Managers.TextureManager;
import dev.hilligans.ourcraft.Util.Settings;
import org.joml.Vector3f;

public class ItemModel implements IModel {

    int model3D;

    static float size = Settings.guiSize * 8;

    public String source;
    public String path;



    public ItemModel(String path, String source) {
        this.path = path;
        this.source = source;
    }

    @Override
    public float[] getVertices(int side) {
        return new float[0];
    }

    @Override
    public int[] getIndices(int side) {
        return new int[0];
    }

    @Override
    public void addData(PrimitiveBuilder primitiveBuilder, TextureManager textureManager, int side, float size, Vector3f offset, int rotX, int rotY) {
        int id = textureManager.getTextureId();
       /* BufferedImage bufferedImage = TextAtlas.instance.register(new ImageLocation("Items/" + path,"purcraft"));
        float minX = TextAtlas.getMinX(id);
        float minY = TextAtlas.getMinY(id);
        float maxX = TextAtlas.getMaxX(id);
        float maxY = TextAtlas.getMaxY(id);

        float light = 1.2f;

        float width = size / bufferedImage.getWidth();
        float height = size / bufferedImage.getHeight();
        float factorX = (maxX - minX) / bufferedImage.getWidth();
        float factorY = (maxY - minY) / bufferedImage.getHeight();

       // width *= 2;
        for(int x = 0; x < bufferedImage.getWidth(); x++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                if(new Color(bufferedImage.getRGB(x,y), true).getAlpha() == 255) {
                    if (x == 0 || ((bufferedImage.getRGB(x - 1, y) >> 24) & 0xff) != 255) {
                        primitiveBuilder.addMinus(x * width, y * height, 0, light,light,light,light, minX + x * factorX, minY + y * factorY);
                        primitiveBuilder.addMinus(x * width, (y + 1) * height, 0, light,light,light,light, minX + x * factorX, minY + y * factorY);
                        primitiveBuilder.addMinus(x * width, y * height, width, light,light,light,light, minX + x * factorX, minY + y * factorY);
                        primitiveBuilder.addMinus(x * width, (y + 1) * height, width, light,light,light,light, minX + x * factorX, minY + y * factorY);

                        primitiveBuilder.addQuadIndicesInverse();
                    }
                    if (y == 0 || ((bufferedImage.getRGB(x, y - 1) >> 24) & 0xff) != 255) {

                    }
                    if (x == bufferedImage.getWidth() - 1 || ((bufferedImage.getRGB(x + 1, y) >> 24) & 0xff) != 255) {
                        primitiveBuilder.addMinus((x + 1) * width, y * height,0,light,light,light,light,minX + x * factorX, minY + y * factorY);
                        primitiveBuilder.addMinus((x + 1) * width, (y + 1) * height,0,light,light,light,light,minX + x * factorX, minY + y * factorY);
                        primitiveBuilder.addMinus((x + 1) * width, y * height,width,light,light,light,light,minX + x * factorX, minY + y * factorY);
                        primitiveBuilder.addMinus((x + 1) * width, (y + 1) * height,width,light,light,light,light,minX + x * factorX, minY + y * factorY);

                        primitiveBuilder.addQuadIndices();
                    }
                    if (y == bufferedImage.getHeight() - 1 || ((bufferedImage.getRGB(x, y + 1) >> 24) & 0xff) != 255) {

                    }

                }
            }
        }

        primitiveBuilder.addQuadInverse(0,0,0,1,1,1,1,minX,minY,0,size,0,1,1,1,1,minX,maxY,size,0,0,1,1,1,1,maxX,minY,size,size,0,1,1,1,1,maxX,maxY);
        primitiveBuilder.addQuad(0,0,width,1,1,1,1,minX,minY,0,size,width,1,1,1,1,minX,maxY,size,0,width,1,1,1,1,maxX,minY,size,size,width,1,1,1,1,maxX,maxY);


        */

    }

    @Override
    public String getModel() {
        return source;
    }

    @Override
    public String getPath() {
        return path;
    }
}
