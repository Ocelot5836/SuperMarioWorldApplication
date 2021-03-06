package com.ocelot.mod.game.main.entity.item;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(ItemPSwitch.Summonable.class)
public class ItemPSwitch extends EntityItem implements IItemCarriable, IDamagable {

	public static final int SWITCH_TIME = 10000;
	public static final int DISPLAY_TIME = 200;

	public static final ResourceLocation SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/item/pswitch.png");
	private static Sprite[] sprites;

	private Sprite sprite;

	private Stopwatch watch;
	private boolean switched;
	private boolean playedStopSound;

	public ItemPSwitch(GameTemplate game) {
		this(game, 0, 0);
	}

	public ItemPSwitch(GameTemplate game, double x, double y) {
		super(game, 0, 0);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;

		this.watch = Stopwatch.createUnstarted();
		this.switched = false;
		this.playedStopSound = false;

		this.sprite = new Sprite();
		if (sprites == null) {
			sprites = new Sprite[2];
			loadSprites();
		}
	}

	private static void loadSprites() {
		sprites[0] = new Sprite(SHEET, 0, 0, 16, 16, 32, 16);
		sprites[1] = new Sprite(SHEET, 16, 0, 16, 16, 32, 16);
	}

	@Override
	public void update() {
		super.update();

		if (this.watch.elapsed(TimeUnit.MILLISECONDS) >= SWITCH_TIME - 1500 && !playedStopSound) {
			game.playSound(Sounds.SWITCH_ENDING, 1.0F);
			playedStopSound = true;
		}

		if (this.watch.elapsed(TimeUnit.MILLISECONDS) >= SWITCH_TIME) {
			this.togglePSwitch();
			this.setDead();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.watch.elapsed(TimeUnit.MILLISECONDS) < DISPLAY_TIME) {
			if (this.watch.elapsed(TimeUnit.MILLISECONDS) > 0) {
				sprite = sprites[1];
			} else {
				sprite = sprites[0];
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
	public boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill) {
		if (!this.switched && source.isHeavy() && sideHit == EnumDirection.UP) {
			game.playSound(Sounds.SWITCH_ACTIVATE, 1.0F);
			this.switched = true;
			this.togglePSwitch();
			this.watch.start();
		}
		return false;
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 2) {
				try {
					KoopaType type = KoopaType.byId(Integer.parseInt(args[0]));
					level.add(new ItemKoopaShell(game, type, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item.summon.numerical", this.getRegistryName()));
				}
			} else if (args.length > 1) {
				try {
					KoopaType type = KoopaType.byId(Integer.parseInt(args[0]));
					level.add(new ItemKoopaShell(game, type));
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item.summon.numerical", this.getRegistryName()));
				}
			} else {
				throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item_koopa_shell.summon.invalid_shell_type"));
			}
		}

		@Override
		public String getRegistryName() {
			return "ItemPSwitch";
		}
	}
}