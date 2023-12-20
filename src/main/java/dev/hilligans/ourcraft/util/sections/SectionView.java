package dev.hilligans.ourcraft.util.sections;

public record SectionView(String sectionName, ISection section) implements AutoCloseable {
    @Override
    public void close() {
        section.stopSection(sectionName);
    }
}