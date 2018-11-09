package com.ocelot.mod.game;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.util.ResourceLocation;

public class Backgrounds {

	public static final ResourceLocation SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/backgrounds/normal.png");
	public static final ResourceLocation ANIMATED_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/backgrounds/animated.png");

	// Normal Backgrounds
	public static final Sprite GREEN_HILLS = new Sprite(SHEET, 2, 2, 512, 432, 1032, 2174);
	public static final Sprite SNOW_HILLS = new Sprite(SHEET, 516, 2, 512, 432, 1032, 2174);
	public static final Sprite JUNGLE_VINES = new Sprite(SHEET, 2, 438, 512, 432, 1032, 2174);
	public static final Sprite MUSHROOM_MOUNTAINS = new Sprite(SHEET, 516, 438, 512, 432, 1032, 2174);
	public static final Sprite GREEN_MOUNTAIN_TOPS = new Sprite(SHEET, 2, 872, 512, 432, 1032, 2174);
	public static final Sprite WHITE_MOUNTAINS = new Sprite(SHEET, 516, 872, 512, 432, 1032, 2174);
	public static final Sprite GREEN_MOUNTAINS = new Sprite(SHEET, 516, 1306, 512, 432, 1032, 2174);
	public static final Sprite CASTLE = new Sprite(SHEET, 2, 1740, 512, 432, 1032, 2174);

	// Animated Backgrounds
	public static final Sprite[] CAVES = new Sprite[] { new Sprite(ANIMATED_SHEET, 6, 21, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 21, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 470, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 21, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 6, 21, 512, 432, 2090, 2814) };
	public static final Sprite[] ICY_CAVES = new Sprite[] { new Sprite(ANIMATED_SHEET, 7, 934, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 934, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 1370, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 934, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 934, 512, 432, 2090, 2814) };
	public static final Sprite[] UNDERWATER = new Sprite[] { new Sprite(ANIMATED_SHEET, 7, 1831, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 526, 1831, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 2281, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 526, 2281, 512, 432, 2090, 2814) };
	public static final Sprite[] GHOST_HOUSE = { new Sprite(ANIMATED_SHEET, 1056, 24, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1573, 24, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1057, 477, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1573, 24, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1056, 24, 512, 432, 2090, 2814) };
	public static final Sprite[] CASTLE_ANIMATED = { new Sprite(ANIMATED_SHEET, 1059, 966, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1576, 966, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1060, 1419, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1576, 966, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1059, 966, 512, 432, 2090, 2814) };
	public static final Sprite[] STARRY_NIGHT = { new Sprite(ANIMATED_SHEET, 1054, 1906, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1572, 1906, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1055, 2363, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1572, 1906, 512, 432, 2090, 2814) };

}