package com.ocelot.mod.game.entity.misc;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Lib;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

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
		animation.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		animation.getSprite().render(x - tileMap.getX() * 2 - cwidth / 2, y - tileMap.getY() * 2 - cheight / 2);
	}
}