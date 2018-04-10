package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.GameTemplate;

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

	/** Whether or not the entity is flinching (Immune to damage) */
	protected boolean flinching;
	/** Used to time flinching to know when to stop */
	protected long flinchTimer;

	public Mob(GameTemplate game) {
		super(game);
		this.flinchTimer = 1000;
	}

	@Override
	public void update() {
		super.update();
		
		if(flinching) {
			long elapsed = getFlinchElapsedTime();
			if(elapsed > 2000) {
				flinching = false;
			}
		}
	}

	/**
	 * Resets the mob's velocity values.
	 */
	public void resetVelocity() {
		xtemp = 0;
		ytemp = 0;
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
	 * @return The amount of time the entity was flinching
	 */
	public long getFlinchElapsedTime() {
		return (System.nanoTime() - flinchTimer) / 1000000;
	}

	/**
	 * @return Whether or not the mob is flinching
	 */
	public boolean isFlinching() {
		return flinching;
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