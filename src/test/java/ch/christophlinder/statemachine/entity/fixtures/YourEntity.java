package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.EntityWithState;

public class YourEntity implements EntityWithState<YourState> {
    private YourState state;
    private String message;

    public YourEntity(YourState state) {
        this.state = state;
    }

    public YourEntity(YourState state, String message) {
        this.state = state;
        this.message = message;
    }

    @Override
    public YourState getState() {
        return state;
    }

    @Override
    public void setState(YourState state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
