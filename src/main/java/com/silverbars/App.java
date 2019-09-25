package com.silverbars;

import com.silverbars.dao.OrderService;
import com.silverbars.dao.OrderServiceImpl;
import com.silverbars.domain.Order;
import com.silverbars.domain.OrderType;

/**
 * Main driver class.
 */
public class App {

	public static void main(String[] args) {
		OrderService orderSrvc = new OrderServiceImpl();

		Order order1 = new Order("user1", 3.5, 306, OrderType.SELL);
		Order order2 = new Order("user2", 1.2, 310, OrderType.SELL);
		Order order3 = new Order("user3", 1.5, 307, OrderType.SELL);
		Order order4 = new Order("user4", 2.0, 306, OrderType.SELL);

		Order order5 = new Order("user5", 2.5, 250, OrderType.BUY);
		Order order6 = new Order("user6", 3.0, 250, OrderType.BUY);
		Order order7 = new Order("user7", 3.0, 310, OrderType.BUY);

		orderSrvc.addOrder(order1);
		orderSrvc.addOrder(order2);
		orderSrvc.addOrder(order3);
		orderSrvc.addOrder(order4);
		orderSrvc.addOrder(order5);
		orderSrvc.addOrder(order6);
		orderSrvc.addOrder(order7);
		orderSrvc.getLiveOrderBoard();

		System.out.println("Cancelling :" + order4);
		orderSrvc.cancelOrder(order4);
		orderSrvc.getLiveOrderBoard();

		System.out.println("Cancelling :" + order7);
		orderSrvc.cancelOrder(order7);
		orderSrvc.getLiveOrderBoard();

		System.out.println("Cancelling :" + order6);
		orderSrvc.cancelOrder(order6);
		orderSrvc.getLiveOrderBoard();
	}
}