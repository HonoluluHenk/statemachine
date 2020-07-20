package ch.christophlinder.statemachine.fixtures.transitions;

import edu.umd.cs.findbugs.annotations.Nullable;

public class MyNullTransitions implements MyTransitions{
    @Nullable
    @Override
    public String returnNull() {
        return null;
    }
}
