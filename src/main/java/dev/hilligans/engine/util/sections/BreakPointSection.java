package dev.hilligans.engine.util.sections;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

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

    @Override
    public synchronized <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, T... names) {

    }

    @Override
    public <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, List<T> names) {

    }

    @Override
    public void stopSubSection(String name) {

    }
}
