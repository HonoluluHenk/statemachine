package ch.christophlinder.statemachine.demo;

import ch.christophlinder.statemachine.demo.actions.*;
import ch.christophlinder.statemachine.demo.utility.ERPService;
import ch.christophlinder.statemachine.entity.EntityStateMachine;

import java.util.HashMap;
import java.util.Map;

public class OrderStateMachine extends EntityStateMachine<Order, OrderState, OrderActions> {
    public OrderStateMachine(ERPService erpService) {
        super(
                () -> new Order(OrderState.INIT),
                Order::getState,
                Order::setState,
                buildStates(erpService)
        );
    }

    private static Map<OrderState, OrderActions> buildStates(ERPService erpService) {
        Map<OrderState, OrderActions> states = new HashMap<>();

        states.put(OrderState.INIT, new InitActions());
        states.put(OrderState.SHOPPING, new ShoppingActions(erpService));
        states.put(OrderState.PLACED, new PlacedActions());

        // please note: since CANCELLED is a terminal state without any actions,
        // nothing needs to be implemented for this state:
        // states.put(OrderState.CANCELLED, new CancelledTransitions());

        return states;
    }

}
