package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.entity.Result;

public class YourNextTransitions implements YourTransitions {
	@Override
	public Result<YourState, String> cancelWithResult(String resultMessage) {
		return Result.of(YourState.CANCELLED, resultMessage);
	}

	@Override
	public Outcome<YourState> cancelWithoutResult(YourEntity entity, String resultMessage) {
		entity.setMessage(resultMessage);

		return Outcome.of(YourState.CANCELLED);
	}

	@Override
	public Result<YourState, String> keepState(String message) {
		return Result.sameState(message);
	}
}
