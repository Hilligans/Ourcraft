package dev.hilligans.ourcraft.item;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.client.rendering.newrenderer.TextAtlas;
import dev.hilligans.ourcraft.data.descriptors.TagCollection;
import dev.hilligans.ourcraft.data.other.ItemProperties;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.recipe.IRecipeComponent;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public class Item implements IRecipeComponent, IRegistryElement {

    public String name;
    public ModContent source;
    public ItemProperties itemProperties;
    public int id;
    public String modID;

    public int vao = -1;
    public int vertexCount = -1;

    public Item(String name, ItemProperties itemProperties) {
        this.name = name;
        this.itemProperties = itemProperties;
       // this.modID = Ourcraft.GAME_INSTANCE.MOD_LOADER.mod;
        id = Items.getNextId();
    }

    public Item(String name, ItemProperties itemProperties, String modID) {
        this(name,itemProperties);
        this.modID = modID;
    }

    public Item setModContent(ModContent modContent) {
        this.source = modContent;
        return this;
    }

    public void generateTextures() {
        if(itemProperties.itemTextureManager != null) {
            itemProperties.itemTextureManager.generate();
        }
    }

    public void render(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        /*
        size *= 2;
        size -= Settings.guiSize * 2;
        x += Settings.guiSize;
        y += Settings.guiSize;
        //glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        glDisable(GL_DEPTH_TEST);
        if(itemProperties.dynamicModel || vao == -1) {
            PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
            addData(primitiveBuilder, 1);
            vertexCount = primitiveBuilder.indices.size();
            vao = VAOManager.createVAO(primitiveBuilder);
        }
        matrixStack.push();
        GL30.glBindVertexArray(vao);
        glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);

        matrixStack.translate(x,y,0);
        matrixStack.scale(size);
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        glDrawElements(GL_TRIANGLES, vertexCount,GL_UNSIGNED_INT,0);
        matrixStack.pop();
        if(itemProperties.dynamicModel) {
            VAOManager.destroyBuffer(vao);
        }
        glEnable(GL_DEPTH_TEST);
        //TODO fix
        //drawString(matrixStack,x - size / 2,y,size/2,itemStack.count);

         */
    }

    public void renderHolding(RenderWindow window, MatrixStack matrixStack, int size, ItemStack itemStack) {
       /*
        int x = (int) (window.getWindowWidth() / 16 * 12);
        int y = (int) (window.getWindowHeight() / 16 * 12);

        size *= 2;
        size -= Settings.guiSize * 2;
        x += Settings.guiSize;
        y += Settings.guiSize;
        //glUseProgram(ClientMain.getClient().shaderManager.colorShader);
        glDisable(GL_DEPTH_TEST);
        if(itemProperties.dynamicModel || vao == -1) {
            PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
            addData(primitiveBuilder, 1);
            vertexCount = primitiveBuilder.indices.size();
            vao = VAOManager.createVAO(primitiveBuilder);
        }
        matrixStack.push();
        //GL30.glBindVertexArray(vao);
        //glBindTexture(GL_TEXTURE_2D, ClientMain.getClient().texture);

        matrixStack.translate(x,y,-250);
        matrixStack.scale(size);
        matrixStack.rotate((float) Math.toRadians(ClientMain.getClient().renderTime / 25),new Vector3f(0,1,0));
        matrixStack.applyTransformation(ClientMain.getClient().shaderManager.colorShader);
        matrixStack.translate(0,0,-5);
        glDrawElements(GL_TRIANGLES, vertexCount,GL_UNSIGNED_INT,0);
        matrixStack.pop();
        if(itemProperties.dynamicModel) {
            VAOManager.destroyBuffer(vao);
        }
        glEnable(GL_DEPTH_TEST);
        drawString(window, matrixStack,x - size / 2,y,size/2,itemStack.count);

        */
    }

    public void addData(TextAtlas textAtlas, PrimitiveBuilder primitiveBuilder, float size) {
        //itemProperties.itemModel.addData(primitiveBuilder,itemProperties.itemTextureManager,0,size,null,0,0);
    }

    public boolean onActivate(IWorld world, PlayerEntity playerEntity) {
        return true;
    }

    void drawString(RenderWindow window, MatrixStack matrixStack, int x, int y, int size, int count) {
        if(count != 1) {
            if(count >= 10) {
              //  window.getStringRenderer().drawStringInternal(window, matrixStack, count + "", x + size + 14 , (int) (y + size * 1f), 0.5f);
            } else {
              //  window.getStringRenderer().drawStringInternal(window, matrixStack, count + "", (int) (x + size) + 29, (int) (y + size * 1f), 0.5f);
            }
        }
    }

    public String getName() {
        return "item." + modID + "." + name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public String getComponentName() {
        return "item";
    }

    @Override
    public void load(GameInstance gameInstance) {
        if(source.gameInstance.side == Side.CLIENT) {
            generateTextures();
        }
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return modID;
    }

    @Override
    public String getResourceType() {
        return "item";
    }

    @Override
    public TagCollection getTagCollection() {
        return itemProperties.tags;
    }
}
