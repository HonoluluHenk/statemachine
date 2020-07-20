package ch.christophlinder.statemachine.fixtures.transitions;

import edu.umd.cs.findbugs.annotations.Nullable;

public class MyInitTransitions implements MyTransitions {
    @Override
    @Nullable
    public String goNext(String a, String b) {
        return a + b;
    }

    @Nullable
    @Override
    public String returnNull() {
        return null;
    }
}
