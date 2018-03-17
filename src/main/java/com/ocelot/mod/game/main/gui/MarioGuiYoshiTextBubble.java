package com.ocelot.mod.game.main.gui;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Lib;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class MarioGuiYoshiTextBubble extends MarioGui {

	private static final BufferedImage SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/level/yoshihouse/text.png"));
	private static final Sprite[] FRAMES;
	private static final Sprite FULL = new Sprite(SHEET.getSubimage(96, 142, 160, 79));

	private Animation animation;

	static {
		FRAMES = new Sprite[8];
		FRAMES[0] = new Sprite(SHEET.getSubimage(71, 35, 25, 11));
		FRAMES[1] = new Sprite(SHEET.getSubimage(63, 51, 33, 15));
		FRAMES[2] = new Sprite(SHEET.getSubimage(47, 71, 49, 23));
		FRAMES[3] = new Sprite(SHEET.getSubimage(39, 100, 57, 27));
		FRAMES[4] = new Sprite(SHEET.getSubimage(23, 133, 73, 35));
		FRAMES[5] = new Sprite(SHEET.getSubimage(0, 174, 96, 47));
		FRAMES[6] = new Sprite(SHEET.getSubimage(144, 0, 112, 55));
		FRAMES[7] = new Sprite(SHEET.getSubimage(105, 61, 151, 75));
	}

	public MarioGuiYoshiTextBubble() {
		animation = new Animation();
		animation.setFrames(FRAMES);
		animation.setDelay(0);
	}

	@Override
	public void update() {
		if (!animation.hasPlayedOnce()) {
			animation.update();
		}
	}

	@Override
	public void render(Gui gui, int mouseX, int mouseY, float partialTicks) {
		if (animation.hasPlayedOnce()) {
			FULL.render(width / 2 - FULL.getWidth() / 2, height / 2 - FULL.getHeight() / 2);
		} else {
			Sprite sprite = animation.getSprite();
			sprite.render(width / 2 - sprite.getWidth() / 2, height / 2 - sprite.getHeight() / 2);
		}
	}
}