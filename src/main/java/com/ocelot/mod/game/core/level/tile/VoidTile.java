package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class VoidTile extends Tile {

	public VoidTile() {
		this.setSolid();
	}

	@Override
	public void update() {
	}

	@Override
	public void render(int x, int y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		gui.drawRect(x, y, x + 16, y + 16, 0xff000000);
		GlStateManager.color(1, 1, 1, 1);
	}
}