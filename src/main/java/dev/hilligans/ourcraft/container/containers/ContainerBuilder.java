package dev.hilligans.ourcraft.container.containers;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.CustomContainerScreen;
import dev.hilligans.ourcraft.client.rendering.widgets.Widget;
import dev.hilligans.ourcraft.client.rendering.widgets.WidgetFetcher;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.ContainerFetcher;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.data.other.Inventory;

public class ContainerBuilder implements ContainerFetcher {

    short id;
    Slot[] slots;
    WidgetHolder[] widgets;
    String textureName;
    int width;
    int height;

    public ContainerBuilder(short id, String textureName, Slot[] slots, WidgetHolder[] widgets, int width, int height) {
        this.id = id;
        this.slots = slots;
        this.widgets = widgets;
        this.textureName = textureName;
        this.width = width;
        this.height = height;
    }


    @Override
    public Container getContainer() {
        Inventory inventory = new Inventory(slots.length);
        Container container = new Container(id,inventory) {
            @Override
            public ContainerScreen<?> getContainerScreen() {
                return new CustomContainerScreen() {
                    @Override
                    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
                       // Texture texture1 = Textures.getTexture(textureName);
                    //    if(texture1 != null) {
                           // texture1.drawCenteredTexture(matrixStack, 0, 0, width, height, Settings.guiSize);
                    //    }
                    }

                    @Override
                    public int getWidth() {
                        return width;
                    }

                    @Override
                    public int getHeight() {
                        return height;
                    }
                };
            }
        };
        for(Slot slot : slots) {
            slot.inventory = inventory;
            container.addSlot(slot.copy());
        }
        for(WidgetHolder widgetHolder : widgets) {
            container.addWidget(widgetHolder.get());
        }
        container.setTextureSize(width,height);
        container.resize();
        return container;
    }

    public static class WidgetHolder {
        WidgetFetcher widgetFetcher;
        int x;
        int y;
        int width;
        int height;

        public WidgetHolder(WidgetFetcher widgetFetcher, int x, int y, int width, int height) {
            this.widgetFetcher = widgetFetcher;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Widget get() {
            return widgetFetcher.getWidget(x,y,width,height);
        }
    }

}
