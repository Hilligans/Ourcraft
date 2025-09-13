package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScreenBuilder implements IRegistryElement {

    public ArrayList<TextureElement> textures = new ArrayList<>();
    public String name;
    public ModContainer owner;

    public ScreenBuilder(String name, JSONObject jsonObject) {
        this.name = name;

        JSONArray textures = jsonObject.getJSONArray("textures");
        for(int x = 0; x < textures.length(); x++) {
            JSONObject textureObject = textures.getJSONObject(x);
            Texture texture = new Texture("Images/" + textureObject.getString("texture"));
            TextureElement textureElement = new TextureElement(texture, getInt(textureObject.getJSONArray("region")),getFloat(textureObject.getJSONArray("position")),getFloat(textureObject.getJSONArray("area")));
            this.textures.add(textureElement);
        }
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
        for(TextureElement textureElement : textures) {
            owner.registerTexture(textureElement.texture);
        }
    }

    public Screen get(Client client) {
        return new ScreenBase() {
            int width;
            int height;

            @Override
            public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
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
                super.drawScreen(window, matrixStack, graphicsContext);
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
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public String getResourceType() {
        return "screen";
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
