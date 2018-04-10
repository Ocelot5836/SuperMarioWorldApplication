package com.ocelot.mod.game.main.level.tile;

import com.ocelot.mod.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.AnimatedTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.player.Player;

public class TileCoin extends AnimatedTile {

	public TileCoin() {
		super(100, new Sprite(TILES_SHEET.getSubimage(0, 16, 16, 16)), new Sprite(TILES_SHEET.getSubimage(16, 16, 16, 16)), new Sprite(TILES_SHEET.getSubimage(32, 16, 16, 16)), new Sprite(TILES_SHEET.getSubimage(48, 16, 16, 16)));
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void onEntityCollision(int x, int y, Entity entity, EnumDirection hitDirection) {
		if(entity instanceof Player) {
			Player player = (Player) entity;
			player.getProperties().increaseCoins();
			player.getGame().playSound(Sounds.COLLECT_COIN, 1.0F);
			entity.getLevel().getMap().setTile(x, y, Tile.AIR);
		}
	}
}