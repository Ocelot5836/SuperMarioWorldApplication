package com.ocelot.mod.game.core;

public enum EnumAxis {
	X, Y;

	public boolean isHorizontalAxis() {
		return this == X;
	}

	public boolean isVerticalAxis() {
		return this == Y;
	}
}