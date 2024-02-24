package dev.hilligans.ourcraft.util.sections;

import dev.hilligans.ourcraft.util.ConsoleReader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SteppingSection implements ISection {

    public ConsoleReader consoleReader;
    public AtomicInteger step = new AtomicInteger();

    public SteppingSection() {
        consoleReader = new ConsoleReader(s -> {
            if(s.equals("n")) {step.incrementAndGet(); }
        });
    }

    @Override
    public SectionView startSection(String name) {
        System.out.println(STR."Waiting on section:\{name}");
        while(step.get() == 0) {}
        step.decrementAndGet();
        return new SectionView(name, this);
    }

    @Override
    public void stopSection(String name) {

    }

    @Override
    public synchronized <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, T... names) {
        //todo implement
        throw new NullPointerException("Unimplemented");
    }

    @Override
    public <T extends Supplier<String>> void startSubSection(BiConsumer<ISection, T> consumer, List<T> names) {

    }

    @Override
    public void stopSubSection(String name) {

    }
}
