package Hilligans.Container.Containers;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Screens.ContainerScreens.CustomContainerScreen;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Client.Rendering.Widgets.WidgetFetcher;
import Hilligans.Container.Container;
import Hilligans.Container.ContainerFetcher;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.Inventory;
import Hilligans.Util.Settings;

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
        Container container = new Container(id) {
            @Override
            public ContainerScreen<?> getContainerScreen() {
                return new CustomContainerScreen() {
                    @Override
                    public void drawScreen(MatrixStack matrixStack) {
                        Texture texture1 = Textures.getTexture(textureName);
                        if(texture1 != null) {
                            Renderer.drawCenteredTexture(matrixStack, texture1, 0, 0, width, height, Settings.guiSize);
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
