package com.ocelot.mod.game.main.entity.item;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ItemPSwitch extends EntityItem implements IItemCarriable, IPlayerDamagable {

	private Stopwatch watch;
	private boolean switched;

	public ItemPSwitch(GameTemplate game, double xSpeed, double ySpeed, double fallSpeed) {
		super(game, xSpeed, ySpeed, fallSpeed);
		this.watch = Stopwatch.createUnstarted();
		this.switched = false;
	}

	@Override
	public void update() {
		super.update();

		if (this.watch.elapsed(TimeUnit.SECONDS) >= 20) {
			this.togglePSwitch();
			setDead();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.watch.elapsed(TimeUnit.SECONDS) < 1) {

		}
	}

	private void togglePSwitch() {
		TileMap map = this.getLevel().getMap();
		for (int y = 0; y < map.getHeight() / tileSize; y++) {
			for (int x = 0; x < map.getWidth() / tileSize; x++) {
				if (map.getTile(x, y).equals(Tile.COIN)) {
					map.setTile(x, y, Tile.QUESTION_BLOCK);
					continue;
				}

				if (map.getTile(x, y).equals(Tile.QUESTION_BLOCK)) {
					map.setTile(x, y, Tile.COIN);
					continue;
				}
			}
		}
	}

	@Override
	public void onHeldUpdate(Player player) {
	}

	@Override
	public void onPickup(Player player) {
	}

	@Override
	public void onThrow(Player player, ThrowingType type) {
		setDefaultThrowing(player, type);
	}

	@Override
	public void onDrop(Player player) {
		setDefaultPlacing(player);
	}

	@Override
	public boolean canPickup(Player player) {
		return !switched;
	}

	@Override
	public void damageEnemy(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible) {
		if (!this.switched && sideHit == EnumDirection.UP) {
			this.switched = true;
			this.togglePSwitch();
			this.watch.start();
		}
	}
}