package com.ocelot.mod.game.main.level.tile;

import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.ConnectedTile;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

public class GrassWallTile extends ConnectedTile {

	public GrassWallTile() {
		super(CONNECTED_TILES_SHEET.getSubimage(48, 0, 48, 48));
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		super.modifyContainer(x, y, tileMap, container);

		boolean up = (boolean) container.getValue(UP);
		boolean down = (boolean) container.getValue(DOWN);

		this.setPassable();
		if (up)
			this.setTopSolid();
		if (!down)
			this.setBottomSolid();

		return container;
	}
}