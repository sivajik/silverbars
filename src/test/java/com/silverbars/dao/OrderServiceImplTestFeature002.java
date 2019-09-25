package com.silverbars.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.silverbars.domain.Order;
import com.silverbars.domain.OrderType;

public class OrderServiceImplTestFeature002 {

	OrderService orderSrvc = null;

	Order order1 = new Order("user1", 3.5, 306, OrderType.SELL);
	Order order2 = new Order("user2", 1.2, 310, OrderType.SELL);
	Order order3 = new Order("user3", 1.5, 307, OrderType.SELL);
	Order order4 = new Order("user4", 2.0, 306, OrderType.SELL);

	Order order5 = new Order("user5", 2.5, 250, OrderType.BUY);
	Order order6 = new Order("user6", 3.0, 250, OrderType.BUY);
	Order order7 = new Order("user7", 3.0, 310, OrderType.BUY);

	@Before
	public void prepareOrderService() {
		orderSrvc = new OrderServiceImpl();
		orderSrvc.addOrder(order1);
		orderSrvc.addOrder(order2);
		orderSrvc.addOrder(order3);
		orderSrvc.addOrder(order4);
		orderSrvc.addOrder(order5);
		orderSrvc.addOrder(order6);
		orderSrvc.addOrder(order7);
	}

	@Test
	public void testCancelOrderSell() {
		orderSrvc.cancelOrder(order4);

		Map<OrderType, List<String>> resultsMap = orderSrvc.getLiveOrderBoard();
		assertEquals(3, resultsMap.get(OrderType.SELL).size());

		List<String> results = resultsMap.get(OrderType.SELL);
		assertTrue(results.get(0).equalsIgnoreCase("3.5 kg for £306.0"));
		assertTrue(results.get(1).equalsIgnoreCase("1.5 kg for £307.0"));
		assertTrue(results.get(2).equalsIgnoreCase("1.2 kg for £310.0"));
	}

	@Test
	public void testCancelOrderBuy() {
		orderSrvc.cancelOrder(order7);

		Map<OrderType, List<String>> resultsMap = orderSrvc.getLiveOrderBoard();
		assertEquals(1, resultsMap.get(OrderType.BUY).size());

		List<String> results = resultsMap.get(OrderType.BUY);
		assertTrue(results.get(0).equalsIgnoreCase("5.5 kg for £250.0"));
	}

	@Test
	public void testCancelOrderBuyNonEmptyList() {
		orderSrvc.cancelOrder(order5);

		Map<OrderType, List<String>> resultsMap = orderSrvc.getLiveOrderBoard();
		assertEquals(2, resultsMap.get(OrderType.BUY).size());

		List<String> results = resultsMap.get(OrderType.BUY);
		assertTrue(results.get(0).equalsIgnoreCase("3.0 kg for £310.0"));
		assertTrue(results.get(1).equalsIgnoreCase("3.0 kg for £250.0"));
	}

	@Test
	public void testCancelOrderBuyEmptyList() {
		orderSrvc.cancelOrder(order5);
		orderSrvc.cancelOrder(order6);
		orderSrvc.cancelOrder(order7);

		Map<OrderType, List<String>> resultsMap = orderSrvc.getLiveOrderBoard();
		assertEquals(0, resultsMap.get(OrderType.BUY).size());

		List<String> results = resultsMap.get(OrderType.BUY);
		assertTrue(results.size() == 0);
	}
}