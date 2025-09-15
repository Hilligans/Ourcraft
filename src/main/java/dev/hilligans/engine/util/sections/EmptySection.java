package dev.hilligans.engine.util.sections;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EmptySection implements ISection {

    public static final ISection EMPTY_SECTION_INSTANCE = new EmptySection();

    @Override
    public SectionView startSection(String name) {
        return new SectionView(name, this);
    }

    @Override
    public void stopSection(String name) {}

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
