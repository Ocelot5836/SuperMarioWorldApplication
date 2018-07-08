package com.ocelot.mod.game.main.gamestate.worldmap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public interface IWorldMapPoint {

	void update();
	
	void render(double width, double height, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);
	
	double getX();
	
	double getY();
}