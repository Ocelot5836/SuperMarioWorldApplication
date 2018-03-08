package com.ocelot.mod.game.gamestate;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.MobMover;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.entity.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

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
		level.add(player = new Player(game, 0, 50).enableKeyboardInput(true));
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

	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {

	}

	private long getMenuTime() {
		return timer.getTime(TimeUnit.SECONDS);
	}
}