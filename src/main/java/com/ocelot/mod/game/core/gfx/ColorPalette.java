package com.ocelot.mod.game.core.gfx;

import java.awt.image.BufferedImage;

import com.ocelot.mod.lib.Colorizer;

public class ColorPalette {

	private int[] colors;

	public ColorPalette(int color1, int color2, int color3, int color4, int color5, int color6, int color7, int color8) {
		this.colors = new int[] { color1, color2, color3, color4, color5, color6, color7, color8 };
	}

	public int[] getColors() {
		return colors;
	}

	public int getColor1() {
		return colors[0];
	}

	public int getColor2() {
		return colors[1];
	}

	public int getColor3() {
		return colors[2];
	}

	public int getColor4() {
		return colors[3];
	}

	public int getColor5() {
		return colors[4];
	}

	public int getColor6() {
		return colors[5];
	}

	public int getColor7() {
		return colors[6];
	}

	public int getColor8() {
		return colors[7];
	}

	public static BufferedImage addPalette(BufferedImage image, ColorPalette palette) {
		return Colorizer.colorize(image, palette.getColor1(), palette.getColor2(), palette.getColor3(), palette.getColor4(), palette.getColor5(), palette.getColor6(), palette.getColor7(), palette.getColor8());
	}
}