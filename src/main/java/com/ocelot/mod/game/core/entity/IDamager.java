package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.main.entity.player.Player;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Deals damage to the entity if it comes in contact with this entity.
 * 
 * @author Ocelot5836
 */
public interface IDamager {

	/**
	 * Called to damage the player if the enemy needs to.
	 * 
	 * @param entity
	 *            The entity being hit
	 * @param sideHit
	 *            The side the player was hit on
	 * @return Whether or not the player was able to be hit
	 */
	boolean dealDamage(Entity entity, EnumDirection sideHit);
}