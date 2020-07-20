package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class Outcome<NextState, R> {
    private final NextState nextState;
    private final R result;

    public Outcome(NextState nextState, R result) {
        this.nextState = requireNonNull(nextState, "No nextState");
        this.result = requireNonNull(result, "No result");
    }

    public NextState getNextState() {
        return nextState;
    }

    public R getResult() {
        return result;
    }
}
