package com.ocelot.mod.game.core.level.tile.property;

import com.ocelot.mod.game.Game;

public class PropertyDouble extends PropertyBase<Double> {

	private String name;
	private double minValue;
	private double maxValue;

	private PropertyDouble(String name, double minValue, double maxValue) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.value = 0.0;
	}

	@Override
	public void setValue(Double value) {
		if (value < minValue || value > maxValue)
			Game.stop(new IllegalArgumentException("Property attempted to set double value out of range. value: " + value + ", range: [" + this.minValue + "-" + this.maxValue + "]"), "A tile attempted to set a double property out of range.");
		super.setValue(value);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public static PropertyDouble create(String name, double minValue, double maxValue) {
		return new PropertyDouble(name, minValue, maxValue);
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}
}