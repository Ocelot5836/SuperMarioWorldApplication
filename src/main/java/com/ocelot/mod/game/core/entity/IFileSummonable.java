package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows a mob to be summoned through files.
 * 
 * @author Ocelot5836
 */
public interface IFileSummonable {

	/**
	 * Called to summon the mob.
	 * 
	 * @param game
	 *            The game instance
	 * @param level
	 *            The level instance
	 * @param args
	 *            The arguments passed.
	 * @throws FileSummonException
	 *             If the mob cannot be summoned, throw this.
	 */
	void summon(GameTemplate game, Level level, String[] args) throws FileSummonException;

	/**
	 * Throws an exception if the mob cannot be summoned.
	 * 
	 * @throws FileSummonException
	 *             If the mob cannot be summoned, throw this.
	 */
	default void throwSummonException() throws FileSummonException {
		throw new FileSummonException();
	}

	/**
	 * Throws an exception if the mob cannot be summoned.
	 * 
	 * @param message
	 *            The error message to why the mob cannot be summoned
	 * @throws FileSummonException
	 *             If the mob cannot be summoned, throw this.
	 */
	default void throwSummonException(String message) throws FileSummonException {
		throw new FileSummonException(message);
	}
}