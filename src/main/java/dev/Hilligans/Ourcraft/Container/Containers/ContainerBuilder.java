package dev.Hilligans.Ourcraft.Container.Containers;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.Ourcraft.Client.Rendering.Screens.ContainerScreens.CustomContainerScreen;
import dev.Hilligans.Ourcraft.Client.Rendering.Texture;
import dev.Hilligans.Ourcraft.Client.Rendering.Textures;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.Widget;
import dev.Hilligans.Ourcraft.Client.Rendering.Widgets.WidgetFetcher;
import dev.Hilligans.Ourcraft.Container.Container;
import dev.Hilligans.Ourcraft.Container.ContainerFetcher;
import dev.Hilligans.Ourcraft.Container.Slot;
import dev.Hilligans.Ourcraft.Data.Other.Inventory;
import dev.Hilligans.Ourcraft.Util.Settings;

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
