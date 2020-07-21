package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.fixtures.MyStates;

public class YourInitTransitions implements YourTransitions {
    @Override
    public YourOutcome<String> goNext() {
        return new YourOutcome<>(MyStates.NEXT, "Hello World");
    }
}
