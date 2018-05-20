package com.ocelot.mod.game.main.gamestate;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.MobMover;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

@DebugSelectStateLevel
public class MenuState extends GameState {

	private StopWatch timer;
	private Level level;
	private Player player;
	private MobMover bot;

	public MenuState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void init() {
		timer = StopWatch.createStarted();
		level = new Level(16, new ResourceLocation(Mod.MOD_ID, "maps/test.map"));
		level.getMap().setTween(0.25);
		level.add(player = new Player(game, 0, 50).enableKeyboardInput(false));
		bot = new MobMover(player).addPos(200, 0, 2, 0).addPos(-150, 0).addPos(50, 0, 1, 0);
	}

	@Override
	public void update() {
		level.update();
		bot.update(timer.getTime());

		level.getMap().setPosition(player.getX() - Game.WIDTH / 2, 0);

		if (this.getMenuTime() == 1) {
			player.setJumping(true);
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		level.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		level.onKeyPressed(keyCode, typedChar);

		if (keyCode == Keyboard.KEY_Y) {
			init();
		}
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
	}

	private long getMenuTime() {
		return timer.getTime(TimeUnit.SECONDS);
	}
}