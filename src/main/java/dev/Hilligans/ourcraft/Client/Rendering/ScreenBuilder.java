package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Screen;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Primitives.Triplet;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScreenBuilder implements IRegistryElement {

    public ArrayList<TextureElement> textures = new ArrayList<>();
    public String name;
    public ModContent modContent;

    public ScreenBuilder(String name, JSONObject jsonObject) {
        this.name = name;

        JSONArray textures = jsonObject.getJSONArray("textures");
        for(int x = 0; x < textures.length(); x++) {
            JSONObject textureObject = textures.getJSONObject(x);
            Texture texture = new Texture(textureObject.getString("texture"));
            TextureElement textureElement = new TextureElement(texture, getInt(textureObject.getJSONArray("region")),getFloat(textureObject.getJSONArray("position")),getFloat(textureObject.getJSONArray("area")));
            this.textures.add(textureElement);
        }
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
        for(TextureElement textureElement : textures) {
            modContent.registerTexture(textureElement.texture);
        }
    }

    public Screen get(Client client) {
        return new ScreenBase(client) {
            int width;
            int height;

            @Override
            public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
                for(TextureElement textureElement : textures) {
                    int x = (int) (textureElement.position[0] * width);
                    int y = (int) (textureElement.position[1] * height);
                    int widthX = (int) (textureElement.area[0] * width);
                    int heightY = (int) (textureElement.area[1] * height);
                    if((float)widthX / heightY > textureElement.scaleFactor) {
                        widthX = (int) (heightY * textureElement.scaleFactor);
                    } else {
                        heightY = (int) (widthX * (1 / textureElement.scaleFactor));
                    }
                   // textureElement.texture.drawTexture(matrixStack, x,y,widthX,heightY, textureElement.region[0], textureElement.region[1], textureElement.region[2], textureElement.region[3]);
                }
                super.drawScreen(window, matrixStack);
            }

            @Override
            public void resize(int x, int y) {
                super.resize(x, y);
                this.width = x;
                this.height = y;
            }
        };
    }

    @Override
    public void load(GameInstance gameInstance) {
        for(TextureElement textureElement : textures) {
          //  textureElement.texture.
        }
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getUniqueName() {
        return "screen." + modContent.getModID() + "." + name;
    }

    static class TextureElement {
        public Texture texture;
        public int[] region;
        public float[] position;
        public float[] area;

        public float scaleFactor;

        public TextureElement(Texture texture, int[] region, float[] position, float[] area) {
            this.texture = texture;
            this.region = region;
            this.position = position;
            this.area = area;

            for(int x = 0; x < position.length; x++) {
                position[x] /= 100;
            }
            for(int x = 0; x < area.length; x++) {
                area[x] /= 100;
            }

            int pixX = region[2] - region[0];
            int pixY = region[3] - region[1];
            scaleFactor = (area[0] * pixX) / (area[1] * pixY);
        }
    }

    static int[] getInt(JSONArray jsonArray) {
        int[] vals = new int[jsonArray.length()];
        for(int x = 0; x < vals.length; x++) {
            vals[x] = jsonArray.getNumber(x).intValue();
        }
        return vals;
    }

    static float[] getFloat(JSONArray jsonArray) {
        float[] vals = new float[jsonArray.length()];
        for(int x = 0; x < vals.length; x++) {
            vals[x] = jsonArray.getNumber(x).floatValue();
        }
        return vals;
    }
}
