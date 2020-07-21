package ch.christophlinder.statemachine.entity.fixtures;

public class YourNextTransitions implements YourTransitions {
    @Override
    public YourResult<String> cancelWithResult(String resultMessage) {
        return new YourResult<>(YourState.CANCELLED, resultMessage);
    }
}
