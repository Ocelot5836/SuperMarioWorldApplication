package com.ocelot.mod.game.main.entity;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IFileSummonable;
import com.ocelot.mod.game.core.entity.ISpawnerEntity;
import com.ocelot.mod.game.core.entity.Spawner;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import scala.util.Random;

public class Fruit extends Entity {

	private Animation animation;

	public Fruit(GameTemplate game) {
		this(game, 0, 0);
		this.setSize(16, 16);
	}

	public Fruit(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.animation = new Animation();

		Sprite[] sprites = new Sprite[4];
		BufferedImage sheet = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/fruit.png"));
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = new Sprite(sheet.getSubimage(i * 16, 0, 16, 16));
		}
		animation.setFrames(sprites);
		animation.setDelay(100);
	}

	@Override
	public void update() {
		super.update();
		
		animation.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);

		double tileMapX = tileMap.getLastX() + tileMap.getPartialRenderX();
		double tileMapY = tileMap.getLastY() + tileMap.getPartialRenderY();
		animation.getSprite().render(x - tileMapX - cwidth / 2, y - tileMapY - cheight / 2);
	}

	public static class Summonable implements IFileSummonable { 
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 1) {
				try {
					level.add(new Fruit(game, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (Exception e) {
					throwSummonException("Can not summon a Fruit at non-numerical coords!");
				}
			} else {
				level.add(new Fruit(game));
			}
		}
	}

	public static class Spawnable implements ISpawnerEntity {
		private static Random random = new Random();
		
		@Override
		public void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) {
			level.add(new Fruit(game, x + random.nextGaussian(), y + random.nextGaussian()));
		}
	}
}