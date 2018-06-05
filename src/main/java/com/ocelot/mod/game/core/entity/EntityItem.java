package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.GameTemplate;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic item in the world.
 * 
 * @author Ocelot5836
 */
public class EntityItem extends Entity {

	private double xSpeedCopy;
	private double ySpeedCopy;
	private double currentFallSpeed;

	private double currentXBoostSpeed;
	private double currentYBoostSpeed;

	protected double xSpeed;
	protected double ySpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double boostSpeed;
	protected double slideSpeed;
	protected double airSlideSpeed;

	public EntityItem(GameTemplate game) {
		this(game, 0, 0, 0.0D);
	}

	public EntityItem(GameTemplate game, double xSpeed, double ySpeed) {
		this(game, xSpeed, ySpeed, 0.0D);
	}

	public EntityItem(GameTemplate game, double xSpeed, double ySpeed, double fallSpeed) {
		super(game);
		this.x = 0;
		this.y = 0;
		this.xSpeed = Math.abs(xSpeed);
		this.ySpeed = Math.abs(ySpeed);
		this.xSpeedCopy = xSpeed;
		this.ySpeedCopy = ySpeed;
		this.fallSpeed = fallSpeed;
		this.maxFallSpeed = Math.min(4.0, fallSpeed * 4);
		this.boostSpeed = -3.8;
		this.slideSpeed = 0.8;
		this.airSlideSpeed = 0.99;
	}

	@Override
	public void update() {
		super.update();

		getNextPosition();
		getNextPosition();
		getNextPosition();
	}

	private void getNextPosition() {
		if (xSpeed != 0) {
			calculateCorners(xdest, y);
			if (topLeft || bottomLeft || topRight || bottomRight) {
				xSpeed = -xSpeed;
				currentXBoostSpeed = 0;
				this.onXBounce();
			}
		}

		if (ySpeed != 0) {
			calculateCorners(x, ydest);
			if (topLeft || topRight || bottomLeft || bottomRight) {
				ySpeed = -ySpeed;
				this.onYBounce();
			}
		}

		calculateCorners(x, ydest);
		if (topLeft || topRight) {
			currentYBoostSpeed = 0;
		}

		calculateCorners(xdest, y);
		if (topLeft || bottomLeft) {
			currentXBoostSpeed = 0;
		}

		if (topRight || bottomRight) {
			currentXBoostSpeed = 0;
		}

		if (falling && fallSpeed > 0) {
			currentFallSpeed += fallSpeed;
			if (currentFallSpeed > maxFallSpeed) {
				currentFallSpeed = maxFallSpeed;
			}
			currentYBoostSpeed += currentFallSpeed;
		}
		
		dx = xSpeed + currentXBoostSpeed;
		dy = ySpeed + currentYBoostSpeed;

		if (currentXBoostSpeed != 0) {
			dx += currentXBoostSpeed;
			currentXBoostSpeed *= !falling ? slideSpeed : airSlideSpeed;
			if(Math.abs(currentXBoostSpeed) < 0.5) {
				currentXBoostSpeed = 0;
			}
		}

		if (currentYBoostSpeed != 0) {
			dy += currentYBoostSpeed;
		}

		if (!falling) {
			currentFallSpeed = 0;
		}

		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}

	/**
	 * Called when the item bounces off a wall in the x direction
	 */
	protected void onXBounce() {
	}

	/**
	 * Called when the item bounces off a wall in the y direction
	 */
	protected void onYBounce() {
	}

	public void updateLastPosition() {
		super.update();
	}

	/**
	 * Resets the x and y speeds to their origional setting.
	 */
	public void resetDirections() {
		this.xSpeed = xSpeedCopy;
		this.ySpeed = ySpeedCopy;
	}

	/**
	 * Sets the falling and boost values to zero.
	 */
	public void resetVelocity() {
		this.currentFallSpeed = 0;
		this.currentXBoostSpeed = 0;
		this.currentYBoostSpeed = 0;
	}

	/**
	 * Sets the direction the item will move in.
	 * 
	 * @param xSpeed
	 *            The x direction to move
	 * @param ySpeed
	 *            The y direction to move
	 */
	public void setDirection(double xSpeed, double ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}

	/**
	 * Flips the moving direction.
	 */
	public void flipDir() {
		this.xSpeed = -xSpeed;
	}

	/**
	 * @return The amount the item will move in the x direction
	 */
	public double getXSpeed() {
		return xSpeed;
	}

	/**
	 * @return The amount the item will move in the y direction
	 */
	public double getYSpeed() {
		return ySpeed;
	}
	
	@Override
	public boolean isOnScreen() {
		return this.x + this.cwidth / 2 >= tileMap.getX() && this.x - this.cwidth / 2 < tileMap.getX() + game.getWidth() && this.y + this.cheight >= tileMap.getY() && this.y + this.cheight / 2 < tileMap.getY() + game.getHeight();
	}

	/**
	 * Boosts the item in the direction specified based on percentages.
	 * 
	 * @param x
	 *            The x percentage
	 * @param y
	 *            The y percentage
	 */
	public void boost(double x, double y) {
		currentXBoostSpeed = x * boostSpeed;
		currentYBoostSpeed = y * boostSpeed;
	}
}