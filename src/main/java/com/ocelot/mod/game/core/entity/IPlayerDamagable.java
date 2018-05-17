package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.fx.PlayerBounceFX;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Deals damage to an enemy if he comes in contact with the player or osme other damage source.
 * 
 * @author Ocelot5836
 */
public interface IPlayerDamagable {

	/**
	 * Called to damage the enemy the player has hit.
	 * 
	 * @param player
	 *            The player hitting the enemy
	 * @param sideHit
	 *            The side the player was hitting from
	 * @param isPlayerSpinning
	 *            Whether or not the player is spin jumping
	 * @param isPlayerInvincible
	 *            Whether or not the player has a star or some other thing that makes him invincible
	 */
	void damageEnemy(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible);

	/**
	 * The default events that happen when an enemy is stomped by mario.
	 * 
	 * @param player
	 *            The player that stomped the enemy
	 */
	default void defaultStompEnemy(Player player) {
		GameTemplate game = player.getGame();
		Level level = player.getLevel();
		player.getProperties().increaseEnemyJumpCounter();
		int enemyJumpCount = player.getProperties().getEnemyJumpCount();
		if (enemyJumpCount >= 8) {
			player.getProperties().increaseLives();
			game.playSound(Sounds.COLLECT_ONE_UP, 1.0F);
			level.add(new TextFX(game, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0, -0.4, "1-UP", 0x00ff00, 1));
		} else {
			game.playSound(Sounds.PLAYER_STOMP, 1.0F + (Math.min((float) enemyJumpCount, 7.0F) / 7.0F));
		}
	}

	/**
	 * The default events that happen when an enemy is stomped on while mario is performing a spin jump.
	 * 
	 * @param player
	 *            The player that stomped the enemy
	 */
	default void defaultSpinStompEnemy(Player player) {
		GameTemplate game = player.getGame();
		Level level = player.getLevel();
		level.add(new PlayerBounceFX(game, player.getX(), player.getY()));
		player.getGame().playSound(Sounds.PLAYER_STOMP_SPIN, 1.0F);
	}
}