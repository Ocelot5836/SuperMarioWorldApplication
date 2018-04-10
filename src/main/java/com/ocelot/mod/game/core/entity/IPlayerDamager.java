package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.main.entity.player.Player;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Deals damage to the player if he comes in contact with the enemy.
 * 
 * @author Ocelot5836
 */
public interface IPlayerDamager {

	/**
	 * Called to damage the player if the enemy needs to.
	 * 
	 * @param player
	 *            The player being hit
	 * @param sideHit
	 *            The side the player was hit on
	 * @param isPlayerSpinning
	 *            Whether or not the player is spin jumping
	 * @param isPlayerInvincible
	 *            Whether or not the player has a star or some other thing that makes him invincible
	 * @return Whether or not the player was able to be hit
	 */
	boolean damagePlayer(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible);
}