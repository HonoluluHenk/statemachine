package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class Outcome<NextState> {
    private final NextState nextState;

    public Outcome(NextState nextState) {
        this.nextState = requireNonNull(nextState, "No nextState");
    }

    public NextState getNextState() {
        return nextState;
    }
}
