package com.ocelot.api.mod;

import com.ocelot.mod.game.GameStateManager;

/**
 * 
 * @author Ocelot5836
 */
public interface MarioPlugin {

	/**
	 * Called to initialize anything that may need to be initialized such as backgrounds.
	 */
	void register();

	/**
	 * Called when the {@link GameStateManager} is ready to accept game states. Use {@link GameStateManager#register(Class, String)} to add new states.
	 * 
	 * @param gsm
	 *            The game state manager
	 */
	void registerGameStates(GameStateManager gsm);
}