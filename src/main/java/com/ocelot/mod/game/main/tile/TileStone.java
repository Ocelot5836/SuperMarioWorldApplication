package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.BasicTile;

import net.minecraft.item.ItemStack;

public class TileStone extends BasicTile {

	public TileStone() {
		super(new Sprite(TILES_SHEET.getSubimage(0, 144, 16, 16)), "stone");
	}
}