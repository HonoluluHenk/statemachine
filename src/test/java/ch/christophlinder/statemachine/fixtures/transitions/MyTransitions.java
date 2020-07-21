package ch.christophlinder.statemachine.fixtures.transitions;

import ch.christophlinder.statemachine.TransitionNotAllowed;
import ch.christophlinder.statemachine.fixtures.AppFailureException;
import edu.umd.cs.findbugs.annotations.Nullable;

public interface MyTransitions {
    @Nullable
    default String goNext(String a, String b) {
        throw new TransitionNotAllowed(String.format("params: %s/%s", a, b));
    }

    default boolean accept(String x) {
        throw new TransitionNotAllowed();
    }

    default String someResult() {
        throw new TransitionNotAllowed();
    }

    default void cancel() {
        throw new AppFailureException();
    }

    @Nullable
    default String returnNull() {
        throw new TransitionNotAllowed();
    }
}
