package com.zhy.eventbus;

import java.util.ArrayList;

public class Item {
	public String id;
	public String content;

	public static ArrayList<Item> ITEMS = new ArrayList<Item>();
	static {
		// Add 6 sample items.
		addItem(new Item("1", "Item 1"));
		addItem(new Item("2", "Item 2"));
		addItem(new Item("3", "Item 3"));
		addItem(new Item("4", "Item 4"));
		addItem(new Item("5", "Item 5"));
		addItem(new Item("6", "Item 6"));
	}

	private static void addItem(Item item) {
		ITEMS.add(item);
	}

	public Item(String id, String content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public String toString() {
		return content;
	}
}