package com.ocelot.mod.game.core.gameState;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Used as a state when something bad happens.
 * 
 * @author Ocelot5836
 */
public class ErrorState extends GameState {

	public ErrorState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		gui.drawCenteredString(mc.fontRenderer, I18n.format("state." + SuperMarioWorld.MOD_ID + ".error.message"), game.getWidth() / 2, game.getHeight() / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 0xffffffff);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
	}
}