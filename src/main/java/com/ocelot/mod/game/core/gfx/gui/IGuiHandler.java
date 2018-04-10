package com.ocelot.mod.game.core.gfx.gui;

import com.ocelot.mod.game.main.entity.player.Player;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows the user to open a gui by id.
 * 
 * @author Ocelot5836
 */
public interface IGuiHandler {

	/**
	 * Opens a gui onto the screen.
	 * 
	 * @param id
	 *            The id of the gui
	 * @param player
	 *            The player that opened the gui
	 */
	MarioGui openGui(int id, Player player);

}