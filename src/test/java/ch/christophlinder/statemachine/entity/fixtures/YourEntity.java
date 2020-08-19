package ch.christophlinder.statemachine.entity.fixtures;

public class YourEntity {
    private YourState state;
    private String message;

    public YourEntity(YourState state) {
        this.state = state;
    }

    public YourEntity(YourState state, String message) {
        this.state = state;
        this.message = message;
    }

    public YourState getState() {
        return state;
    }

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
