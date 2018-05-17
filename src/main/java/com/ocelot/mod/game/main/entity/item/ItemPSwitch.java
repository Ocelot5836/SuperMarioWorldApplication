package com.ocelot.mod.game.main.entity.item;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class ItemPSwitch extends EntityItem implements IItemCarriable, IPlayerDamagable {

	public static final BufferedImage SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/pswitch.png"));
	private static BufferedImage[] sprites;

	private Sprite sprite;

	private Stopwatch watch;
	private boolean switched;

	public ItemPSwitch(GameTemplate game) {
		this(game, 0, 0);
	}

	public ItemPSwitch(GameTemplate game, double x, double y) {
		super(game, 0, 0, 0.015);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;

		this.watch = Stopwatch.createUnstarted();
		this.switched = false;

		this.sprite = new Sprite();
		if (sprites == null) {
			this.loadSprites();
		}
	}

	private void loadSprites() {
		sprites = new BufferedImage[2];
		sprites[0] = SHEET.getSubimage(0, 0, 16, 16);
		sprites[1] = SHEET.getSubimage(16, 0, 16, 16);
	}

	@Override
	public void update() {
		super.update();

		if (this.watch.elapsed(TimeUnit.MILLISECONDS) == 18000) {
			game.playSound(Sounds.SWITCH_ENDING, 1.0F);
			if (this.watch.elapsed(TimeUnit.SECONDS) >= 19) {
				this.togglePSwitch();
				setDead();
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.watch.elapsed(TimeUnit.MILLISECONDS) < 600) {
			if (this.watch.elapsed(TimeUnit.MILLISECONDS) > 0) {
				sprite.setData(sprites[1]);
			} else {
				sprite.setData(sprites[0]);
			}
			double posX = lastX + this.getPartialRenderX();
			double posY = lastY + this.getPartialRenderY();
			sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2);
		}
	}

	private void togglePSwitch() {
		TileMap map = this.getLevel().getMap();
		for (int y = 0; y < map.getHeight() / tileSize; y++) {
			for (int x = 0; x < map.getWidth() / tileSize; x++) {
				if (map.getTile(x, y).equals(Tile.COIN)) {
					map.setTile(x, y, Tile.BRICKS);
					continue;
				}

				if (map.getTile(x, y).equals(Tile.BRICKS)) {
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
	public boolean canHold(Player player) {
		return this.watch.elapsed(TimeUnit.MILLISECONDS) < 600;
	}

	@Override
	public void damageEnemy(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible) {
		if (!this.switched && sideHit == EnumDirection.UP) {
			game.playSound(Sounds.SWITCH_ACTIVATE, 1.0F);
			this.switched = true;
			this.togglePSwitch();
			this.watch.start();
		}
	}
}