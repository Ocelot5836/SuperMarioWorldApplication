package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.main.entity.fx.PlayerBounceFX;
import com.ocelot.mod.game.main.entity.player.Player;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Specifies that mario can carry this item.
 * 
 * @author Ocelot5836
 */
public interface IItemCarriable {

	/**
	 * Called when the player updates.
	 * 
	 * @param player
	 *            The player that is holding the item
	 */
	void onHeldUpdate(Player player);

	/**
	 * Called when the player picks this item up.
	 * 
	 * @param player
	 *            The player that picked it up
	 */
	void onPickup(Player player);

	/**
	 * Called when the player throws this item.
	 * 
	 * @param player
	 *            The player that threw the item
	 * @param type
	 *            The type of throwing that occurred
	 */
	void onThrow(Player player, ThrowingType type);

	/**
	 * Called when the player drops this item.
	 * 
	 * @param player
	 *            The player that dropped the item
	 */
	void onDrop(Player player);

	/**
	 * Checks if the player can pickup the item.
	 * 
	 * @param player
	 *            The player attempting to pickup the item
	 * @return Whether or not the player can pickup the item
	 */
	boolean canPickup(Player player);

	/**
	 * Checks if the player can hold the item.
	 * 
	 * @param player
	 *            The player holding the item
	 * @return Whether or not the player can hold the item
	 */
	boolean canHold(Player player);

	/**
	 * Sets the default settings for items when they are thrown. Should be called when the item is thrown.
	 * 
	 * @param player
	 *            The player that was holding the item
	 * @param type
	 *            The type of throwing that occurred
	 */
	default void setDefaultThrowing(Player player, ThrowingType type) {
		if (!(this instanceof EntityItem))
			return;

		EntityItem item = (EntityItem) this;
		if (player.isFacingRight()) {
			item.setPosition(item.getX() + 5, player.getY() - 1);
		} else {
			item.setPosition(item.getX() - 5, player.getY() - 1);
		}
		item.resetDirections();

		player.getLevel().add(new PlayerBounceFX(player.getGame(), player.getX(), player.getY()));

		if (type == ThrowingType.SIDE) {
			if (player.isFacingRight()) {
				item.setDirection(item.getXSpeed(), item.getYSpeed());
				item.boost(-0.5f, 0);
			} else {
				item.setDirection(-item.getXSpeed(), item.getYSpeed());
				item.boost(0.5f, 0);
			}
		} else if (type == ThrowingType.UP) {
			item.setDirection(0, 0);
			item.boost(0, 0.6f);
		} else {
			item.setDirection(0, 0);
		}
	}

	/**
	 * Sets the default settings for items when they are placed. Should be called when the item is placed.
	 * 
	 * @param player
	 *            The player that was holding the item
	 */
	default void setDefaultPlacing(Player player) {
		if (!(this instanceof EntityItem))
			return;

		EntityItem item = (EntityItem) this;
		if (player.isFacingRight()) {
			item.setPosition(item.getX() + 5, player.getY() - 4);
		} else {
			item.setPosition(item.getX() - 5, player.getY() - 4);
		}
		item.setDirection(0, 0);
	}

	public enum ThrowingType {
		SIDE, UP, UNKNOWN
	}
}