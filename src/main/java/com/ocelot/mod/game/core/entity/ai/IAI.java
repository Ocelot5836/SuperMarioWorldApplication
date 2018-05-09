package com.ocelot.mod.game.core.entity.ai;

import com.ocelot.mod.game.core.entity.Mob;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The base for any type of AI. Does not have any overhead just in case.
 * 
 * @author Ocelot5836
 */
public interface IAI {

	/**
	 * Initializes the AI.
	 */
	void initAI();

	/**
	 * Called every update to update the AI.
	 */
	void update();

	/**
	 * Sets the mob for the AI.
	 * 
	 * @param mob
	 *            The mob that has the AI
	 */
	void setMob(Mob mob);

	/**
	 * Used when registering an AI for a mob.
	 * 
	 * @return The name of the ai
	 */
	String getName();
}