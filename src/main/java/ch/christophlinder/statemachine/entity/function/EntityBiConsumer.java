package ch.christophlinder.statemachine.entity.function;

import edu.umd.cs.findbugs.annotations.NonNull;

@FunctionalInterface
public interface EntityBiConsumer<T, U> {
    void accept(@NonNull T t, @NonNull U u);
}
