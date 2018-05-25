package com.ocelot.mod.game.main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.game.core.gfx.FontRenderer;
import com.ocelot.mod.lib.Lib;

import net.minecraft.util.ResourceLocation;

public class MarioFontRenderer implements FontRenderer {

	private BufferedImage fontSheet;
	private String chars;
	private List<int[]> fontFile;

	public MarioFontRenderer(ResourceLocation fontSheet, ResourceLocation charFileLocation, ResourceLocation fontFileLocation) {
		this.fontSheet = Lib.loadImage(fontSheet);
		this.chars = Lib.loadTextToString(charFileLocation);
		this.fontFile = new ArrayList<int[]>();
		this.loadFontFile(fontFileLocation);
	}

	private void loadFontFile(ResourceLocation fontFileLocation) {
		String fileInfo = Lib.loadTextToString(fontFileLocation);
		String[] lines = fileInfo.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String[] data = line.split(";");
			if (data.length != 4)
				throw new IllegalArgumentException("Illegal arguments on line " + i + " in \'" + fontFileLocation + "\'");
			int[] intData = new int[4];
			for (int j = 0; j < intData.length; j++) {
				intData[j] = Integer.parseInt(data[j]);
			}
			fontFile.add(intData);
		}
	}

	@Override
	public void renderString(String text, double x, double y, boolean dropShadow) {

	}

	@Override
	public void renderCenteredString(String text, double x, double y, boolean dropShadow) {
		this.renderString(text, x - this.getStringWidth(text) / 2, y, dropShadow);
	}

	@Override
	public int getStringWidth(String text) {
		return 0;
	}
}