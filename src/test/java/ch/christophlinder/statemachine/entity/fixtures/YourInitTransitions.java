package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.fixtures.MyStates;

public class YourInitTransitions implements YourTransitions {
    @Override
    public Outcome<MyStates, String> goNext() {
        return new Outcome<>(MyStates.NEXT, "Hello World");
    }
}
