package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.TransitionNotAllowed;
import ch.christophlinder.statemachine.entity.Outcome;

public interface YourTransitions {

    class YourOutcome<R> extends Outcome<YourState> {

        private final R result;

        public YourOutcome(YourState state, R result) {
            super(state);
            this.result = result;
        }

        public R getResult() {
            return result;
        }
    }

    default YourEntity initialize(YourEntity entity) {
        throw new TransitionNotAllowed();
    }

    default YourEntity initializeWithParams(YourEntity entity, String message) {
        throw new TransitionNotAllowed();
    }

    default YourEntity initializeWithNewInstance(YourEntity entity, YourState otherEntitySTate) {
        throw new TransitionNotAllowed();
    }

    default YourResult<String> cancelWithResult(String resultMessage) {
        throw new TransitionNotAllowed();
    }

    default YourOutcome<String> goNext() {
        throw new TransitionNotAllowed();
    }
}
