package com.ocelot.mod.game.core.gfx.gui;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class MarioGui {

	protected Minecraft mc;
	protected Player player;
	protected GameTemplate game;
	protected int width;
	protected int height;

	public void initGui() {
	}

	public void update() {
	}

	public void onKeyPressed(int keyCode, char typedChar) {
		if (keyCode == Keyboard.KEY_RETURN) {
			this.player.closeGui();
		}
	}

	public void onKeyReleased(int keyCode, char typedChar) {
	}

	public void render(Gui gui, int mouseX, int mouseY, float partialTicks) {
	}

	public MarioGui setSizeAndWorld(GameTemplate game, Player player) {
		this.game = game;
		this.width = game.getWidth();
		this.height = game.getHeight();
		this.player = player;
		this.mc = Minecraft.getMinecraft();
		return this;
	}
}