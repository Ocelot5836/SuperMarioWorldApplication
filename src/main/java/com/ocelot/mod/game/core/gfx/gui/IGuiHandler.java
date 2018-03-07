package com.ocelot.mod.game.core.gfx.gui;

import com.ocelot.mod.game.core.entity.Player;

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
	 * Opens a gui client sided.
	 * 
	 * @param id
	 *            The id of the gui
	 * @param player
	 *            The player that opened the gui
	 */
	MarioGui openClient(int id, Player player);

	/**
	 * Opens a gui server sided. (Currently unused)
	 * 
	 * @param id
	 *            The id of the gui
	 * @param player
	 *            The player that opened the gui
	 */
	MarioGui openServer(int id, Player player);

}