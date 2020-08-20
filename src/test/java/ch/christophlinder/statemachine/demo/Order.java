package ch.christophlinder.statemachine.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
	private OrderState state;
	private String customer;
	private final List<OrderLine> orderLines = new ArrayList<>();

	public Order() {
		this.state = OrderState.INIT;
	}

	public Order(OrderState state) {
		this.state = state;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public List<OrderLine> getOrderLines() {
		return Collections.unmodifiableList(orderLines);
	}

	public void addOrderLine(OrderLine orderLine) {
		orderLines.add(orderLine);
	}

}
