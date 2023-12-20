package dev.hilligans.ourcraft.util.sections;

public class BreakPointSection implements ISection {

    public String breakWord;

    public BreakPointSection(String breakWorld) {
        this.breakWord = breakWorld;
    }

    @Override
    public SectionView startSection(String name) {
        return null;
    }

    @Override
    public void stopSection(String name) {

    }
}
