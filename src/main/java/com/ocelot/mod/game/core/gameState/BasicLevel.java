package com.ocelot.mod.game.core.gameState;

import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.gui.GuiOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class BasicLevel extends GameState {

	protected LevelTemplate level;
	protected Player player;
	protected MarioGui overlay;

	public BasicLevel(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	protected void init(LevelTemplate level) {
		this.level = level;
		this.level.getProperties().playMusic();
		this.overlay = new GuiOverlay(level);
	}

	@Override
	public void update() {
		this.level.update();
		this.overlay.update();

		if (this.player != null) {
			if (this.player.getProperties().isDead() || level.getProperties().getCurrTime() <= 0) {
				this.player.onDeath(this);
			}

			this.level.getLevel().getMap().setPosition(this.player.getX() - Game.WIDTH / 2, 0);
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.level.render(gui, mc, mouseX, mouseY, partialTicks);
		this.overlay.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		this.level.onKeyPressed(keyCode, typedChar);
		this.overlay.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		this.level.onKeyReleased(keyCode, typedChar);
		this.overlay.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		this.level.onMousePressed(mouseButton, mouseX, mouseY);
		this.overlay.onMousePressed(mouseButton, mouseX, mouseY);
	}

	@Override
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
		this.level.onMouseReleased(mouseButton, mouseX, mouseY);
		this.overlay.onMouseReleased(mouseButton, mouseX, mouseY);
	}

	@Override
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
		this.level.onMouseScrolled(direction, mouseX, mouseY);
		this.overlay.onMouseScrolled(direction, mouseX, mouseY);
	}

	@Override
	public void onLoseFocus() {
		this.level.getLevel().onLoseFocus();
	}

	protected void setPlayer(Player player) {
		this.player = player;
		this.overlay.setSizeAndWorld(game, player);
	}
}