package com.query.video.youtube.models;

public enum SortingOrder {	
	
	ASCENDING (1),
	DESCENDING (-1);
	
	private int order;
	
	SortingOrder(int order) {
		this.order = order;
	}
	
	public int getOrder() {
		return order;
	}
}
