package com.ocelot.mod.game;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.util.ResourceLocation;

public class Backgrounds {

	public static final BufferedImage SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/backgrounds.png"));

	public static final Sprite GREEN_HILLS = new Sprite(SHEET.getSubimage(2, 2, 512, 432));
	public static final Sprite SNOW_HILLS = new Sprite(SHEET.getSubimage(516, 2, 512, 432));
	public static final Sprite JUNGLE_VINES = new Sprite(SHEET.getSubimage(2, 438, 512, 432));
	public static final Sprite MUSHROOM_MOUNTAINS = new Sprite(SHEET.getSubimage(516, 438, 512, 432));
	public static final Sprite GREEN_MOUNTAIN_TOPS = new Sprite(SHEET.getSubimage(2, 872, 512, 432));
	public static final Sprite WHITE_MOUNTAINS = new Sprite(SHEET.getSubimage(516, 872, 512, 432));
	public static final Sprite GREEN_MOUNTAINS = new Sprite(SHEET.getSubimage(516, 1306, 512, 432));
	public static final Sprite CASTLE = new Sprite(SHEET.getSubimage(2, 1740, 512, 432));

	
}