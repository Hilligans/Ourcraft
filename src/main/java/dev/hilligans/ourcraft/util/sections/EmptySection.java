package dev.hilligans.ourcraft.util.sections;

public class EmptySection implements ISection {

    public static final ISection EMPTY_SECTION_INSTANCE = new EmptySection();

    @Override
    public SectionView startSection(String name) {
        return new SectionView(name, this);
    }

    @Override
    public void stopSection(String name) {}
}
