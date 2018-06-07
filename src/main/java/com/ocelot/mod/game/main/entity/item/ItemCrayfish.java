package com.ocelot.mod.game.main.entity.item;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.config.ModConfig;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.ISpawnerEntity;
import com.ocelot.mod.game.core.entity.Spawner;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.fx.particle.CheeseParticle;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(ItemCrayfish.Summonable.class)
public class ItemCrayfish extends EntityItem implements IItemCarriable {

	public static final BufferedImage SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/item/crayfish.png"));

	private Sprite sprite = new Sprite(SHEET.getSubimage(0, 0, 16, 16));

	public ItemCrayfish(GameTemplate game) {
		this(game, 0, 0);
	}

	public ItemCrayfish(GameTemplate game, double x, double y) {
		super(game, 0.4, 0);
		this.setPosition(x, y);
		this.setSize(sprite.getWidth(), sprite.getHeight());
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2);
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
		level.add(new Spawner(game, x, y, new CheeseParticle.Spawnable(), ModConfig.crayfishParticleSpawnCount, 60));
	}

	@Override
	public void onDrop(Player player) {
		setDefaultPlacing(player);
	}

	@Override
	public boolean canPickup(Player player) {
		return true;
	}

	@Override
	public boolean canHold(Player player) {
		return true;
	}

	public static class Spawnable implements ISpawnerEntity {
		@Override
		public void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) throws SummonException {
			level.add(new ItemCrayfish(game, x, y));
		}
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 1) {
				try {
					ItemCrayfish crayfish = new ItemCrayfish(game, Double.parseDouble(args[0]), Double.parseDouble(args[1]));
					if (args.length > 2) {
						try {
							if (Boolean.parseBoolean(args[2])) {
								crayfish.flipDir();
							}
						} catch (Exception e) {
							Mod.logger().catching(e);
						}
					}
					level.add(crayfish);
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".item.summon.numerical", this.getRegistryName()));
				}
			} else {
				level.add(new ItemCrayfish(game));
			}
		}

		@Override
		public String getRegistryName() {
			return "ItemCrayfish";
		}
	}
}