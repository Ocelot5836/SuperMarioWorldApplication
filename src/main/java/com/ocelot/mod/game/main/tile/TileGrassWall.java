package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.level.tile.BasicConnectedTile;
import com.ocelot.mod.game.core.level.tile.Tile;

public class TileGrassWall extends BasicConnectedTile {

	public TileGrassWall() {
		super("grass_wall", Tile.CONNECTED_TILES_SHEET, 48, 0, 256, 416);
		this.setSolid();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean isTopSolid() {
		return true || (boolean) this.getValue(DOWN);
	}
}