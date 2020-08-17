package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class Outcome<State> {
    @Nullable
    private final State nextState;

    protected Outcome(@Nullable State nextState) {
        this.nextState = nextState;
    }

    public static <NextState> Outcome<NextState> of(NextState nextState) {
        return new Outcome<>(requireNonNull(nextState));
    }

    /**
     * Signal the state-machine to keep the current state unchanged.
     */
    public static <NextState> Outcome<NextState> sameState() {
        return new Outcome<>(null);
    }

    @Nullable
    protected State getNextState() {
        return nextState;
    }

    public Optional<State> nextState() {
        return Optional.ofNullable(nextState);
    }

}
