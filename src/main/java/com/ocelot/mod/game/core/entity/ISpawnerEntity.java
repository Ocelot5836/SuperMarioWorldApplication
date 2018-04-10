package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Specifies an entity can be spawned by a spawner.
 * 
 * @author Ocelot5836
 */
public interface ISpawnerEntity {

	/**
	 * Creates a new entity and adds it to the list of the spawner.
	 * 
	 * @param game
	 *            The game instance
	 * @param level
	 *            The level to add the entities to
	 * @param spawner
	 *            The spawner trying to spawn the entity
	 * @param x
	 *            The x position to spawn the entity
	 * @param y
	 *            The y position to spawn the entity
	 * @param args
	 *            The parameters passed in
	 * @throws SummonException
	 *             Throws this if the entity cannot be summoned
	 */
	void create(GameTemplate game, Level level, Spawner spawner, double x, double y, Object... args) throws SummonException;

	/**
	 * Attempts to parse a byte.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default byte parseByte(Object param) {
		return param instanceof Byte ? (Byte) param : 0;
	}

	/**
	 * Attempts to parse a short.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default short parseShort(Object param) {
		return param instanceof Byte ? (Byte) param : 0;
	}

	/**
	 * Attempts to parse an integer.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default int parseInt(Object param) {
		return param instanceof Integer ? (Integer) param : 0;
	}

	/**
	 * Attempts to parse a float.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default float parseFloat(Object param) {
		return param instanceof Float ? (Float) param : 0;
	}

	/**
	 * Attempts to parse a double.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default double parseDouble(Object param) {
		return param instanceof Double ? (Double) param : 0;
	}

	/**
	 * Attempts to parse a long.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default long parseLong(Object param) {
		return param instanceof Long ? (Long) param : 0;
	}

	/**
	 * Attempts to parse a boolean.
	 * 
	 * @param param
	 *            The param to parse
	 * @return The value if it was parsed
	 */
	default boolean parseBoolean(Object param) {
		return param instanceof Boolean ? (Boolean) param : false;
	}
}