package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.data.primitives.IntegerWrapper;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.tag.ListNBTTag;
import dev.hilligans.ourcraft.tag.NBTTag;

import java.util.ArrayList;
import java.util.TreeMap;

public class  FolderWidget extends Widget {

    public static final int size = 29;
    public static final int spacing = 32;
    static final int length = 150;

    ArrayList<Widget> widgets = new ArrayList<>();
    boolean isOpen = false;
    public int scrollY = 0;

    boolean isList = false;

    FolderWidget parent;

    public FolderWidget(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        this.name = name;
    }

    public FolderWidget(String name) {
        super(0, 0, 0, 0);
        this.name = name;
    }

    public FolderWidget addWidget(Widget widget) {
        if(widget instanceof FolderWidget) {
            ((FolderWidget) widget).parent = this;
        }
        widget.minY = this.minY;
        widgets.add(widget);
        update();
        return this;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, matrixStack, xOffset, yOffset);
        if (this.getY() > 0 && this.getY() < window.getWindowHeight() && this.isActive()) {
            if (shouldRender) {
                if (isOpen) {
                 //   Textures.MINUS_ICON.drawTexture(matrixStack, getX() + xOffset + size / 4, getY() + yOffset + size / 4, size / 2, size / 2);
                } else {
                 //   Textures.PLUS_ICON.drawTexture(matrixStack, getX() + xOffset + size / 4, getY() + yOffset + size / 4, size / 2, size / 2);
                }
                if(isList) {
                 //   Textures.LIST_ICON.drawTexture(matrixStack, getX() + xOffset + size, getY() + yOffset, size, size);
                } else {
                  //  Textures.FOLDER.drawTexture(matrixStack, getX() + xOffset + size, getY() + yOffset, size, size);
                }
                window.getStringRenderer().drawStringInternal(window, matrixStack, name, getX() + xOffset + size * 2, getY() + yOffset, 0.5f);
            }
        }
        if (isOpen) {
            for (Widget widget : widgets) {
                widget.render(window, matrixStack, xOffset, yOffset);
            }
        }
    }

    @Override
    public boolean isInBounds(int x, int y) {
        if(x > this.getX() && x < this.getX() + length && y > this.getY() && y < this.getY() + size && this.isActive()) {
            return true;
        }
        if(isOpen) {
            for (Widget widget : widgets) {
                if (widget.isInBounds(x, y)) {
                    return true;
                }
            }
        }
        return super.isInBounds(x,y);
    }

    @Override
    public void activate(int x, int y) {
        if(x > 0 && x < length && y > 0 && y < size) {
            isOpen = !isOpen;
            update();
        } else {
            for (Widget widget : widgets) {
                if (widget.isInBounds(x + this.getX(), y + this.getY())) {
                    widget.activate(x - widget.getX() + this.getX(), y - widget.getY() + this.getY());
                    return;
                }
            }
        }
    }

    public void update() {
        if(parent != null) {
            height = spacing;
            if(isOpen) {
                for (Widget widget : widgets) {
                    int y = height + scrollY;
                    widget.shouldRender = true;
                    if(y - spacing < 0) {
                        widget.shouldRender = false;
                    }
                    widget.y = y + this.y;
                    widget.x = this.x + spacing;
                    height += widget.height;
                }
            }
            parent.update();
        } else {
            updateDown();
        }
    }

    public void updateDown() {
        height = spacing;
        if(isOpen) {
            for (Widget widget : widgets) {
                int y = height + scrollY;
                widget.shouldRender = true;
                if(y - spacing < 0) {
                    widget.shouldRender = false;
                }
                widget.y = y + this.y;
                widget.x = this.x + spacing;
                height += widget.height;
                if(widget instanceof FolderWidget) {
                    ((FolderWidget) widget).updateDown();
                }
            }
        }
    }

    public void addAll(CompoundNBTTag compoundTag) {
        TreeMap<String, NBTTag> tags = new TreeMap<>(compoundTag.tags);
        for(String string : tags.keySet()) {
            NBTTag NBTTag = compoundTag.getTag(string);
            if(NBTTag.getId() == 10) {
                FolderWidget folderWidget = new FolderWidget(string);
                addWidget(folderWidget);
                folderWidget.addAll((CompoundNBTTag) NBTTag);
            } else if(NBTTag.getId() == 9) {
                FolderWidget folderWidget = new FolderWidget(string);
                folderWidget.isList = true;
                addWidget(folderWidget);
                folderWidget.addAll((ListNBTTag<?>) NBTTag);
            } else {
                addWidget(new DataWidget(100,FolderWidget.spacing, NBTTag.getId() - 1, NBTTag.getVal(),string));
            }
        }
    }

    public void addAll(ListNBTTag<?> listTag) {
        int x = 0;
        for(NBTTag NBTTag : listTag.tags) {
            if(NBTTag.getId() == 0) {
                FolderWidget folderWidget = new FolderWidget(x + "");
                folderWidget.addAll((CompoundNBTTag) NBTTag);
                addWidget(folderWidget);
            } else {
                addWidget(new DataWidget(100,FolderWidget.spacing, NBTTag.getId() - 1, NBTTag.getVal(),""));
            }
            x++;
        }
    }

    @Override
    public Widget addOffset(IntegerWrapper integerWrapper) {
        for(Widget widget : widgets) {
            widget.addOffset(integerWrapper);
        }
        return super.addOffset(integerWrapper);
    }

}
