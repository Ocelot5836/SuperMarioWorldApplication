package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.BasicTile;

public class TileBricks extends BasicTile {

	public TileBricks() {
		super(new Sprite(TILES_SHEET,0, 96, 16, 16, 256, 416), "bricks");
		this.setSolid();
	}
}