package Hilligans.Client.Rendering.World;

import org.joml.Vector2i;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class JoinableBufferedImage  {

    ArrayList<ArrayList<Integer>> pixels = new ArrayList<>();

    int width, height;

    int x,y;

    ArrayList<ImageData> imageData = new ArrayList<>();


    public JoinableBufferedImage(BufferedImage bufferedImage) {
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        x = 1;
        y = 1;

        for(int y = 0; y < bufferedImage.getWidth(); y++) {
            pixels.add(new ArrayList<>());
        }

        for(int x = 0; x < bufferedImage.getWidth(); x++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                set(x, y,bufferedImage.getRGB(x,y));
            }
        }
    }

    public void join(BufferedImage bufferedImage) {
        Vector2i pos = getNextSpot(bufferedImage.getWidth(),bufferedImage.getHeight());

        imageData.add(new ImageData(pos.x,pos.y,bufferedImage.getWidth(),bufferedImage.getHeight()));

        for(int x = 0; x < bufferedImage.getWidth(); x++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                set((pos.x - 1) * width + x,(pos.y - 1) * height + y,bufferedImage.getRGB(x,y));
            }
        }
    }

    public BufferedImage generate() {
        BufferedImage bufferedImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                bufferedImage.setRGB(x,y,get(x,y));
            }
        }
        return bufferedImage;
    }

    public int getWidth() {
        return x * width;
    }

    public int getHeight() {
        return y * height;
    }

    public int get(int x, int y) {
        return pixels.get(x).get(y);
    }

    public void set(int x, int y, int val) {
        pixels.get(x).set(y,val);
    }

    public Vector2i getNextSpot(int width, int height) {
        for(int x = 0; x < this.x; x++) {
            for(int y = 0; y < this.y; y++) {
                boolean found = false;
                for(ImageData imageData : imageData) {
                    if(imageData.x == x && imageData.y == y) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    return new Vector2i(x,y);
                }
            }
        }
        if(x > y) {
            y++;
        } else {
            x++;
        }
        return new Vector2i(x,y);
    }

    static class ImageData {

        public int x,y,width,height;

        public ImageData(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }



    }



}
