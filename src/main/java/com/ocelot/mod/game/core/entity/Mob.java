package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class Mob extends Entity {

	private double xdest;
	private double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean collidedTop;
	protected boolean collidedBottom;
	protected boolean collidedLeft;
	protected boolean collidedRight;

	protected boolean facingRight;
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	protected int health;
	protected int maxHealth;

	protected boolean flinching;
	private long flinchTimer;

	public Mob(GameTemplate game) {
		super(game);
	}

	/**
	 * Make sure to call the super for {@link #update()} or else the entity will not render correctly!
	 */
	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);
		if (flinchTimer == 0) {
			flinching = false;
		}
	}

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

	public double getDx() {
		return dx;
	}
	
	public double getDy() {
		return dy;
	}
	
	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	protected long getFlinchElapsedTime() {
		return (System.nanoTime() - flinchTimer) / 1000000;
	}

	public void setDX(double dx) {
		this.dx = dx;
	}

	public void setDY(double dy) {
		this.dy = dy;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	protected void setHealth(int health) {
		this.health = health;
	}

	protected void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}