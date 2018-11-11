package com.ocelot.mod.game.main.gamestate.store;

import java.util.List;

import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

@DebugSelectStateLevel
public class ShopState extends GameState {

	private List<ShopItem> items;

	private double scroll;
	private double lastScroll;

	private double gridX;
	private double gridY;
	private double padding;
	private double cellWidth;
	private double cellHeight;
	private int width;

	public ShopState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void load() {
		this.items = ShopItemRegistry.getItems();
		this.gridX = 0;
		this.gridY = 30;
		this.padding = 2;
		this.cellWidth = 60;
		this.cellHeight = 70;
		this.width = 4;
	}

	@Override
	public void update() {
		this.lastScroll = this.scroll;

		for (int i = 0; i < items.size(); i++) {
			items.get(i).update();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, lastScroll + (scroll - lastScroll) * partialTicks, 0);
		for (int i = 0; i < items.size(); i++) {
			int x = i % width;
			int y = i / width;
			double renderX = x + x * padding + x * cellWidth + gridX;
			double renderY = y + y * padding + gridY;
			items.get(i).render(this.getGame().getWidth() / 2 - (width * cellWidth + width * padding) / 2 + renderX, renderY, cellWidth, cellHeight, gui, mc, mouseX, mouseY, partialTicks);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).onMousePressed(mouseButton, mouseX, mouseY);
		}
	}

	@Override
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).onMouseReleased(mouseButton, mouseX, mouseY);
		}
	}

	@Override
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).onMouseScrolled(direction, mouseX, mouseY);
		}

		if (direction) {
			scrollUp(5);
		} else {
			scrollDown(5);
		}
	}

	private void scrollUp(double amount) {
		scroll += amount;
		if (scroll > 0)
			scroll = 0;
	}

	private void scrollDown(double amount) {
		scroll -= amount;
		if (scroll < -(cellHeight * (items.size() / width - 1)))
			scroll = -(cellHeight * (items.size() / width - 1));
	}
}