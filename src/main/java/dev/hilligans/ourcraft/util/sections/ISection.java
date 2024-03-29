package dev.hilligans.ourcraft.util.sections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ISection {

    SectionView startSection(String name);

    void stopSection(String name);

    <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, T... names);

    <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, List<T> names);


    void stopSubSection(String name);

    static ISection getSection(String name) {
        if("stepping".equals(name)) {
            return new SteppingSection();
        } else if ("profiled".equals(name)) {
            return new ProfiledSection();
        }
        return new EmptySection();
    }
}
