package com.ocelot.mod.game.core.level.tile;

public class AirTile extends BasicTile {

	public AirTile() {
		super("air");
		this.setShouldNotRender();
	}
}