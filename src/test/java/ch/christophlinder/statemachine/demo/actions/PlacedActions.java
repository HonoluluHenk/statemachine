package ch.christophlinder.statemachine.demo.actions;

import ch.christophlinder.statemachine.demo.Order;
import ch.christophlinder.statemachine.demo.OrderLine;
import ch.christophlinder.statemachine.demo.OrderState;
import ch.christophlinder.statemachine.entity.Result;

import java.util.stream.Collectors;

public class PlacedActions implements OrderActions {

    @Override
    public Result<OrderState, String> printReceipt(Order order) {
        String receipt = "Receipt: " + order.getOrderLines()
                .stream()
                .map(OrderLine::getItem)
                .collect(Collectors.joining(","));

        // stay in same state (might also just pass the constant)
        return Result.of(order.getState(), receipt);
    }
}
