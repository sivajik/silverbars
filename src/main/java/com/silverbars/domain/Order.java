package com.silverbars.domain;

/**
 * Order must contain these fields:
 * 
 * - user id
 * 
 * - order quantity (e.g.: 3.5 kg)
 * 
 * - price per kg (e.g.: £303)
 * 
 * - order type: BUY or SELL
 *
 */
public class Order {
	// user id
	private String userId;

	// order quantity (e.g.: 3.5 kg)
	private double orderQuantity;

	// price per kg (e.g.: £303)
	private double pricePerKilo;

	// order type: BUY or SELL
	private OrderType orderType;

	/**
	 * public constructoro to make a new Order object.
	 * 
	 * @param userId
	 * @param orderQuantity
	 * @param pricePerKilo
	 * @param orderType
	 */
	public Order(String userId, double orderQuantity, double pricePerKilo, OrderType orderType) {
		super();
		this.userId = userId;
		this.orderQuantity = orderQuantity;
		this.pricePerKilo = pricePerKilo;
		this.orderType = orderType;
	}

	/**
	 * Get method for user id
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set method for user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get order quantity
	 * 
	 * @return
	 */
	public double getOrderQuantity() {
		return orderQuantity;
	}

	/**
	 * Set order quantity
	 * 
	 * @param orderQuantity
	 */
	public void setOrderQuantity(double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	/**
	 * Get price per kilo
	 * 
	 * @return
	 */
	public double getPricePerKilo() {
		return pricePerKilo;
	}

	/**
	 * Set price per kilo
	 * 
	 * @param pricePerKilo
	 */
	public void setPricePerKilo(double pricePerKilo) {
		this.pricePerKilo = pricePerKilo;
	}

	/**
	 * Type of the order, Usually BUY or SELL.
	 * 
	 * @return
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * Set the type of the order, Usually BUY or SELL.
	 * 
	 * @param orderType
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * Default toString implementation with all fields.
	 */
	@Override
	public String toString() {
		return "Order [userId=" + userId + ", orderQuantity=" + orderQuantity + ", pricePerKilo=" + pricePerKilo
				+ ", orderType=" + orderType + "]";
	}

	/**
	 * Default hashcode of the Order
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(orderQuantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		temp = Double.doubleToLongBits(pricePerKilo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	/**
	 * Default equals method of Order
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (Double.doubleToLongBits(orderQuantity) != Double.doubleToLongBits(other.orderQuantity))
			return false;
		if (orderType != other.orderType)
			return false;
		if (Double.doubleToLongBits(pricePerKilo) != Double.doubleToLongBits(other.pricePerKilo))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
