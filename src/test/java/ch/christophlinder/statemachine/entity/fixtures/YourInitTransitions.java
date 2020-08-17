package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.Result;

public class YourInitTransitions implements YourTransitions {
    @Override
    public Result<YourState, YourEntity> initialize(YourEntity entity) {
        entity.setMessage("set in initialize");

        return Result.sameState(entity);
    }

    @Override
    public Result<YourState, YourEntity> initializeWithParams(YourEntity entity, String message) {
        entity.setMessage(message);

        return Result.sameState(entity);
    }

    @Override
    public Result<YourState, YourEntity> initializeWithNewInstance(YourEntity entity, YourState otherEntityState) {
        return Result.of(otherEntityState, new YourEntity(otherEntityState, "other instance"));
    }

    @Override
    public Result<YourState, String> goNext() {
        return Result.of(YourState.NEXT, "Hello World");
    }
}
