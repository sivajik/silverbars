package com.silverbars.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.silverbars.domain.Order;
import com.silverbars.domain.OrderType;

/**
 * Silver Bars Marketplace - Live Order Board
 * 
 * @author sivaji_kondapalli@yahoo.com
 *
 */
public class OrderServiceImpl implements OrderService {
	/**
	 * Key data structure (Map of TreeMaps) to hold given orders in a "search"
	 * effective, "sum" effective way. At a high level {@code liveBoard} is a map of
	 * key values where keys are {@code orderType} i.e. BUY/SELL/SOMETHING ELSE and
	 * values are a {@code TreeMap} instances whose key is a {@code pricePerKilo}
	 * and value is respective list of orders at that price (of same order type).
	 * 
	 * SELL -> 306 -> [order1, order4]
	 * 
	 * -> 307 -> [order3]
	 * 
	 * -> 310 -> [order2]
	 * 
	 * BUY -> 310 -> [order7]
	 * 
	 * -> 250 -> [order5, order6] // Note: 310 SELL order is not same as BUY order
	 */
	private Map<OrderType, TreeMap<Double, List<Order>>> liveBoard;

	/**
	 * Constructor to create brand new in-memory data structure. Adding multi
	 * threading support by using {@code ConcurrentHashMap} as this service to be
	 * used by multiple users/systems to add/cancel orders concurrently. If such
	 * Synchronisation requirement is not there using plain {@code HashMap} is
	 * suggested data structure over {@code ConcurrentHashMap}.
	 */
	public OrderServiceImpl() {
		liveBoard = new ConcurrentHashMap<OrderType, TreeMap<Double, List<Order>>>();
		/*
		 * pre-create 2 TreeMaps for now as problem statement is clear on Order Types
		 * (BUY/SELL). However BUY should show {@code pricePerKilo} - highest prices
		 * first
		 */
		liveBoard.put(OrderType.BUY, new TreeMap<Double, List<Order>>(Collections.reverseOrder()));
		liveBoard.put(OrderType.SELL, new TreeMap<Double, List<Order>>());
	}

	/**
	 * No validation rules provided in problem definition. Hence assumed passed in
	 * order is validated.
	 * 
	 * Approach:
	 * 
	 * First get the respective {@code TreeMap} for a given order type (either SELL
	 * or BUY TreeMap). Once map is given, see if there is already an order with
	 * same {@code pricePerKilo}. If so, append your new order to existing list, or
	 * else create a new list and add to it.
	 * 
	 * Time Complexity:
	 * 
	 * getting the respective {@code TreeMap} for a given order type (either SELL or
	 * BUY TreeMap) is always O(1). Once map is given, see if there is already an
	 * order with same {@code pricePerKilo} in O(log n) as keys are sorted in
	 * TreeMap. If so, append your new order to existing list, or else create a new
	 * list and add to it. In case of addition, (since LinkedLists) can do in O(1).
	 * So overall, whole addOrder can be done in O(log n) time even in worst case
	 * (excluding time to create a new LinkedList).
	 */
	@Override
	public void addOrder(Order order) {
		TreeMap<Double, List<Order>> map = liveBoard.get(order.getOrderType());
		List<Order> listOfOrders = map.get(order.getPricePerKilo());
		// no such order exists previously with same pricePerKilo
		if (listOfOrders == null) {
			/*
			 * as Problem is asking for "cancel" operation, LinkedList is well suited over
			 * ArrayLsit as it makes removal operation slightly better as it involves
			 * finding an element and adjusting the links only. No shuffling and shifting
			 * needed as in ArrayList. Still time complexity is O(n) as searching is not
			 * avoided fully.
			 */
			List<Order> newList = new LinkedList<Order>();
			newList.add(order);
			map.put(order.getPricePerKilo(), newList);
		} else {
			listOfOrders.add(order);
			map.put(order.getPricePerKilo(), listOfOrders);
		}
	}

	/**
	 * Approach:
	 * 
	 * for a given order, find out whether {@code order} is living in SELL map or
	 * BUY map and then get the list of orders where given {@code order} is located.
	 * Once given order is found in the list, delete it. If element is not there,
	 * throw IllegalArgumentException
	 * 
	 * Time Complexity:
	 * 
	 * To find the respective map (based on order's type) we need O(1) time
	 * (currently we have only 2 keys, BUY/SELL). Based on it, query respective
	 * Treemap, to know in which LinkedList given order is residing with in log(n)
	 * time (as keys are ordered in TreeMap, "get" operation would take log(n)). As
	 * there could be more than one order at same price so traverse the LinkedList
	 * linearly and delete the order so O(n) where n is list of orders at a given
	 * price. In case of single key and being cancelled, remove operation would take
	 * O(log n) time So total time complexity is O(1) + O(log n) + O(log n) + O(n)
	 * sums to O(n).
	 */
	@Override
	public void cancelOrder(Order order) {
		TreeMap<Double, List<Order>> map = liveBoard.get(order.getOrderType());
		List<Order> listOfOrders = map.get(order.getPricePerKilo());
		if (listOfOrders != null && listOfOrders.contains(order)) {
			listOfOrders.remove(order);
			/*
			 * Remove the whole element from TreeMap if no other orders with this price
			 */
			if (listOfOrders.size() == 0) {
				map.remove(order.getPricePerKilo());
			}
		} else {
			throw new IllegalArgumentException("Given order does not exists: " + order);
		}
	}

	/**
	 * Instance of Current Live Board
	 * 
	 * @return
	 */
	@Override
	public Map<Double, List<Order>> getOrdersByOrderType(OrderType orderType) {
		return liveBoard.get(orderType);
	}

	/**
	 * Main method where users/systems are interested to print SELL and BUY
	 * information and sum up all orders by their orderQuantity before displaying.
	 * For simplicity returning List<String> but could return List<ResultRow> where
	 * ResultRow would be a POJO with all requires results in it. Normally we would
	 * not need "system.out.print" as a side effect but for demo sake I left as it
	 * is so that command line testing can be done easily. Logger implementation
	 * also (log4j) an option here.
	 * 
	 */
	@Override
	public Map<OrderType, List<String>> getLiveOrderBoard() {
		Map<OrderType, List<String>> summary = new HashMap<>();
		System.out.println("****** LIVE SUMMARY BOARD ******");
		for (OrderType orderType : OrderType.values()) {
			summary.put(orderType, getSummaryByOrderType(orderType));
		}
		System.out.println("****** END ******\n\n");
		return summary;
	}

	/**
	 * Utility to print given OrderType information and sum up all orders by their
	 * orderQuantity before displaying. For simplicity returning List<String> but
	 * could return List<ResultRow> where ResultRow would be a POJO with all
	 * requires results in it.
	 *
	 * @param orderType
	 */
	private List<String> getSummaryByOrderType(OrderType orderType) {
		TreeMap<Double, List<Order>> ordersMap = liveBoard.get(orderType);
		System.out.println("---" + orderType + "---");
		List<String> results = aggregateOrders(ordersMap);
		for (String result : results) {
			System.out.println(result);
		}
		return results;
	}

	/**
	 * Utility method to aggregate orders which are key'ed by {@code pricePerKilo}.
	 * Sum all order's {@code orderQuantity} at same {@code pricePerKilo} using
	 * streams and return resultant string as an outcome.
	 * 
	 * @param map
	 * @return
	 */
	private List<String> aggregateOrders(TreeMap<Double, List<Order>> map) {
		ArrayList<String> results = new ArrayList<String>();
		map.forEach((pricePerKilo, list) -> {
			Double totalKilos = list.parallelStream().mapToDouble(order -> order.getOrderQuantity()).sum();
			results.add(totalKilos + " kg for £" + pricePerKilo);
		});
		return results;
	}
}
