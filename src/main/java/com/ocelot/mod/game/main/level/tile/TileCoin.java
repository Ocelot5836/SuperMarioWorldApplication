package com.ocelot.mod.game.main.level.tile;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.ColorPalette;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.AnimatedTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.item.ItemKoopaShell;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Colorizer;

public class TileCoin extends AnimatedTile {

	public TileCoin(CoinType type) {
		super("coin", 100, generateSprite(type, 0), generateSprite(type, 1), generateSprite(type, 2), generateSprite(type, 3));
	}

	@Override
	public void onEntityCollision(int x, int y, Entity entity, EnumDirection hitDirection) {
		if (entity instanceof ItemKoopaShell) {
			ItemKoopaShell shell = (ItemKoopaShell) entity;
			if (shell.getThrowingPlayer() != null) {
				Player player = shell.getThrowingPlayer();
				player.getProperties().increaseCoins();
				player.getGame().playSound(Sounds.COLLECT_COIN, 1.0F);
				entity.getLevel().getMap().setTile(x, y, Tile.AIR);
			}
		}
		if (entity instanceof Player) {
			Player player = (Player) entity;
			player.getProperties().increaseCoins();
			player.getGame().playSound(Sounds.COLLECT_COIN, 1.0F);
			entity.getLevel().getMap().setTile(x, y, Tile.AIR);
		}
	}

	private static Sprite generateSprite(CoinType type, int animationFrame) {
		if (type == CoinType.NORMAL) {
			return new Sprite(Colorizer.colorize(Tile.TILES_SHEET.getSubimage(animationFrame * 16, 16, 16, 16), ColorPalette.COIN_GOLD));
		} else {
			return new Sprite(Colorizer.colorize(Tile.TILES_SHEET.getSubimage(animationFrame * 16, 16, 16, 16), ColorPalette.COIN_BLUE));
		}
	}

	public enum CoinType {
		NORMAL, BLUE
	}
}