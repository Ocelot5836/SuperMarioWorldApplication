package com.ocelot.mod.game.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.ai.IAI;
import com.ocelot.mod.game.main.entity.enemy.Enemy.MarioDamageSource;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * @author Ocelot5836
 */
public abstract class Mob extends Entity {

	/** Whether or not this entity is facing right */
	protected boolean facingRight;
	/** Whether or not the entity is moving left */
	protected boolean left;
	/** Whether or not the entity is moving right */
	protected boolean right;
	/** Whether or not the entity is moving up */
	protected boolean up;
	/** Whether or not the entity is moving down */
	protected boolean down;
	/** Whether or not the entity is jumping */
	protected boolean jumping;

	/** How fast the entity can travel until it reaches it's maximum */
	protected double moveSpeed;
	/** The maximum speed the entity can travel */
	protected double maxSpeed;
	/** How fast the entity stops */
	protected double stopSpeed;
	/** The falling speed of the entity */
	protected double fallSpeed;
	/** The terminal velocity of the entity */
	protected double maxFallSpeed;
	/** The height/power of the jump */
	protected double jumpStart;
	/** Used for better jumping */
	protected double stopJumpSpeed;
	/** The health of the entity */
	protected int health;
	/** The maximum health of the entity */
	protected int maxHealth;

	private List<IAI> ais;

	public Mob(GameTemplate game) {
		super(game);
		this.ais = new ArrayList<IAI>();
	}

	public void initAI() {
	}

	public void registerAI(IAI ai) {
		for (IAI currentAi : ais) {
			if (currentAi.getName().equalsIgnoreCase(ai.getName())) {
				return;
			}
		}
		if (ai == null)
			Game.stop(new RuntimeException("AI for entity " + this.toString() + " was found to be null. This should NOT happen. Please to report to the mod author about this if you encounter it."), "Fatal Error Occured");

		ai.setMob(this);
		ai.initAI();
		ais.add(ai);
	}

	@Override
	public void update() {
		super.update();

		for (IAI ai : ais) {
			ai.update();
		}
	}
	
	/**
	 * Gets the next position for the mob.
	 */
	protected void getNextPosition() {
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx += stopSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx -= stopSpeed;
			}
		} else {
			if (dx > 0) {
				dx = 0;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx = 0;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		if (falling) {
			dy += fallSpeed;
			if (dy > 0)
				jumping = false;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed) {
				dy = maxFallSpeed;
			}
		}

		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}

	/**
	 * Resets the mob's velocity values.
	 */
	public void resetVelocity() {
		xtemp = 0;
		ytemp = 0;
	}

	/**
	 * Checks whether or not the specified damage source can hurt this enemy.
	 * 
	 * @param entity
	 *            The entity that is damaging the mob
	 * @param source
	 *            The source of damage
	 * @return Whether or not the source can do any damage
	 */
	public boolean canDamage(Entity entity, MarioDamageSource source) {
		return entity != this;
	}

	/**
	 * @return The entity's health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @return The entity's maximum health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * @return if the mob is moving left or not.
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * @return if the mob is moving right or not.
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * @return if the mob is moving up or not.
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * @return if the mob is moving down or not.
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * @return if the mob is jumping or not.
	 */
	public boolean isJumping() {
		return jumping;
	}

	/**
	 * @return if the mob is falling or not.
	 */
	public boolean isFalling() {
		return falling;
	}

	/**
	 * @return Whether or not the mob is facing right
	 */
	public boolean isFacingRight() {
		return facingRight;
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public double getFallSpeed() {
		return fallSpeed;
	}

	public double getMaxFallSpeed() {
		return maxFallSpeed;
	}

	/**
	 * Sets the entity's health.
	 * 
	 * @param health
	 *            The new health value
	 */
	protected void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Sets the entity's maximum health.
	 * 
	 * @param health
	 *            The new maximum health value
	 */
	protected void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * Sets the mob to move left or not.
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * Sets the mob to move right or not.
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * Sets the mob to move up or not.
	 */
	public void setUp(boolean up) {
		this.up = up;
	}

	/**
	 * Sets the mob to move down or not.
	 */
	public void setDown(boolean down) {
		this.down = down;
	}

	/**
	 * Sets the mob to jump or not.
	 */
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	/**
	 * Sets the mob to fall or not.
	 */
	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	@Override
	public void onLoseFocus() {
		this.left = false;
		this.right = false;
		this.down = false;
		this.up = false;
	}
}