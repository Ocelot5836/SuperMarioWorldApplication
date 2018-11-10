package com.ocelot.mod.game.main.gui;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class MarioGuiPlayerHello extends MarioGui {

	private static final ResourceLocation SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/gui/text.png");
	private static final Sprite[] FRAMES;
	private static final Sprite FULL = new Sprite(SHEET, 96, 142, 160, 79, 256, 256);

	private Animation<Sprite> animation;

	static {
		FRAMES = new Sprite[8];
		FRAMES[0] = new Sprite(SHEET,71, 35, 25, 11,256,256);
		FRAMES[1] = new Sprite(SHEET,63, 51, 33, 15,256,256);
		FRAMES[2] = new Sprite(SHEET,47, 71, 49, 23,256,256);
		FRAMES[3] = new Sprite(SHEET,39, 100, 57, 27,256,256);
		FRAMES[4] = new Sprite(SHEET,23, 133, 73, 35,256,256);
		FRAMES[5] = new Sprite(SHEET,0, 174, 96, 47,256,256);
		FRAMES[6] = new Sprite(SHEET,144, 0, 112, 55,256,256);
		FRAMES[7] = new Sprite(SHEET,105, 61, 151, 75,256,256);
	}

	public MarioGuiPlayerHello() {
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
		String message = I18n.format("gui.osmw.hello", Minecraft.getMinecraft().player.getName());
		if (animation.hasPlayedOnce()) {
			FULL.render(width / 2 - FULL.getWidth() / 2, height / 2 - FULL.getHeight() / 2);
			mc.fontRenderer.drawSplitString(message, width / 2 - FULL.getWidth() / 2 + 5, height / 2 - FULL.getHeight() / 2 + 5, 150, 0xffffff);
		} else {
			animation.get().render(width / 2 - animation.get().getWidth() / 2, height / 2 - animation.get().getHeight() / 2);
		}
	}
}