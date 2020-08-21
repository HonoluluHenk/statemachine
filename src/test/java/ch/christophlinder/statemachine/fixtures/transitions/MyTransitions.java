package ch.christophlinder.statemachine.fixtures.transitions;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.fixtures.AppFailureException;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public interface MyTransitions {
    @Nullable
    default String goNext(String a, String b) {
        throw new ActionDeniedException(String.format("params: %s/%s", a, b));
    }

    default boolean accept(String x) {
        throw new ActionDeniedException();
    }

    default String someResult() {
        throw new ActionDeniedException();
    }

    default void cancel() {
        throw new AppFailureException();
    }

    @Nullable
    default String returnNull() {
        throw new ActionDeniedException();
    }

    default void sideEffect(AtomicInteger inOut, int newValue) {
        throw new ActionDeniedException();
    }
}
