package com.ocelot.mod.game.main.gamestate.store;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ShopItem {
	
	protected int price;
	
	public ShopItem(int price) {
		this.price = price;
	}
	
	public void update() {
	}
	
	public void render(double x, double y, double width, double height, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
	}
	
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
	}
	
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
	}
	
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
	}
	
	public int getPrice() {
		return price;
	}
}