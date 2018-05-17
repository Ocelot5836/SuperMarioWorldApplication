package com.ocelot.mod.game.main.level.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.AnimatedTile;
import com.ocelot.mod.game.core.level.tile.Tile;

public class QuestionBlockTile extends AnimatedTile {

	public QuestionBlockTile() {
		super(100, new Sprite(Tile.TILES_SHEET.getSubimage(0, 80, 16, 16)), new Sprite(Tile.TILES_SHEET.getSubimage(16, 80, 16, 16)), new Sprite(Tile.TILES_SHEET.getSubimage(32, 80, 16, 16)), new Sprite(Tile.TILES_SHEET.getSubimage(48, 80, 16, 16)));
		this.setSolid();
	}
}