package com.ocelot.mod.game;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.audio.Jukebox;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.ErrorState;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.main.gamestate.DebugSelectLevelState;
import com.ocelot.mod.game.main.gamestate.MenuState;
import com.ocelot.mod.game.main.gamestate.TestState;
import com.ocelot.mod.game.main.gamestate.level.DemoLevelState;
import com.ocelot.mod.game.main.gamestate.level.TestLevelState;
import com.ocelot.mod.game.main.gamestate.level.YoshiHouseState;
import com.ocelot.mod.game.main.gamestate.store.ShopState;
import com.ocelot.mod.game.main.gamestate.worldmap.WorldMapState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

public class GameStateManager {

	private Map<String, Class<? extends GameState>> gameStates = Maps.<String, Class<? extends GameState>>newHashMap();
	@Nonnull
	private GameState selectedState;
	private GameTemplate game;

	public static final String TEST = "TEST";
	public static final String SHOP = "SHOP";
	public static final String DEBUG_SELECT_LEVEL = "DEBUG_SELECT_LEVEL";
	public static final String MENU = "MENU";
	public static final String WORLD_MAP = "WORLD_MAP";
	public static final String YOSHI_HOUSE = "YOSHI_HOUSE";
	public static final String DEMO_LEVEL = "DEMO_LEVEL";
	public static final String TEST_LEVEL = "TEST_LEVEL";

	public GameStateManager(GameTemplate game) {
		this.game = game;

		registerGameState(TestState.class, TEST);
		registerGameState(ShopState.class, SHOP);
		registerGameState(DebugSelectLevelState.class, DEBUG_SELECT_LEVEL);
		registerGameState(MenuState.class, MENU);
		registerGameState(WorldMapState.class, WORLD_MAP);
		registerGameState(YoshiHouseState.class, YOSHI_HOUSE);
		registerGameState(DemoLevelState.class, DEMO_LEVEL);
		registerGameState(TestLevelState.class, TEST_LEVEL);

		this.loadState(DEBUG_SELECT_LEVEL);
	}

	public void registerGameState(Class<? extends GameState> clazz, String registryName) {
		if (!gameStates.containsKey(registryName)) {
			gameStates.put(registryName, clazz);
		} else {
			throw new RuntimeException("Game State \'" + clazz.getName() + "\' attempted to override another with the id of \'" + registryName + "\'");
		}
	}

	public void reload() {
		Jukebox.stopMusic();
		this.getSelectedState().load();
	}

	private void loadState(String gameState) {
		this.selectedState = this.createNewState(gameState);
		Jukebox.stopMusic();
		this.getSelectedState().load();
	}

	public void unloadState() {
		this.selectedState.unload();
		this.loadState(null);
	}

	public void setState(@Nullable String gameState) {
		this.unloadState();
		this.loadState(gameState);
	}

	@Nonnull
	public GameState createNewState(@Nullable String gameState) {
		Class<? extends GameState> clazz = this.gameStates.get(gameState);
		if (clazz != null) {
			try {
				return clazz.getConstructor(GameStateManager.class, GameTemplate.class).newInstance(this, game);
			} catch (Exception e) {
				SuperMarioWorld.logger().error("Could not load state \'" + gameState + "\'");
			}
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

	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		this.getSelectedState().onMousePressed(mouseButton, mouseX, mouseY);
	}

	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
		this.getSelectedState().onMouseReleased(mouseButton, mouseX, mouseY);
	}

	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
		this.getSelectedState().onMouseScrolled(direction, mouseX, mouseY);
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

	public Set<Entry<String, Class<? extends GameState>>> getGameStates() {
		return gameStates.entrySet();
	}
}