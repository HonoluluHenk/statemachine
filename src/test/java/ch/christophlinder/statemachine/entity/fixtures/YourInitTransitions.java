package ch.christophlinder.statemachine.entity.fixtures;

public class YourInitTransitions implements YourTransitions {
    @Override
    public YourEntity initialize(YourEntity entity) {
        entity.setMessage("set in initialize");

        return entity;
    }

    @Override
    public YourEntity initializeWithParams(YourEntity entity, String message) {
        entity.setMessage(message);

        return entity;
    }

    @Override
    public YourEntity initializeWithNewInstance(YourEntity entity, YourState otherEntityState) {
        return new YourEntity(otherEntityState, "other instance");
    }

    @Override
    public YourOutcome<String> goNext() {
        return new YourOutcome<>(YourState.NEXT, "Hello World");
    }
}
