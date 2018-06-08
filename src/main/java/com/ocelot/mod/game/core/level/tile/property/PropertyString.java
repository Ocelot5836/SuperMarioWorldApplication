package com.ocelot.mod.game.core.level.tile.property;

import com.ocelot.mod.game.Game;

public class PropertyString extends PropertyBase<String> {

	private String name;
	private String defaultValue;

	private PropertyString(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public static PropertyString create(String name, String defaultValue) {
		return new PropertyString(name, defaultValue);
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}