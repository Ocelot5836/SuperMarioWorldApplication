package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.util.ResourceLocation;

public abstract class Enemy extends Mob {

	public static final BufferedImage ENEMY_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/enemies.png"));

	protected double detectRange;

	public Enemy(GameTemplate game) {
		this(game, 0);
	}

	public Enemy(GameTemplate game, double detectRange) {
		super(game);
		this.detectRange = detectRange;
	}

	/**
	 * Checks whether or not the specified damage source can hurt this enemy.
	 * 
	 * @param source
	 *            The source of damage
	 * @return Whether or not the source can do any damage
	 */
	public boolean canDamage(MarioDamageSource source) {
		return source != MarioDamageSource.ENEMY;
	}

	/**
	 * Searches for the nearest player if it is within the enemy's range.
	 * 
	 * @return The player if it was found inside the range
	 */
	protected Player getNearestPlayer() {
		Player nearestPlayer = level.getNearestPlayer(this);
		double distance = Math.sqrt(Math.pow(x - nearestPlayer.getX(), 2) + Math.pow(y - nearestPlayer.getY(), 2));
		return distance <= detectRange ? nearestPlayer : null;
	}

	public enum MarioDamageSource {
		MARIO, SHELL, ENEMY
	}
}