package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class Result<State, R> extends Outcome<State> {
	private final R value;

	protected Result(
			@Nullable State nextState,
			R value
	) {
		super(nextState);
		this.value = requireNonNull(value);
	}

	public static <State, R> Result<State, R> of(State state, R value) {
		return new Result<>(requireNonNull(state), value);
	}

	public static <State, R> Result<State, R> sameState(R result) {
		return new Result<>(null, result);
	}

	public R getValue() {
		return value;
	}

}
