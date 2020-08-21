package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Represents an action-{@link Outcome} with an additional result value.
 */
public class Result<State, R> extends Outcome<State> {
    @NonNull
    private final R value;

    protected Result(
            @Nullable State nextState,
            @NonNull R value
    ) {
        super(nextState);
        this.value = requireNonNull(value);
    }

    @NonNull
    public static <State, R> Result<State, R> of(
            @NonNull State state,
            @NonNull R value
    ) {
        requireNonNull(state);
        requireNonNull(value);

        return new Result<>(requireNonNull(state), value);
    }

    @NonNull
    public static <State, R> Result<State, R> sameState(
            @NonNull R result
    ) {
        requireNonNull(result);

        return new Result<>(null, result);
    }

    @NonNull
    public R getValue() {
        return value;
    }

    /**
     * Convenience: allow initializing and then re-setting the value.
     * <p>
     * Example:
     * <pre>{@code
     * Result r = Result.of("Foo", "meh");
     * if (!itsWednesdayMyFriends) {
     *     return r;
     * }
     * return r.withValue("wednesday!");
     * }</pre>
     */
    @NonNull
    public <NewR> Result<State, NewR> withValue(@NonNull NewR newValue) {
        requireNonNull(newValue);

        State nextState = nextState()
                .orElse(null);

        return new Result<>(nextState, newValue);
    }

}
