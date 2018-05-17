package com.ocelot.mod.game.main.level.tile;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.AnimatedTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.item.ItemKoopaShell;
import com.ocelot.mod.game.main.entity.player.Player;

public class TileCoin extends AnimatedTile {

	private static final int[] NORMAL_COLORS = { 0xff000000, 0xffd8a038, 0xfff8d820, 0xfff8f800, 0xffe8f0f8, -1, -1, -1 };
	private static final int[] BLUE_COLORS = { 0xff000000, 0xff484888, 0xff6868b0, 0xff8080c8, 0xffe8f0f8, -1, -1, -1 };

	public TileCoin(CoinType type) {
		super(100, generateSprite(type, 0), generateSprite(type, 1), generateSprite(type, 2), generateSprite(type, 3));
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
		// if (type == CoinType.NORMAL) {
		// return new Sprite(Colorizer.colorize(Tile.TILES_SHEET.getSubimage(animationFrame * 16, 16, 16, 16), NORMAL_COLORS[0], NORMAL_COLORS[1], NORMAL_COLORS[2], NORMAL_COLORS[3], NORMAL_COLORS[4], NORMAL_COLORS[5], NORMAL_COLORS[6], NORMAL_COLORS[7]));
		// } else {
		// return new Sprite(Colorizer.colorize(Tile.TILES_SHEET.getSubimage(animationFrame * 16, 16, 16, 16), BLUE_COLORS[0], BLUE_COLORS[1], BLUE_COLORS[2], BLUE_COLORS[3], BLUE_COLORS[4], BLUE_COLORS[5], BLUE_COLORS[6], BLUE_COLORS[7]));
		// }
		return new Sprite(Tile.TILES_SHEET.getSubimage(animationFrame * 16, 16 + (type == CoinType.BLUE ? 16 : 0), 16, 16));
	}

	public enum CoinType {
		NORMAL, BLUE
	}
}