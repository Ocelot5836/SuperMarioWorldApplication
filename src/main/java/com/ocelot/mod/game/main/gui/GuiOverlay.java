package com.ocelot.mod.game.main.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.main.entity.powerup.Powerup;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiOverlay extends MarioGui {

	public static final BufferedImage HUD_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/gui/hud.png"));
	private static List<BufferedImage> sprites;

	public static final int COIN = 0;
	public static final int MARIO = 1;
	public static final int LUIGI = 2;
	public static final int WHITE_0 = 3;
	public static final int WHITE_1 = 4;
	public static final int WHITE_2 = 5;
	public static final int WHITE_3 = 6;
	public static final int WHITE_4 = 7;
	public static final int WHITE_5 = 8;
	public static final int WHITE_6 = 9;
	public static final int WHITE_7 = 10;
	public static final int WHITE_8 = 11;
	public static final int WHITE_9 = 12;
	public static final int YELLOW_0 = 13;
	public static final int YELLOW_1 = 14;
	public static final int YELLOW_2 = 15;
	public static final int YELLOW_3 = 16;
	public static final int YELLOW_4 = 17;
	public static final int YELLOW_5 = 18;
	public static final int YELLOW_6 = 19;
	public static final int YELLOW_7 = 20;
	public static final int YELLOW_8 = 21;
	public static final int YELLOW_9 = 22;
	public static final int WHITE_0_TALL = 23;
	public static final int WHITE_1_TALL = 24;
	public static final int WHITE_2_TALL = 25;
	public static final int WHITE_3_TALL = 26;
	public static final int WHITE_4_TALL = 27;
	public static final int WHITE_5_TALL = 28;
	public static final int WHITE_6_TALL = 29;
	public static final int WHITE_7_TALL = 30;
	public static final int WHITE_8_TALL = 31;
	public static final int WHITE_9_TALL = 32;

	private Sprite hudSprite;
	private Sprite sprite;

	private LevelTemplate level;

	public GuiOverlay(LevelTemplate level) {
		this.level = level;
		this.hudSprite = new Sprite(HUD_SHEET.getSubimage(1, 1, 256, 150));
		this.sprite = new Sprite();

		if (sprites == null) {
			sprites = new ArrayList<BufferedImage>();
			initSprites();
		}
	}

	private void initSprites() {
		sprites.add(HUD_SHEET.getSubimage(258, 1, 8, 8));
		sprites.add(HUD_SHEET.getSubimage(267, 1, 40, 8));
		sprites.add(HUD_SHEET.getSubimage(308, 1, 40, 8));
		for (int y = 0; y < 3; y++) {
			int yOffset = y * 9;
			for (int x = 0; x < 10; x++) {
				int xOffset = x * 9;
				sprites.add(HUD_SHEET.getSubimage(258 + xOffset, 10 + yOffset, 8, y == 2 ? 16 : 8));
			}
		}
	}

	@Override
	public void render(Gui gui, int mouseX, int mouseY, float partialTicks) {
		hudSprite.render(0, 0);
		renderSprite(MARIO, 16, 15);
		renderSprite(COIN, 200, 15);

		renderNumber(WHITE_0, player.getProperties().getLives(), 40, 23);
		renderNumber(YELLOW_0, (int) level.getProperties().getCurrTime(), 168, 23);
		renderNumber(WHITE_0, player.getProperties().getCoins(), 232, 15);
		renderNumber(WHITE_0_TALL, (int) player.getProperties().getBonus(), 104, 14);
		renderNumber(WHITE_0, player.getProperties().getScore(), 232, 23);
		
		int dragonCoins = player.getProperties().getDragonCoins();
		for(int i = 0; i < dragonCoins; i++) {
			renderSprite(COIN, 64 + i * 8, 15);
		}
		
		Powerup powerup = player.getProperties().getReserve();
		if(powerup != Powerup.NULL) {
			powerup.render(120, 15, mc, gui, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
	}

	private void renderNumber(int baseId, int number, double x, double y) {
		String[] digits = Integer.toString(number).split("(?<=.)");
		for (int i = 0; i < digits.length; i++) {
			int index = i - digits.length + 1;
			renderSprite(baseId + Integer.parseInt(digits[i]), x + index * 8, y);
		}
	}

	private void renderSprite(int id, double x, double y) {
		sprite.setData(sprites.get(id % sprites.size()));
		sprite.render(x, y);
		sprite.setData();
	}
}