package com.ocelot.mod.game.core.gameState;

import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic state in the game.
 * 
 * @author Ocelot5836
 */
public abstract class GameState {

	/** The game state manager instance */
	protected GameStateManager gsm;
	/** The game instance */
	protected GameTemplate game;

	/**
	 * Creates a new game state.
	 * 
	 * @param gsm
	 *            The game state manager instance
	 * @param game
	 *            The game instance
	 */
	public GameState(GameStateManager gsm, GameTemplate game) {
		this.gsm = gsm;
		this.game = game;
	}

	/**
	 * Initializes the state when it is about to change to it.
	 */
	public void load() {
	}
	
	/**
	 * Called right before the state is set to null to clear any caches or anything like that.
	 */
	public void unload() {
	}

	/**
	 * Updates the game state.
	 */
	public abstract void update();

	/**
	 * Renders the game state to the screen.
	 * 
	 * @param gui
	 *            A gui instance
	 * @param mc
	 *            A minecraft instance
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param partialTicks
	 *            The partial ticks
	 */
	public abstract void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);

	/**
	 * Called when a key is pressed.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public abstract void onKeyPressed(int keyCode, char typedChar);

	/**
	 * Called when a key is released.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public abstract void onKeyReleased(int keyCode, char typedChar);

	/**
	 * Called when the game window loses focus.
	 */
	public void onLoseFocus() {
	}

	/**
	 * Loads the data from the tag compound.
	 * 
	 * @param nbt
	 *            The tag compound to read from
	 */
	public void load(NBTTagCompound nbt) {
	}

	/**
	 * Saves the data to the tag compound.
	 * 
	 * @param nbt
	 *            The tag compound to save to
	 */
	public void save(NBTTagCompound nbt) {
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}