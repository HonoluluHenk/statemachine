package ch.christophlinder.statemachine.entity;

public interface EntityWithState<State> {
    State getState();

    void setState(State state);
}
