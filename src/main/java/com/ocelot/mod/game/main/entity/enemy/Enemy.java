package com.ocelot.mod.game.main.entity.enemy;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.IDamager;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.main.entity.player.Player;

public abstract class Enemy extends Mob implements IDamager {

	/** The range that this enemy can detect mobs at */
	protected double detectRange;

	public Enemy(GameTemplate game) {
		this(game, 0);
	}

	/**
	 * @param detectRange
	 *            The range in pixels of how close a mob has to be to be detected by this enemy
	 */
	public Enemy(GameTemplate game, double detectRange) {
		super(game);
		this.detectRange = detectRange;
	}

	/**
	 * Searches for the nearest player if it is within the enemy's range.
	 * 
	 * @return The player if it was found inside the range
	 */
	protected Player getNearestPlayer() {
		Player nearestPlayer = this.level.getNearestPlayer(this);
		double distance = Math.sqrt(Math.pow(x - nearestPlayer.getX(), 2) + Math.pow(y - nearestPlayer.getY(), 2));
		return distance <= this.detectRange ? nearestPlayer : null;
	}

	/**
	 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
	 * 
	 * <br>
	 * </br>
	 * 
	 * The type of damage that can be dealt to a mob.
	 * 
	 * @author Ocelot5836
	 */
	public enum MarioDamageSource {
		MARIO, SHELL, ENEMY, HEAVY;

		public boolean isHeavy() {
			return this == HEAVY;
		}
	}
}