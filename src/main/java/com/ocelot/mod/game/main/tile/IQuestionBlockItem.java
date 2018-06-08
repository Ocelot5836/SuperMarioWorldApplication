package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public interface IQuestionBlockItem {

	void update();

	void render(Minecraft mc, Gui gui, double x, double y, int mouseX, int mouseY, float partialTicks);
	
	Entity createInstance(GameTemplate game, double x, double y);

	String getName();

}