package ch.christophlinder.statemachine.demo.utility;

import ch.christophlinder.statemachine.demo.Order;

public class ERPService {
    public void orderPlaced(Order order) {
        System.out.println("Order placed: " + order.getCustomer() + ", " + order.getOrderLines());
    }
}
