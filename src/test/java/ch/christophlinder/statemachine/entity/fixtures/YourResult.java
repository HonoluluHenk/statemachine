package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.Outcome;

public class YourResult<R> extends Outcome<YourState> {
    private final R result;

    public YourResult(YourState yourState, R result) {
        super(yourState);
        this.result = result;
    }

    public R getResult() {
        return result;
    }
}
