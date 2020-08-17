package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class Result<State, R> extends Outcome<State> {
    private final R result;

    protected Result(
            @Nullable State o,
            R result
    ) {
        super(o);
        this.result = result;
    }

    public static <State, R> Result<State, R> of(State state, R result) {
        return new Result<>(requireNonNull(state), result);
    }

    public static <State, R> Result<State, R> sameState(R result) {
        return new Result<>(null, result);
    }

    public R getResult() {
        return result;
    }

}
