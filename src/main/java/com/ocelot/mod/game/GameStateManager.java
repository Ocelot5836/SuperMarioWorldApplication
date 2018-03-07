package com.ocelot.mod.game;

import java.util.Map;

import com.google.common.collect.Maps;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.ErrorState;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gameState.TestState;
import com.ocelot.mod.game.core.gameState.level.YoshiHouseState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GameStateManager {

	private Map<String, GameState> gameStates = Maps.<String, GameState>newHashMap();
	private String selectedState;
	private GameTemplate game;

	public static final String ERROR = "error";
	public static final String TEST = "test";
	public static final String YOSHI_HOUSE = "yoshi_house";

	public GameStateManager(GameTemplate game) {
		this.game = game;

		gameStates.put(ERROR, new ErrorState(this, game));
		gameStates.put(TEST, new TestState(this, game));
		gameStates.put(YOSHI_HOUSE, new YoshiHouseState(this, game));

		this.setState(YOSHI_HOUSE);
	}

	public void update() {
		this.getSelectedState().update();
	}

	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.getSelectedState().render(gui, mc, mouseX, mouseY, partialTicks);
	}

	public void onKeyPressed(int keyCode, char typedChar) {
		this.getSelectedState().onKeyPressed(keyCode, typedChar);
	}
	
	public void onKeyReleased(int keyCode, char typedChar) {
		this.getSelectedState().onKeyReleased(keyCode, typedChar);
	}
	
	public GameState getSelectedState() {
		return gameStates.get(selectedState);
	}

	public void setState(String gameState) {
		if (!gameState.equalsIgnoreCase(selectedState)) {
			this.selectedState = gameState;
			this.getSelectedState().init();
		}
	}
}