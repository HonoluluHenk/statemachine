package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.Result;

public class YourNextTransitions implements YourTransitions {
    @Override
    public Result<YourState, String> cancelWithResult(String resultMessage) {
        return Result.of(YourState.CANCELLED, resultMessage);
    }
}
