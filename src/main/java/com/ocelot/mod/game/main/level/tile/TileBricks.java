package com.ocelot.mod.game.main.level.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.BasicTile;

public class TileBricks extends BasicTile {

	public TileBricks() {
		super(new Sprite(TILES_SHEET.getSubimage(0, 96, 16, 16)), "bricks");
		this.setSolid();
	}
}