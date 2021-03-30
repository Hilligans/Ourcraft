package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.DataWidget;
import Hilligans.Client.Rendering.Widgets.FolderWidget;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.IntegerWrapper;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.Tag;
import Hilligans.WorldSave.FileLoader;
import Hilligans.WorldSave.WorldLoader;
import org.lwjgl.PointerBuffer;

public class TagEditorScreen extends ScreenBase {

    static final int size = 29;

    CompoundTag compoundTag;
    public String filePath;
    public FolderWidget folderWidget;
    public IntegerWrapper yOffset;

    public TagEditorScreen() {
        folderWidget = new FolderWidget(100,100,100,40,"Data");
        folderWidget.minY = 100 + FolderWidget.spacing;
        widgets.add(folderWidget);
        widgets.add(new Button(0, 0, 100, 40, "Open", () -> FileLoader.openFile("dat", null, new FileLoader.HandleFile() {
            @Override
            public void success(PointerBuffer path) {
                path.mark();
                compoundTag = WorldLoader.loadTag(path.getStringUTF8());
                path.reset();
                filePath = path.getStringUTF8();
                yOffset = new IntegerWrapper(0);
                folderWidget.addAll(compoundTag);
                folderWidget.addOffset(yOffset);
                //System.out.println(compoundTag);
                folderWidget.yOffset = new IntegerWrapper(0);
                folderWidget.minY = 100;
            }
            @Override
            public void cancel() {
                ClientMain.openScreen(new JoinScreen());
            }
        })));
        widgets.add(new Button(100, 0, 100, 40, "Save", () -> WorldLoader.save(compoundTag,filePath)));
    }

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);

    }

    @Override
    public void mouseClick(int x, int y, int mouseButton) {
        super.mouseClick(x, y, mouseButton);
    }

    @Override
    public void mouseScroll(int x, int y, float amount) {
        if(amount == 1.0f) {
            if(yOffset.value < 0) {
                yOffset.value += FolderWidget.spacing;
            }
        } else {
            yOffset.value-= FolderWidget.spacing;
        }
    }
}
