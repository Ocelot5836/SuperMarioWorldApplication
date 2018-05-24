package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.ConnectedTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

public class TileGrassWall extends ConnectedTile {

	public TileGrassWall() {
		super("grass_wall", Tile.CONNECTED_TILES_SHEET.getSubimage(48, 0, 48, 48));
		this.setSolid();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean isTopSolid() {
		return true || (boolean) this.getValue(DOWN);
	}
}