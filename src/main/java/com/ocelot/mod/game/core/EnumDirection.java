package com.ocelot.mod.game.core;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.util.math.MathHelper;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Used for directional mapping.
 * 
 * @author Ocelot5836
 */
public enum EnumDirection {
	UP(0, 1, "up", EnumAxis.Y), DOWN(1, 0, "down", EnumAxis.Y), LEFT(2, 3, "left", EnumAxis.X), RIGHT(3, 2, "right", EnumAxis.X), NONE(4, 4, "none", EnumAxis.NONE);

	private final int index;
	private final int opposite;
	private final String name;
	private final EnumAxis axis;

	private static final Map<String, EnumDirection> NAME_LOOKUP = Maps.<String, EnumDirection>newHashMap();

	private EnumDirection(int index, int opposite, String name, EnumAxis axis) {
		this.index = index;
		this.opposite = opposite;
		this.name = name;
		this.axis = axis;
	}

	public int getIndex() {
		return index;
	}

	public EnumDirection getOpposite() {
		return byId(opposite);
	}

	public boolean isHorizontalAxis() {
		return axis == EnumAxis.X;
	}

	public boolean isVerticalAxis() {
		return axis == EnumAxis.Y;
	}

	public String getName() {
		return name;
	}
	
	public EnumAxis getAxis() {
		return axis;
	}

	@Override
	public String toString() {
		return name;
	}

	@Nullable
	public static EnumDirection byName(String name) {
		return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
	}

	public static EnumDirection byId(int index) {
		return EnumDirection.values()[MathHelper.abs(index % EnumDirection.values().length)];
	}

	static {
		for (EnumDirection direction : values()) {
			NAME_LOOKUP.put(direction.getName().toLowerCase(Locale.ROOT), direction);
		}
	}
}