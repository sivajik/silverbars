package com.silverbars.dao;

import java.util.List;
import java.util.Map;

import com.silverbars.domain.Order;
import com.silverbars.domain.OrderType;

/**
 * 
 * Interface to represent all operations on a Order Service.
 *
 */
public interface OrderService {
	/**
	 * Add a new order to the underlying data structure.
	 * 
	 * @param order
	 */
	public void addOrder(Order order);

	/**
	 * Cancel the given order
	 * 
	 * @param order
	 */
	public void cancelOrder(Order order);

	/**
	 * Access to underlying orders by their type.
	 * 
	 * @return
	 */
	public Map<Double, List<Order>> getOrdersByOrderType(OrderType orderType);

	/**
	 * Get the snapshot of the current live order summary board. For each Order Type
	 * a list of responses would be prepared. For simplicity returning List<String>
	 * but could return List<ResultRow> where ResultRow would be a POJO with all
	 * requires results in it.
	 * 
	 * @return
	 */
	public Map<OrderType, List<String>> getLiveOrderBoard();
}