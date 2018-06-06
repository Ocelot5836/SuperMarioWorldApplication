package com.ocelot.mod.game.main.gamestate;

import java.lang.annotation.RetentionPolicy;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Backgrounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.enemy.Galoomba;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class TestState extends GameState {

	private Background bg;
	private Level level;
	private Player player;

	public TestState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void load() {
		bg = new Background(Backgrounds.UNDERWATER, 100, 0.1, 0.5);
		level = new Level(16, new ResourceLocation(Mod.MOD_ID, "maps/test.map"));
		level.getMap().setTween(0.25);
		level.add(new Galoomba(game, 60, 50));
		level.add(player = new Player(game));
		player.setPosition(50, 50);
	}

	@Override
	public void update() {
		bg.setStartingPosition(0, -144);
		bg.update();
		bg.setPosition(level.getMap().getX(), level.getMap().getY());
		level.update();
		level.getMap().setPosition(player.getX() - Game.WIDTH / 2, 0);
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		bg.render();
		level.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		level.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_T) {
			load();
		}
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
	}

	@Override
	public void onLoseFocus() {
		player.onLoseFocus();
	}
}