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
	UP(0, 1, "up"), DOWN(1, 0, "down"), LEFT(2, 3, "left"), RIGHT(3, 2, "right");

	private final int index;
	private final int opposite;
	private final String name;

	private static final Map<String, EnumDirection> NAME_LOOKUP = Maps.<String, EnumDirection>newHashMap();

	private EnumDirection(int index, int opposite, String name) {
		this.index = index;
		this.opposite = opposite;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public EnumDirection getOpposite() {
		return byId(opposite);
	}
	
	public boolean isHorizontalAxis() {
		return this == LEFT || this == RIGHT;
	}
	
	public boolean isVerticalAxis() {
		return this == UP || this == DOWN;
	}

	public String getName() {
		return name;
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