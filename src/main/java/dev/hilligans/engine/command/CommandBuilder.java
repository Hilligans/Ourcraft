package dev.hilligans.engine.command;

import dev.hilligans.engine.command.executors.CommandExecutor;
import dev.hilligans.engine.data.Triplet;
import dev.hilligans.engine.util.EnumParser;
import dev.hilligans.engine.util.interfaces.QuadConsumer;
import dev.hilligans.engine.util.interfaces.QuinConsumer;
import dev.hilligans.engine.util.interfaces.SextConsumer;
import dev.hilligans.engine.util.interfaces.TriConsumer;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandBuilder {

    public static EmptyCommand<CommandExecutor> create(String... aliases) {
        return new EmptyCommand<>(CommandExecutor.class, aliases);
    }

    public static <T extends CommandExecutor> EmptyCommand<T> create(Class<T> clazz, String... aliases) {
        return new EmptyCommand<>(clazz, aliases);
    }

    public static abstract class BuilderCommand<T> extends Command {
        public int argCount;
        public Class<T> executorClass;

        public BuilderCommand(Class<T> executorClass, int argCount, String[] aliases) {
            super(aliases[0]);
            this.executorClass = executorClass;
            this.argCount = argCount;
            this.aliases = aliases;
        }

        public BuilderCommand(BuilderCommand<T> parent) {
            super(parent.name);
            this.executorClass = parent.executorClass;
            this.argCount = parent.argCount + 1;
            this.aliases = parent.aliases;
            this.permissions = parent.permissions;
        }

        @Override
        public boolean execute(CommandExecutor executor, String[] args) {
            if(args.length < argCount || !executorClass.isInstance(executor)) {
                return false;
            }
            parseAndExecute((T)executor, args);
            return true;
        }

        public abstract void parseAndExecute(T executor, String[] args);
    }


    public static class EmptyCommand<T extends CommandExecutor> extends BuilderCommand<T> {
        public Consumer<T> consumer;

        public EmptyCommand(Class<T> executorClass, String... aliases) {
            super(executorClass, 0, aliases);
        }

        public EmptyCommand(EmptyCommand<T> self, Consumer<T> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor);
        }

        public <X> SingleCommand<T, X> with(BiFunction<ICommand, String, X> parser) {
            return new SingleCommand<>(this, new Triplet<>(parser, null, null));
        }

        public <X> SingleCommand<T, X> with(BiFunction<ICommand, String, X> parser, String description, Function<T, List<String>> validArgs) {
            return new SingleCommand<>(this, new Triplet<>(parser, description, validArgs));
        }

        public <X> SingleCommand<T, X> optional(BiFunction<ICommand, String, X> parser, String description, Function<T, List<String>> validArgs, Function<T, X> defaultArgument) {
            return new SingleCommand<>(this, new Triplet<>(parser, description, validArgs));
        }

        public <X> SingleCommand<T, X> flag(String key, BiFunction<ICommand, String, X> parser, String description, Function<T, List<String>> validArgs) {
            return new SingleCommand<>(this, new Triplet<>(parser, description, validArgs));
        }

        public SingleCommand<T, Boolean> withBool() { return with(CommandBuilder::parseBoolean); }
        public SingleCommand<T, Integer> withInt() { return with(CommandBuilder::parseInt); }
        public SingleCommand<T, Long> withLong() { return with(CommandBuilder::parseLong); }
        public SingleCommand<T, Float> withFloat() { return with(CommandBuilder::parseFloat); }
        public SingleCommand<T, Double> withDouble() { return with(CommandBuilder::parseDouble); }
        public SingleCommand<T, String> withString() { return with((c, str) -> str); }
        public <X extends Enum<X>> SingleCommand<T, X> withEnum(Class<X> clazz) { return with((c, str) -> EnumParser.parse(c, str, clazz)); }
        public <X extends IRegistryElement> SingleCommand<T, X> withElement(Class<X> clazz) { return with((c, str) -> c.getGameInstance().get(str, clazz)); }
        public SingleCommand<T, ICommand> withSubCommand(ICommand... commands) { return null; }
        public TripleCommand<T, Integer, Integer, Integer> withXYZ() { return new TripleCommand<>(this, new Triplet<>(CommandBuilder::parseInt, null, null), new Triplet<>(CommandBuilder::parseInt, null, null), new Triplet<>(CommandBuilder::parseInt, null, null)); }

        public EmptyCommand<T> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public EmptyCommand<T> withDescription(String description) {

            return this;
        }

        public EmptyCommand<T> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(Consumer<T> consumer, ICommand... subCommands) {
            return new EmptyCommand<>(this, consumer);
        }
    }


    public static class SingleCommand<T, A> extends BuilderCommand<T>  {
        public Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA;
        public BiConsumer<T, A> consumer;

        public SingleCommand(BuilderCommand<T> parent, Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA) {
            super(parent);
            this.argA = argA;
        }

        public SingleCommand(BuilderCommand<T> parent, Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA, int argCount) {
            super(parent);
            this.argA = argA;
            this.argCount = argCount;
        }

        public SingleCommand(SingleCommand<T, A> self, BiConsumer<T, A> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.argA = self.argA;
            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, argA.getTypeA().apply(this, args[0]));
        }

        public <X> DoubleCommand<T, A, X> with(BiFunction<ICommand, String, X> parser) {
            return new DoubleCommand<>(this, parser, null, null);
        }

        public <X> DoubleCommand<T, A, X> with(BiFunction<ICommand, String, X> parser, String description, Function<T, List<String>> validArgs) {
            return new DoubleCommand<>(this, new Triplet<>(parser, description, validArgs));
        }

        public <X> DoubleCommand<T, A, X> flag(String key, BiFunction<ICommand, String, X> parser, String description, Function<T, List<String>> validArgs) {
            return new DoubleCommand<>(this, new Triplet<>(parser, description, validArgs));
        }

        public DoubleCommand<T, A, Boolean> withBool() { return with(CommandBuilder::parseBoolean); }
        public DoubleCommand<T, A, Integer> withInt() { return with(CommandBuilder::parseInt); }
        public DoubleCommand<T, A, Long> withLong() { return with(CommandBuilder::parseLong); }
        public DoubleCommand<T, A, Float> withFloat() { return with(CommandBuilder::parseFloat); }
        public DoubleCommand<T, A, Double> withDouble() { return with(CommandBuilder::parseDouble); }
        public DoubleCommand<T, A, String> withString() { return with((c, str) -> str); }
        public <X extends Enum<X>> DoubleCommand<T, A, X> withEnum(Class<X> clazz) { return with((c,str) -> EnumParser.parse(c, str, clazz)); }
        public <X extends IRegistryElement> DoubleCommand<T, A, X> withElement(Class<X> clazz) { return with((c, str) -> c.getGameInstance().get(str, clazz)); }
        public DoubleCommand<T, A, ICommand> withSubCommand(ICommand... commands) { return null; }
        public QuadrupleCommand<T, A, Integer, Integer, Integer> withXYZ() { return new QuadrupleCommand<>(this, argA, new Triplet<>(CommandBuilder::parseInt, null, null), new Triplet<>(CommandBuilder::parseInt, null, null), new Triplet<>(CommandBuilder::parseInt, null, null)); }

        public SingleCommand<T, A> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public SingleCommand<T, A> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(BiConsumer<T, A> consumer) {
            return new SingleCommand<>(this, consumer);
        }
    }

    public static class DoubleCommand<T, A, B> extends BuilderCommand<T> {
        public Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA;
        public Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB;
        public TriConsumer<T, A, B> consumer;

        public DoubleCommand(SingleCommand<T, A> parent, Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB) {
            super(parent);
            this.argA = parent.argA;
            this.argB = argB;
        }

        public DoubleCommand(SingleCommand<T, A> parent, BiFunction<ICommand, String, B> parser, String description, Function<T, List<String>> validArgs) {
            super(parent);
            this.argA = parent.argA;
            this.argB = new Triplet<>(parser, description, validArgs);
        }

        public DoubleCommand(BuilderCommand<T> parent, Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA, Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB) {
            super(parent);
            this.argA = argA;
            this.argB = argB;
            this.argCount = 2;
        }

        public DoubleCommand(DoubleCommand<T, A, B> self, TriConsumer<T, A, B> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.argA = self.argA;
            this.argB = self.argB;
            this.consumer = consumer;
        }

        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, argA.getTypeA().apply(this, args[0]), argB.getTypeA().apply(this, args[1]));
        }

        public <X> TripleCommand<T, A, B, X> with(BiFunction<ICommand, String, X> parser) {
            return new TripleCommand<>(this, new Triplet<>(parser, null, null));
        }

        public TripleCommand<T, A, B, Boolean> withBool() { return with(CommandBuilder::parseBoolean); }
        public TripleCommand<T, A, B, Integer> withInt() { return with(CommandBuilder::parseInt); }
        public TripleCommand<T, A, B, Long> withLong() { return with(CommandBuilder::parseLong); }
        public TripleCommand<T, A, B, Float> withFloat() { return with(CommandBuilder::parseFloat); }
        public TripleCommand<T, A, B, Double> withDouble() { return with(CommandBuilder::parseDouble); }
        public TripleCommand<T, A, B, String> withString() { return with((c, str) -> str); }
        public <X extends Enum<X>> TripleCommand<T, A, B, X> withEnum(Class<X> clazz) { return with((c, str) -> EnumParser.parse(c, str, clazz)); }
        public <X extends IRegistryElement> TripleCommand<T, A, B, X> withElement(Class<X> clazz) { return with((c, str) -> c.getGameInstance().get(str, clazz)); }
        public TripleCommand<T, A, B, ICommand> withSubCommand(ICommand... command) { return null; }
       // public QuintupleCommand<T, A, B, Integer, Integer, Integer> withXYZ() { return new QuintupleCommand<>(this, parserA, parserB, Integer::parseInt, Integer::parseInt, Integer::parseInt); }


        public DoubleCommand<T, A, B> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public DoubleCommand<T, A, B> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(TriConsumer<T, A, B> consumer) {
            return new DoubleCommand<>(this, consumer);
        }
    }

    public static class TripleCommand<T, A, B, C> extends BuilderCommand<T> {
        public Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA;
        public Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB;
        public Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC;
        public QuadConsumer<T, A, B, C> consumer;

        public TripleCommand(DoubleCommand<T, A, B> parent, Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC) {
            super(parent);
            this.argA = parent.argA;
            this.argB = parent.argB;
            this.argC = argC;
        }

        public TripleCommand(BuilderCommand<T> parent, Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA, Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB, Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC) {
            super(parent);
            this.argA = argA;
            this.argB = argB;
            this.argC = argC;
            this.argCount = 3;
        }

        public TripleCommand(TripleCommand<T, A, B, C> self, QuadConsumer<T, A, B, C> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.argA = self.argA;
            this.argB = self.argB;
            this.argC = self.argC;
            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, argA.getTypeA().apply(this, args[0]), argB.getTypeA().apply(this, args[1]), argC.getTypeA().apply(this, args[2]));
        }

        public <X> QuadrupleCommand<T, A, B, C, X> with(BiFunction<ICommand, String, X> parser) {
            return new QuadrupleCommand<>(this, new Triplet<>(parser, null, null));
        }

        public QuadrupleCommand<T, A, B, C, Boolean> withBool() { return with(CommandBuilder::parseBoolean); }
        public QuadrupleCommand<T, A, B, C, Integer> withInt() { return with(CommandBuilder::parseInt); }
        public QuadrupleCommand<T, A, B, C, Long> withLong() { return with(CommandBuilder::parseLong); }
        public QuadrupleCommand<T, A, B, C, Float> withFloat() { return with(CommandBuilder::parseFloat); }
        public QuadrupleCommand<T, A, B, C, Double> withDouble() { return with(CommandBuilder::parseDouble); }
        public QuadrupleCommand<T, A, B, C, String> withString() { return with((c, str) -> str); }
        public <X extends Enum<X>> QuadrupleCommand<T, A, B, C, X> withEnum(Class<X> clazz) { return with((c, str) -> EnumParser.parse(c, str, clazz)); }
        public <X extends IRegistryElement> QuadrupleCommand<T, A, B, C, X> withElement(Class<X> clazz) { return with((c, str) -> c.getGameInstance().get(str, clazz)); }
        public QuadrupleCommand<T, A, B, C, ICommand> withSubCommand(ICommand... command) { return null; }

        public TripleCommand<T, A, B, C> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public TripleCommand<T, A, B, C> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(QuadConsumer<T, A, B, C> consumer) {
            return new TripleCommand<>(this, consumer);
        }
    }

    public static class QuadrupleCommand<T, A, B, C, D> extends BuilderCommand<T> {
        public Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA;
        public Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB;
        public Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC;
        public Triplet<BiFunction<ICommand, String, D>, String, Function<T, List<String>>> argD;
        public QuinConsumer<T, A, B, C, D> consumer;

        public QuadrupleCommand(TripleCommand<T, A, B, C> parent, Triplet<BiFunction<ICommand, String, D>, String, Function<T, List<String>>> argD) {
            super(parent);
            this.argA = parent.argA;
            this.argB = parent.argB;
            this.argC = parent.argC;
            this.argD = argD;
        }

        public QuadrupleCommand(BuilderCommand<T> parent,
                                Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA,
                                Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB,
                                Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC,
                                Triplet<BiFunction<ICommand, String, D>, String, Function<T, List<String>>> argD) {
            super(parent);
            this.argA = argA;
            this.argB = argB;
            this.argC = argC;
            this.argD = argD;
            this.argCount = 4;
        }

        public QuadrupleCommand(QuadrupleCommand<T, A, B, C, D> self, QuinConsumer<T, A, B, C, D> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.argA = self.argA;
            this.argB = self.argB;
            this.argC = self.argC;
            this.argD = self.argD;
            this.consumer = consumer;
        }

        public <X> QuintupleCommand<T, A, B, C, D, X> with(BiFunction<ICommand, String, X> parser) {
            return new QuintupleCommand<>(this, new Triplet<>(parser, null, null));
        }

        public QuintupleCommand<T, A, B, C, D, Boolean> withBool() { return with(CommandBuilder::parseBoolean); }
        public QuintupleCommand<T, A, B, C, D, Integer> withInt() { return with(CommandBuilder::parseInt); }
        public QuintupleCommand<T, A, B, C, D, Long> withLong() { return with(CommandBuilder::parseLong); }
        public QuintupleCommand<T, A, B, C, D, Float> withFloat() { return with(CommandBuilder::parseFloat); }
        public QuintupleCommand<T, A, B, C, D, Double> withDouble() { return with(CommandBuilder::parseDouble); }
        public QuintupleCommand<T, A, B, C, D, String> withString() { return with((c, str) -> str); }
        public <X extends Enum<X>> QuintupleCommand<T, A, B, C, D, X> withEnum(Class<X> clazz) { return with((c, str) -> EnumParser.parse(c, str, clazz)); }
        public <X extends IRegistryElement> QuintupleCommand<T, A, B, C, D, X> withElement(Class<X> clazz) { return with((c, str) -> c.getGameInstance().get(str, clazz)); }
        public QuintupleCommand<T, A, B, C, D, ICommand> withSubCommand(ICommand... command) { return null; }


        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, argA.getTypeA().apply(this, args[0]), argB.getTypeA().apply(this, args[1]), argC.getTypeA().apply(this, args[2]), argD.getTypeA().apply(this, args[3]));
        }

        public BuilderCommand<T> build(QuinConsumer<T, A, B, C, D> consumer) {
            return new QuadrupleCommand<>(this, consumer);
        }
    }

    public static class QuintupleCommand<T, A, B, C, D, E> extends BuilderCommand<T> {
        public Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA;
        public Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB;
        public Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC;
        public Triplet<BiFunction<ICommand, String, D>, String, Function<T, List<String>>> argD;
        public Triplet<BiFunction<ICommand, String, E>, String, Function<T, List<String>>> argE;
        public SextConsumer<T, A, B, C, D, E> consumer;

        public QuintupleCommand(QuadrupleCommand<T, A, B, C, D> parent, Triplet<BiFunction<ICommand, String, E>, String, Function<T, List<String>>> argE) {
            super(parent);
            this.argA = parent.argA;
            this.argB = parent.argB;
            this.argC = parent.argC;
            this.argD = parent.argD;
            this.argE = argE;
        }

        public QuintupleCommand(BuilderCommand<T> parent,
                                Triplet<BiFunction<ICommand, String, A>, String, Function<T, List<String>>> argA,
                                Triplet<BiFunction<ICommand, String, B>, String, Function<T, List<String>>> argB,
                                Triplet<BiFunction<ICommand, String, C>, String, Function<T, List<String>>> argC,
                                Triplet<BiFunction<ICommand, String, D>, String, Function<T, List<String>>> argD,
                                Triplet<BiFunction<ICommand, String, E>, String, Function<T, List<String>>> argE) {
            super(parent);
            this.argA = argA;
            this.argB = argB;
            this.argC = argC;
            this.argD = argD;
            this.argE = argE;
            this.argCount = 5;
        }

        public QuintupleCommand(QuintupleCommand<T, A, B, C, D, E> self, SextConsumer<T, A, B, C, D, E> consumer) {
            super(self);
            this.argCount = self.argCount;
            this.argA = self.argA;
            this.argB = self.argB;
            this.argC = self.argC;
            this.argD = self.argD;
            this.argE = self.argE;
            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, argA.getTypeA().apply(this, args[0]), argB.getTypeA().apply(this, args[1]), argC.getTypeA().apply(this, args[2]), argD.getTypeA().apply(this, args[3]), argE.getTypeA().apply(this, args[4]));
        }

        public BuilderCommand<T> build(SextConsumer<T, A, B, C, D, E> consumer) {
            return new QuintupleCommand<>(this, consumer);
        }
    }

    public static String[] concatenate(String[] a, String[] b) {
        if(a == null) {
            return b;
        } else if(b == null) {
            return a;
        }

        int aLen = a.length;
        int bLen = b.length;

        String[] c = (String[]) Array.newInstance(String.class, aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    public static boolean parseBoolean(ICommand command, String val) {
        return Boolean.parseBoolean(val);
    }

    public static int parseInt(ICommand command, String val) {
        return Integer.parseInt(val);
    }

    public static long parseLong(ICommand command, String val) {
        return Long.parseLong(val);
    }

    public static float parseFloat(ICommand command, String val) {
        return Float.parseFloat(val);
    }

    public static double parseDouble(ICommand command, String val) {
        return Double.parseDouble(val);
    }
}
