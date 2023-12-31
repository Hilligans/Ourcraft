package dev.hilligans.ourcraft.util.sections;

import dev.hilligans.ourcraft.util.ConsoleReader;

import java.util.concurrent.atomic.AtomicInteger;

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
}
