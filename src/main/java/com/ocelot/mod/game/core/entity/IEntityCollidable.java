package com.ocelot.mod.game.core.entity;

import java.awt.Rectangle;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Specifies an entity has the possibility of causing entities to collide with it like tiles.
 * 
 * @author Ocelot5836
 */
public interface IEntityCollidable {

	/**
	 * Used to detect collisions.
	 * 
	 * @return The rectangle with the x, and y offsets and the width and height of the collision
	 */
	Rectangle getCollisionBox();
}