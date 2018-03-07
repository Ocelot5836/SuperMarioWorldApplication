package com.ocelot.mod.game.core.gameState;

import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class GameState {

	protected GameStateManager gsm;
	protected GameTemplate game;

	public GameState(GameStateManager gsm, GameTemplate game) {
		this.gsm = gsm;
		this.game = game;
	}

	public abstract void init();

	public abstract void update();

	public abstract void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);

	public abstract void onKeyPressed(int keyCode, char typedChar);

	public abstract void onKeyReleased(int keyCode, char typedChar);

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}