package com.ocelot.mod.game.core.gameState;

import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.core.level.LevelTemplate;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.gamestate.DebugSelectStateLevel;
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
		level.update();
		overlay.update();
		
		if(player.getProperties().isDead() || level.getProperties().getCurrTime() <= 0) {
			player.onDeath(this);
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		level.render(gui, mc, mouseX, mouseY, partialTicks);
		overlay.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		level.onKeyPressed(keyCode, typedChar);
		overlay.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
		overlay.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onLoseFocus() {
		level.getLevel().onLoseFocus();
	}

	protected void setPlayer(Player player) {
		this.player = player;
		this.overlay.setSizeAndWorld(game, player);
	}
}