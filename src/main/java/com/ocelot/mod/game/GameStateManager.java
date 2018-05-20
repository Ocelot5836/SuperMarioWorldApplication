package com.ocelot.mod.game;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Jukebox;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.ErrorState;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.main.gamestate.DebugSelectLevelState;
import com.ocelot.mod.game.main.gamestate.MenuState;
import com.ocelot.mod.game.main.gamestate.TestState;
import com.ocelot.mod.game.main.gamestate.level.DemoLevelState;
import com.ocelot.mod.game.main.gamestate.level.YoshiHouseState;
import com.ocelot.mod.game.main.gamestate.worldmap.WorldMapState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GameStateManager {

	private Map<Integer, GameState> gameStates = Maps.<Integer, GameState>newHashMap();
	private int selectedState;
	private GameTemplate game;

	public static final int ERROR = 0;
	public static final int TEST = 1;
	public static final int DEBUG_SELECT_LEVEL = 2;
	public static final int MENU = 3;
	public static final int WORLD_MAP = 4;
	public static final int YOSHI_HOUSE = 5;
	public static final int DEMO_LEVEL = 6;

	public GameStateManager(GameTemplate game) {
		this.game = game;

		gameStates.put(ERROR, new ErrorState(this, game));
		gameStates.put(TEST, new TestState(this, game));
		gameStates.put(DEBUG_SELECT_LEVEL, new DebugSelectLevelState(this, game));
		gameStates.put(MENU, new MenuState(this, game));
		gameStates.put(WORLD_MAP, new WorldMapState(this, game, new ResourceLocation(Mod.MOD_ID, "maps/world/test.map")));
		gameStates.put(YOSHI_HOUSE, new YoshiHouseState(this, game));
		gameStates.put(DEMO_LEVEL, new DemoLevelState(this, game));

		this.setState(DEBUG_SELECT_LEVEL);
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

	public void load(NBTTagCompound nbt) {
		this.getSelectedState().load(nbt);
	}

	public void save(NBTTagCompound nbt) {
		this.getSelectedState().save(nbt);
	}

	public void onLoseFocus() {
		this.getSelectedState().onLoseFocus();
	}

	public GameState getSelectedState() {
		return gameStates.get(selectedState);
	}

	public void setState(int gameState) {
		if (gameState != selectedState && gameStates.containsKey(gameState)) {
			Jukebox.stopMusic();
			this.selectedState = gameState;
			this.getSelectedState().init();
		}
	}

	public Map<Integer, GameState> getGameStates() {
		return new HashMap<Integer, GameState>(gameStates);
	}
}