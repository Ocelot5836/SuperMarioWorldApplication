package com.ocelot.mod.game.main.entity.ai;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Used to detect when sides of walls are hit.
 * 
 * @author Ocelot5836
 */
public interface BasicWalkListener {

	/**
	 * Called when the ai hits the side
	 * 
	 * @param right
	 *            Whether or not the right side was hit
	 */
	void basicWalkTrigger(boolean right);

}