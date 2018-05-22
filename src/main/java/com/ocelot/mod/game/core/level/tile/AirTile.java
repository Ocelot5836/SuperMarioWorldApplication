package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class AirTile extends BasicTile {

	public AirTile() {
		super("air");
		this.setShouldNotRender();
	}
}