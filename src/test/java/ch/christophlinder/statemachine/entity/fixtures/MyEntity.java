package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.entity.EntityWithState;
import ch.christophlinder.statemachine.fixtures.MyStates;

public class MyEntity implements EntityWithState<MyStates> {
    private MyStates state;

    public MyEntity(MyStates state) {
        this.state = state;
    }

    @Override
    public MyStates getState() {
        return state;
    }

    @Override
    public void setState(MyStates state) {
        this.state = state;
    }
}
