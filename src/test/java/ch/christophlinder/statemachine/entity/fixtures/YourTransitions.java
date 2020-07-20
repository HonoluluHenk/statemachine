package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.TransitionNotAllowed;
import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.fixtures.MyStates;

public interface YourTransitions {
    default Outcome<MyStates, String> goNext() {
        throw new TransitionNotAllowed();
    }
}
