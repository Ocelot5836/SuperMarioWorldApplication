package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.fx.PlayerBounceFX;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Deals damage to this entity if it comes in contact with the player or some other damage source.
 * 
 * @author Ocelot5836
 */
public interface IDamagable {

	/**
	 * Called to damage the enemy the player has hit.
	 * 
	 * @param entity
	 *            The entity hitting this entity
	 * @param source
	 *            The source of the damage
	 * @param sideHit
	 *            The side the player was hitting from
	 * @param isInstantKill
	 *            Whether or not the player is spin jumping
	 * @return Whether or not the damage taken actually dealt damage
	 */
	boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill);

	/**
	 * The default events that happen when an entity is stomped by mario.
	 * 
	 * @param player
	 *            The player that stomped the enemy
	 */
	default void defaultStompEnemy(Player player) {
		GameTemplate game = player.getGame();
		Level level = player.getLevel();
		level.add(new PlayerBounceFX(game, player.getX(), player.getY()));
		player.getProperties().increaseEnemyJumpCounter();
		int enemyJumpCount = player.getProperties().getEnemyJumpCount();
		if (enemyJumpCount >= 8) {
			player.getProperties().increaseLives();
			game.playSound(Sounds.COLLECT_ONE_UP, 1.0F);
			level.add(new TextFX(game, player.getX() + player.getWidth() / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth("1-UP") / 2, player.getY() + player.getHeight() / 2, 0, -0.4, "1-UP", 0xff00ff00, 1));
		} else {
			player.addScore(Lib.getScoreFromJumps(enemyJumpCount));
			game.playSound(Sounds.PLAYER_STOMP, 1.0F + (Math.min((float) enemyJumpCount, 7.0F) / 7.0F));
		}
	}

	/**
	 * The default events that happen when an entity is stomped on by another entity.
	 * 
	 * @param entity
	 *            This entity
	 */
	default void defaultKillEntity(Entity entity) {
		GameTemplate game = entity.getGame();
		Level level = entity.getLevel();
		game.playSound(Sounds.PLAYER_STOMP, 1.0F);
		entity.setDead();
	}

	/**
	 * The default events that happen when an entity is stomped on while mario is performing a spin jump.
	 * 
	 * @param player
	 *            The player that stomped this enemy
	 */
	default void defaultSpinStompEnemy(Player player) {
		GameTemplate game = player.getGame();
		Level level = player.getLevel();
		level.add(new PlayerBounceFX(game, player.getX(), player.getY()));
		player.getGame().playSound(Sounds.PLAYER_STOMP_SPIN, 1.0F);
	}
}