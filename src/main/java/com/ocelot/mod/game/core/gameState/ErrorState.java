package com.ocelot.mod.game.core.gameState;

import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ErrorState extends GameState {

	public ErrorState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		gui.drawCenteredString(mc.fontRenderer, "Well, This is weird...", 150, 75, 0xffffffff);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {		
	}
	
	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
	}
}