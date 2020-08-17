package ch.christophlinder.statemachine.demo.actions;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.demo.Order;
import ch.christophlinder.statemachine.demo.OrderLine;
import ch.christophlinder.statemachine.demo.OrderState;
import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.entity.Result;

public interface OrderActions {
    default Result<OrderState, Order> initialize(Order emptyOrder, String customer) {
        throw new ActionDeniedException();
    }
    
    default Result<OrderState, OrderLine> addOrderLine(Order order, String lineItem) {
        throw new ActionDeniedException();
    }
    
    default Outcome<OrderState> placeOrder(Order order) {
        throw new ActionDeniedException();
    }

    default Result<OrderState, String> printReceipt(Order order) {
        throw new ActionDeniedException();
    }
}
