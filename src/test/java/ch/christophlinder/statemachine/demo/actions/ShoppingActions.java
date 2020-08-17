package ch.christophlinder.statemachine.demo.actions;

import ch.christophlinder.statemachine.demo.Order;
import ch.christophlinder.statemachine.demo.OrderLine;
import ch.christophlinder.statemachine.demo.OrderState;
import ch.christophlinder.statemachine.demo.utility.ERPService;
import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.entity.Result;

public class ShoppingActions implements OrderActions {
    private final ERPService erpService;

    public ShoppingActions(ERPService erpService) {
        this.erpService = erpService;
    }

    @Override
    public Result<OrderState, OrderLine> addOrderLine(Order order, String lineItem) {
        OrderLine newLine = new OrderLine(lineItem);
        order.addOrderLine(newLine);

        return Result.sameState(newLine);
    }

    @Override
    public Outcome<OrderState> placeOrder(Order order) {
        erpService.orderPlaced(order);

        return Outcome.of(OrderState.PLACED);
    }
}
