package com.ocelot.mod.game.main.gamestate.store;

import java.util.List;

import com.google.common.collect.Lists;

public class ShopItemRegistry {

	private static List<ShopItem> items = Lists.newArrayList();
	
	public static void register(ShopItem item) {
		items.add(item);
	}
	
	public static List<ShopItem> getItems() {
		return items;
	}
}