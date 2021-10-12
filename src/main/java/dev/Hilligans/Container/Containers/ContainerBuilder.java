package dev.Hilligans.Container.Containers;

import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Client.Rendering.ContainerScreen;
import dev.Hilligans.Client.Rendering.Screens.ContainerScreens.CustomContainerScreen;
import dev.Hilligans.Client.Rendering.Texture;
import dev.Hilligans.Client.Rendering.Textures;
import dev.Hilligans.Client.Rendering.Widgets.Widget;
import dev.Hilligans.Client.Rendering.Widgets.WidgetFetcher;
import dev.Hilligans.Container.Container;
import dev.Hilligans.Container.ContainerFetcher;
import dev.Hilligans.Container.Slot;
import dev.Hilligans.Data.Other.Inventory;
import dev.Hilligans.Util.Settings;

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
                    public void drawScreen(MatrixStack matrixStack) {
                        Texture texture1 = Textures.getTexture(textureName);
                        if(texture1 != null) {
                            texture1.drawCenteredTexture(matrixStack, 0, 0, width, height, Settings.guiSize);
                        }
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
