package com.ocelot.mod.game;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Game extends GameTemplate {

	public static final int WIDTH = 256;
	public static final int HEIGHT = 150;

	public MarioGui currentDisplayedGui;

	private GameStateManager gsm;

	public Game() {
		super(WIDTH, HEIGHT);
	}

	@Override
	public void init() {
		gsm = new GameStateManager(this);
		currentDisplayedGui = null;
	}

	@Override
	public void update() {
		if (!closed) {
			gsm.update();
			if (currentDisplayedGui != null)
				currentDisplayedGui.update();
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (!closed) {
			gsm.render(gui, mc, mouseX, mouseY, partialTicks);
			if (currentDisplayedGui != null)
				currentDisplayedGui.render(gui, mouseX, mouseY, partialTicks);
			if (this.isDebug()) {
				gui.drawString(mc.fontRenderer, "Current: " + gsm.getSelectedState().toString(), 2, 2, 0xffffffff);
			}
		} else {
			gui.drawCenteredString(mc.fontRenderer, "An exception was thrown!", WIDTH / 2, HEIGHT / 2 - 8, 0xffdd0000);
		}
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		gsm.onKeyPressed(keyCode, typedChar);
		if (currentDisplayedGui != null)
			currentDisplayedGui.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		gsm.onKeyReleased(keyCode, typedChar);
		if (currentDisplayedGui != null)
			currentDisplayedGui.onKeyReleased(keyCode, typedChar);
	}
}