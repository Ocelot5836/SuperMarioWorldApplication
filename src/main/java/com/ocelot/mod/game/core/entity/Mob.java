package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * @author Ocelot5836
 */
public abstract class Mob extends Entity {

	private double xdest;
	private double ydest;

	/** The temporary new x */
	protected double xtemp;
	/** The temporary new y */
	protected double ytemp;

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
	/** Whether or not the entity is falling */
	protected boolean falling;

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
	private long flinchTimer;

	public Mob(GameTemplate game) {
		super(game);
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);
		if (flinchTimer == 0) {
			flinching = false;
		}
	}

	/**
	 * Checks whether or not the entity has collided with the tile map.
	 */
	protected void checkTileMapCollision() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;

		xdest = x + dx;
		ydest = y + dy;

		xtemp = x;
		ytemp = y;

		calculateCorners(x, ydest);
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			} else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			} else {
				ytemp += dy;
			}
		}

		calculateCorners(xdest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			} else {
				xtemp += dx;
			}
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			} else {
				xtemp += dx;
			}
		}

		if (!falling) {
			calculateCorners(x, ydest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
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

	@Override
	public void onLoseFocus() {
		this.left = false;
		this.right = false;
		this.down = false;
		this.up = false;
	}
}