package com.ocelot.mod.game.core.level.tile.property;

import com.ocelot.mod.game.Game;

public class PropertyBoolean extends PropertyBase<Boolean> {

	private String name;

	private PropertyBoolean(String name) {
		this.name = name;
		this.value = false;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public static PropertyBoolean create(String name) {
		return new PropertyBoolean(name);
	}
}