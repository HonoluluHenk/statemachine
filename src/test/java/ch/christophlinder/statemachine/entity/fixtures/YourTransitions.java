package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.TransitionNotAllowed;
import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.fixtures.MyStates;

public interface YourTransitions {
    class YourOutcome<R> extends Outcome<MyStates> {
        private final R result;

        public YourOutcome(MyStates myStates, R result) {
            super(myStates);
            this.result = result;
        }

        public R getResult() {
            return result;
        }
    }


    default YourOutcome<String> goNext() {
        throw new TransitionNotAllowed();
    }
}
