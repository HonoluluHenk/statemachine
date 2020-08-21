package ch.christophlinder.statemachine.entity.function;

import edu.umd.cs.findbugs.annotations.NonNull;

@FunctionalInterface
public interface EntitySupplier<T> {
    @NonNull
    T get();
}
