package dev.Hilligans.ourcraft.Container.Containers;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.CustomContainerScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Widget;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.WidgetFetcher;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.ContainerFetcher;
import dev.Hilligans.ourcraft.Container.Slot;
import dev.Hilligans.ourcraft.Data.Other.Inventory;
import dev.Hilligans.ourcraft.Util.Settings;

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
            public ContainerScreen<?> getContainerScreen(Client client) {
                return new CustomContainerScreen(null) {
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
