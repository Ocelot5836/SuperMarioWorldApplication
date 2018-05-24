package com.ocelot.mod.game;

import java.util.Map;

import javax.annotation.Nonnull;

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

	private Map<Integer, String> gameStates = Maps.<Integer, String>newHashMap();
	@Nonnull
	private GameState selectedState;
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

		gameStates.put(ERROR, "ERROR");
		gameStates.put(TEST, "TEST");
		gameStates.put(DEBUG_SELECT_LEVEL, "DEBUG_SELECT_LEVEL");
		gameStates.put(MENU, "MENU");
		gameStates.put(WORLD_MAP, "WORLD_MAP");
		gameStates.put(YOSHI_HOUSE, "YOSHI_HOUSE");
		gameStates.put(DEMO_LEVEL, "DEMO_LEVEL");

		this.loadState(DEBUG_SELECT_LEVEL);
	}

	public void reload() {
		Jukebox.stopMusic();
		this.getSelectedState().load();
	}

	private void loadState(int gameState) {
		this.selectedState = this.createNewState(gameState);
		Jukebox.stopMusic();
		this.getSelectedState().load();
	}

	public void unloadState() {
		this.selectedState.unload();
		this.loadState(ERROR);
	}

	public void setState(int gameState) {
		this.unloadState();
		this.loadState(gameState);
	}

	@Nonnull
	public GameState createNewState(int gameState) {
		switch (gameState) {
		case ERROR:
			return new ErrorState(this, game);
		case TEST:
			return new TestState(this, game);
		case DEBUG_SELECT_LEVEL:
			return new DebugSelectLevelState(this, game);
		case MENU:
			return new MenuState(this, game);
		case WORLD_MAP:
			return new WorldMapState(this, game, new ResourceLocation(Mod.MOD_ID, "maps/world/test.map"));
		case YOSHI_HOUSE:
			return new YoshiHouseState(this, game);
		case DEMO_LEVEL:
			return new DemoLevelState(this, game);
		}
		return new ErrorState(this, game);
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
		return selectedState;
	}

	public Map<Integer, String> getGameStates() {
		return gameStates;
	}
}