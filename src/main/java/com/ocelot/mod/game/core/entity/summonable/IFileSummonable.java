package com.ocelot.mod.game.core.entity.summonable;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.SummonException;
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
	 * Called to summon the mob from file.
	 * 
	 * @param game
	 *            The game instance
	 * @param level
	 *            The level instance
	 * @param args
	 *            The arguments passed.
	 * @throws SummonException
	 *             If the mob cannot be summoned, throw this.
	 */
	void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException;

	/**
	 * Used in registry.
	 * 
	 * @return The name that is going to be used to summon the entity
	 */
	String getRegistryName();
	
	/**
	 * Throws an exception if the mob cannot be summoned.
	 * 
	 * @throws SummonException
	 *             If the mob cannot be summoned, throw this.
	 */
	default void throwSummonException() throws SummonException {
		throw new SummonException();
	}

	/**
	 * Throws an exception if the mob cannot be summoned.
	 * 
	 * @param message
	 *            The error message to why the mob cannot be summoned
	 * @throws SummonException
	 *             If the mob cannot be summoned, throw this.
	 */
	default void throwSummonException(String message) throws SummonException {
		throw new SummonException(message);
	}
}