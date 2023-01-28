package dev.hilligans.ourcraft.Data.Descriptors;

public abstract class Descriptor<T> {

    public String name;
    public String modID;
    public TagCollection tagCollection = new TagCollection();
    public boolean matchesExactly = true;

    public boolean matches(T object) {
        return compare(object) == 0;
    }

    public abstract long compare(T object, BitPriority bitPriority);

    public long compare(T object) {
        return compare(object,DEFAULT_PRIORITY);
    }


    public static final BitPriority DEFAULT_PRIORITY = new BitPriority(1L<<62,1L<<61,1L<<60,1L<<59);

    public static class BitPriority {
        public long modID;
        public long name;
        public long tags;
        public long nbt;

        public BitPriority(long modID, long name, long tags, long nbt) {
            this.modID = modID;
            this.name = name;
            this.tags = tags;
            this.nbt = nbt;
        }

    }


}
