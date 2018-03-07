package com.ocelot.mod.game.core.level.tile.property;

import java.util.Map;

import com.google.common.collect.Maps;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.level.tile.Tile;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The basic handler for properties.
 * 
 * @author Ocelot5836
 */
public class TileStateContainer {

	private Tile tile;
	private Map<String, IProperty> properties = Maps.<String, IProperty>newHashMap();

	public TileStateContainer(Tile tile, IProperty[] properties) {
		this.tile = tile;
		for (int i = 0; i < properties.length; i++) {
			this.properties.put(properties[i].getName(), properties[i]);
		}
	}

	public Tile getTile() {
		return tile;
	}

	public Object getValue(IProperty property) {
		if (!properties.containsKey(property.getName()))
			Game.stop(new IllegalArgumentException("Property " + property.getName() + " attempted to be accessed even though it does not exist."), "You cannot get the value of a property that does not exist!");
		return properties.get(property.getName()).getValue();
	}

	public void setValue(IProperty property, Object value) {
		if (!properties.containsKey(property.getName()))
			Game.stop(new IllegalArgumentException("Property " + property.getName() + " attempted to be set even though it does not exist."), "You cannot set the value of a property that does not exist!");
		properties.get(property.getName()).setValue(value);
	}
}