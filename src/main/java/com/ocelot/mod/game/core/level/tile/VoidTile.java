package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class VoidTile extends BasicTile {

	public VoidTile() {
		super();
		this.setSolid();
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		gui.drawRect((int) x, (int) y, (int) x + 16, (int) y + 16, 0xff000000);
		GlStateManager.color(1, 1, 1, 1);
	}
}