package com.ocelot.mod.game;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.util.ResourceLocation;

public class Backgrounds {

	public static final BufferedImage SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/backgrounds/normal.png"));
	public static final BufferedImage ANIMATED_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/backgrounds/animated.png"));

	// Normal Backgrounds
	public static final Sprite GREEN_HILLS = new Sprite(SHEET.getSubimage(2, 2, 512, 432));
	public static final Sprite SNOW_HILLS = new Sprite(SHEET.getSubimage(516, 2, 512, 432));
	public static final Sprite JUNGLE_VINES = new Sprite(SHEET.getSubimage(2, 438, 512, 432));
	public static final Sprite MUSHROOM_MOUNTAINS = new Sprite(SHEET.getSubimage(516, 438, 512, 432));
	public static final Sprite GREEN_MOUNTAIN_TOPS = new Sprite(SHEET.getSubimage(2, 872, 512, 432));
	public static final Sprite WHITE_MOUNTAINS = new Sprite(SHEET.getSubimage(516, 872, 512, 432));
	public static final Sprite GREEN_MOUNTAINS = new Sprite(SHEET.getSubimage(516, 1306, 512, 432));
	public static final Sprite CASTLE = new Sprite(SHEET.getSubimage(2, 1740, 512, 432));

	// Animated Backgrounds
	public static final Sprite[] CAVES = new Sprite[] { new Sprite(ANIMATED_SHEET.getSubimage(6, 21, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(521, 21, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(7, 470, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(521, 21, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(6, 21, 512, 432)) };
	public static final Sprite[] ICY_CAVES = new Sprite[] { new Sprite(ANIMATED_SHEET.getSubimage(7, 934, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(521, 934, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(7, 1370, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(521, 934, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(7, 934, 512, 432)) };
	public static final Sprite[] UNDERWATER = new Sprite[] { new Sprite(ANIMATED_SHEET.getSubimage(7, 1831, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(522, 1831, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(7, 2281, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(522, 2281, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(7, 1831, 512, 432)) };
	public static final Sprite[] GHOST_HOUSE = { new Sprite(ANIMATED_SHEET.getSubimage(1056, 24, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1573, 24, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1057, 477, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1573, 24, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1056, 24, 512, 432)) };
	public static final Sprite[] CASTLE_ANIMATED = { new Sprite(ANIMATED_SHEET.getSubimage(1059, 966, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1576, 966, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1060, 1419, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1576, 966, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1059, 966, 512, 432)) };
	public static final Sprite[] STARRY_NIGHT = { new Sprite(ANIMATED_SHEET.getSubimage(1054, 1906, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1572, 1906, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1055, 2363, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1572, 1906, 512, 432)), new Sprite(ANIMATED_SHEET.getSubimage(1054, 1906, 512, 432)) };

}