package com.ocelot.mod.game;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.util.ResourceLocation;

public class Backgrounds {

	public static final ResourceLocation SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/backgrounds/normal.png");
	public static final ResourceLocation ANIMATED_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/backgrounds/animated.png");

	private static final Map<String, Sprite[]> BACKGROUNDS = Maps.<String, Sprite[]>newHashMap();

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
	public static final Sprite[] CAVES = Lib.asArray(new Sprite(ANIMATED_SHEET, 6, 21, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 21, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 470, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 21, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 6, 21, 512, 432, 2090, 2814));
	public static final Sprite[] ICY_CAVES = Lib.asArray(new Sprite(ANIMATED_SHEET, 7, 934, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 934, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 1370, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 521, 934, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 934, 512, 432, 2090, 2814));
	public static final Sprite[] UNDERWATER = Lib.asArray(new Sprite(ANIMATED_SHEET, 7, 1831, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 526, 1831, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 7, 2281, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 526, 2281, 512, 432, 2090, 2814));
	public static final Sprite[] GHOST_HOUSE = Lib.asArray(new Sprite(ANIMATED_SHEET, 1056, 24, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1573, 24, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1057, 477, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1573, 24, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1056, 24, 512, 432, 2090, 2814));
	public static final Sprite[] CASTLE_ANIMATED = Lib.asArray(new Sprite(ANIMATED_SHEET, 1059, 966, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1576, 966, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1060, 1419, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1576, 966, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1059, 966, 512, 432, 2090, 2814));
	public static final Sprite[] STARRY_NIGHT = Lib.asArray(new Sprite(ANIMATED_SHEET, 1054, 1906, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1572, 1906, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1055, 2363, 512, 432, 2090, 2814), new Sprite(ANIMATED_SHEET, 1572, 1906, 512, 432, 2090, 2814));

	public static void register() {
		register("GREEN_HILLS", GREEN_HILLS);
		register("SNOW_HILLS", SNOW_HILLS);
		register("JUNGLE_VINES", JUNGLE_VINES);
		register("MUSHROOM_MOUNTAINS", MUSHROOM_MOUNTAINS);
		register("GREEN_MOUNTAIN_TOPS", GREEN_MOUNTAIN_TOPS);
		register("WHITE_MOUNTAINS", WHITE_MOUNTAINS);
		register("GREEN_MOUNTAINS", GREEN_MOUNTAINS);
		register("CASTLE", CASTLE);

		register("CAVES", CAVES);
		register("ICY_CAVES", ICY_CAVES);
		register("UNDERWATER", UNDERWATER);
		register("GHOST_HOUSE", GHOST_HOUSE);
		register("CASTLE_ANIMATED", CASTLE_ANIMATED);
		register("STARRY_NIGHT", STARRY_NIGHT);
	}

	public static void register(String registryName, Sprite... sprites) {
		if (!BACKGROUNDS.containsKey(registryName)) {
			BACKGROUNDS.put(registryName, sprites);
		} else {
			throw new RuntimeException("Background \'" + registryName + "\' attempted to override another with the id of \'" + registryName + "\'");
		}
	}

	public static Set<Entry<String, Sprite[]>> getBackgrounds() {
		return BACKGROUNDS.entrySet();
	}
}