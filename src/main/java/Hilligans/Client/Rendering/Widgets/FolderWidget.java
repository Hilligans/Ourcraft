package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.IntegerWrapper;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.ListTag;
import Hilligans.Tag.Tag;

import java.util.ArrayList;
import java.util.TreeMap;

public class FolderWidget extends Widget {

    public static final int size = 29;
    public static final int spacing = 32;
    static final int length = 150;

    ArrayList<Widget> widgets = new ArrayList<>();
    String name;
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
            ((FolderWidget) widget).minY = this.minY;
        }
        if(widget instanceof DataWidget) {
            ((DataWidget) widget).minY = this.minY;
        }
        widgets.add(widget);
        update();
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        if (this.getY() > 0 && this.getY() < ClientMain.getWindowY() && this.isActive()) {
            if (shouldRender) {
                if (isOpen) {
                    Renderer.drawTexture(matrixStack, Textures.MINUS_ICON, getX() + xOffset + size / 4, getY() + yOffset + size / 4, size / 2, size / 2);
                } else {
                    Renderer.drawTexture(matrixStack, Textures.PLUS_ICON, getX() + xOffset + size / 4, getY() + yOffset + size / 4, size / 2, size / 2);
                }
                if(isList) {
                    Renderer.drawTexture(matrixStack, Textures.LIST_ICON, getX() + xOffset + size, getY() + yOffset, size, size);
                } else {
                    Renderer.drawTexture(matrixStack, Textures.FOLDER, getX() + xOffset + size, getY() + yOffset, size, size);
                }
                StringRenderer.drawString(matrixStack, name, getX() + xOffset + size * 2, getY() + yOffset, 0.5f);
            }
        }
        if (isOpen) {
            for (Widget widget : widgets) {
                widget.render(matrixStack, xOffset, yOffset);
            }
        }
    }

    @Override
    public boolean isInBounds(int x, int y) {
        if(x > this.getX() && x < this.getX() + length && y > this.getY() && y < this.getY() + size && this.isActive()) {
            //System.out.println("yes");
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

    public void addAll(CompoundTag compoundTag) {
        TreeMap<String, Tag> tags = new TreeMap<>(compoundTag.tags);
        for(String string : tags.keySet()) {
            Tag tag = compoundTag.getTag(string);
            if(tag.getId() == 0) {
                FolderWidget folderWidget = new FolderWidget(string);
                addWidget(folderWidget);
                folderWidget.addAll((CompoundTag) tag);
            } else if(tag.getId() == 10) {
                FolderWidget folderWidget = new FolderWidget(string);
                folderWidget.isList = true;
                addWidget(folderWidget);
                folderWidget.addAll((ListTag<?>)tag);
            } else {
                addWidget(new DataWidget(100,FolderWidget.spacing,tag.getId() - 1, tag.getVal(),string));
            }
        }
    }

    public void addAll(ListTag<?> listTag) {
        int x = 0;
        for(Tag tag : listTag.tags) {
            if(tag.getId() == 0) {
                FolderWidget folderWidget = new FolderWidget(x + "");
                folderWidget.addAll((CompoundTag)tag);
                addWidget(folderWidget);
            } else {
                addWidget(new DataWidget(100,FolderWidget.spacing,tag.getId() - 1, tag.getVal(),""));
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
