package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Return value of actions: represents the next state the entity should transition to.
 * <p>
 * See {@link Result} for an outcome with result value.
 */
public class Outcome<State> {
    @Nullable
    private final State nextState;

    protected Outcome(@Nullable State nextState) {
        this.nextState = nextState;
    }

    @NonNull
    public static <NextState> Outcome<NextState> of(@NonNull NextState nextState) {
        return new Outcome<>(requireNonNull(nextState));
    }

    /**
     * Signal the state-machine to keep the current state unchanged.
     */
    @NonNull
    public static <NextState> Outcome<NextState> sameState() {
        return new Outcome<>(null);
    }

    @NonNull
    public Optional<State> nextState() {
        return Optional.ofNullable(nextState);
    }

}
