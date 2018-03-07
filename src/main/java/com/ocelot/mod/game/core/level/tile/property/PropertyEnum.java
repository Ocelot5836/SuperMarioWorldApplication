package com.ocelot.mod.game.core.level.tile.property;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ocelot.mod.game.Game;

import net.minecraft.util.IStringSerializable;

public class PropertyEnum<T extends Enum<T> & IStringSerializable> extends PropertyBase<T> {
	
	private String name;
	private final ImmutableSet<T> allowedValues;
	private final Map<String, T> nameToValue = Maps.<String, T>newHashMap();

	protected PropertyEnum(String name, Class<T> valueClass, Collection<T> allowedValues) {
		this.name = name;
		this.allowedValues = ImmutableSet.copyOf(allowedValues);

		for (T t : allowedValues) {
			String s = ((IStringSerializable) t).getName();

			if (this.nameToValue.containsKey(s)) {
				Game.stop(new IllegalArgumentException("Multiple values have the same name '" + s + "'"));
			}

			this.nameToValue.put(s, t);
		}
	}

	public Collection<T> getAllowedValues() {
		return this.allowedValues;
	}

	public Optional<T> parseValue(String value) {
		return Optional.<T>fromNullable(this.nameToValue.get(value));
	}
	
	@Override
	public String getName() {
		return name;
	}

	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else if (p_equals_1_ instanceof PropertyEnum && super.equals(p_equals_1_)) {
			PropertyEnum<?> propertyenum = (PropertyEnum) p_equals_1_;
			return this.allowedValues.equals(propertyenum.allowedValues) && this.nameToValue.equals(propertyenum.nameToValue);
		} else {
			return false;
		}
	}

	public int hashCode() {
		int i = super.hashCode();
		i = 31 * i + this.allowedValues.hashCode();
		i = 31 * i + this.nameToValue.hashCode();
		return i;
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz) {
		return create(name, clazz, Predicates.alwaysTrue());
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz, Predicate<T> filter) {
		return create(name, clazz, Collections2.filter(Lists.newArrayList(clazz.getEnumConstants()), filter));
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz, T... values) {
		return create(name, clazz, Lists.newArrayList(values));
	}

	public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz, Collection<T> values) {
		return new PropertyEnum<T>(name, clazz, values);
	}
}