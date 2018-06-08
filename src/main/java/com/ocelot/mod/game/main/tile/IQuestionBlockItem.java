package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public interface IQuestionBlockItem {

	void update();

	void render(double x, double y, Minecraft mc, Gui gui, int mouseX, int mouseY, float partialTicks);
	
	Entity createInstance(GameTemplate game, double x, double y);

	String getName();

}