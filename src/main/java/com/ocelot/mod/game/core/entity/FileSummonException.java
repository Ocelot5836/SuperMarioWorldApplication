package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.level.LevelTemplate;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A simple, caught exception that is used to notify the {@link LevelTemplate}.
 * 
 * @author Ocelot5836
 */
public class FileSummonException extends Exception {

	public FileSummonException() {
		this("Attempted to summon a mob with improper arguments.");
	}

	public FileSummonException(String message) {
		super(message);
	}
}