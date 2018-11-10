package com.ocelot.mod.game.core.gfx;

public class ColorPalette {

	public static final ColorPalette TEST = new ColorPalette(0xffff00ff, 0xffffff00, 0xff0000ff, 0xff000000, 0xff00ff00, 0xffffffff, 0xff7f007f, 0xffff7fff, -1, -1, -1, -1);
	public static final ColorPalette COIN_GOLD = new ColorPalette(0xff000000, 0xffd8a038, 0xfff8d820, 0xfff8f800, 0xffe8f0f8, -1, -1, -1, -1, -1, -1, -1);
	public static final ColorPalette COIN_BLUE = new ColorPalette(0xff000000, 0xff484888, 0xff6868b0, 0xff8080c8, 0xffe8f0f8, -1, -1, -1, -1, -1, -1, -1);

	private int[] colors;

	public ColorPalette(int color1, int color2, int color3, int color4, int color5, int color6, int color7, int color8, int color9, int color10, int color11, int color12) {
		this.colors = new int[] { color1, color2, color3, color4, color5, color6, color7, color8, color9, color10, color11, color12 };
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

	public int getColor9() {
		return colors[8];
	}

	public int getColor10() {
		return colors[9];
	}

	public int getColor11() {
		return colors[10];
	}

	public int getColor12() {
		return colors[11];
	}
}