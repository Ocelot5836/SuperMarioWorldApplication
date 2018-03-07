package com.ocelot.mod.game.core.gfx.gui;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Lib;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class MarioGuiYoshiTextBubble extends MarioGui {

	private static final BufferedImage SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/level/yoshi_house/text.png"));
	private static final Sprite FULL = new Sprite(SHEET.getSubimage(96, 142, 160, 79));

	// TODO add animation in

	public MarioGuiYoshiTextBubble() {

	}

	@Override
	public void render(Gui gui, int mouseX, int mouseY, float partialTicks) {
		FULL.render(width / 2, height / 2);
	}
}