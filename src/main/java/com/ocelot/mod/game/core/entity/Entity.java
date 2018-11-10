package com.ocelot.mod.game.core.entity;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.lib.AxisAlignedBB;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic, abstract entity that can be placed in a {@link Level}.
 * 
 * @author Ocelot5836
 */
public abstract class Entity {

	/** The x position the entity wants to move to */
	protected double xdest;
	/** The y position the entity wants to move to */
	protected double ydest;

	/** The game's instance */
	protected GameTemplate game;
	/** The level this entity is currently in */
	protected Level level;
	/** The tile map used in the level */
	protected TileMap tileMap;
	/** The size of each tile */
	protected int tileSize;
	/** The x position */
	protected double x;
	/** The y position */
	protected double y;
	/** The last x position */
	protected double lastX;
	/** The last y position */
	protected double lastY;

	/** The temporary new x */
	protected double xtemp;
	/** The temporary new y */
	protected double ytemp;

	/** The width of this entity */
	protected double cwidth;
	/** The height of this entity */
	protected double cheight;

	/** Collision variables */
	public boolean topLeft, topRight, bottomLeft, bottomRight;

	/** The current collumn this entity is in */
	protected int currCol;
	/** The current row this entity is in */
	protected int currRow;

	/** The x speed */
	protected double dx;
	/** The y speed */
	protected double dy;

	/** Whether or not the entity is falling */
	protected boolean falling;
	/** Whether or not the entity is able to take damage */
	public boolean invulnerable;

	private boolean dead;

	public Entity(GameTemplate game) {
		this.game = game;
	}

	/**
	 * Initializes the entity with it's level.
	 * 
	 * @param level
	 *            The level this entity is in
	 */
	public void init(Level level) {
		this.level = level;
		this.tileMap = level.getMap();
		this.tileSize = level.getMap().getTileSize();
	}

	/**
	 * Called 20 times per second. Updates the entity's logic.
	 */
	public void update() {
		this.lastX = x;
		this.lastY = y;
	}

	/**
	 * Renders the entity to the screen.
	 * 
	 * @param gui
	 *            A gui instance
	 * @param mc
	 *            A minecraft instance
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param partialTicks
	 *            The partial ticks
	 */
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
	}

	/**
	 * Called each time a key is pressed.
	 * 
	 * @param keyCode
	 *            The code of the key typed
	 * @param typedChar
	 *            The character that was typed
	 */
	public void onKeyPressed(int keyCode, char typedChar) {
	}

	/**
	 * Called each time a key is released.
	 * 
	 * @param keyCode
	 *            The code of the key typed
	 * @param typedChar
	 *            The character that was typed
	 */
	public void onKeyReleased(int keyCode, char typedChar) {
	}

	/**
	 * Called when the mouse is pressed.
	 * 
	 * @param mouseButton
	 *            The button pressed
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
	}

	/**
	 * Called when the mouse is released.
	 * 
	 * @param mouseButton
	 *            The button released
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
	}

	/**
	 * Called when the mouse is scrolled.
	 * 
	 * @param direction
	 *            The direction the mouse was scrolled
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
	}

	/**
	 * Updates the {@link #topLeft}, {@link #topRight}, {@link #bottomLeft}, and {@link #bottomRight} variables with this entity's current position. Also notifies tiles of when the entity collides with it.
	 * 
	 * @param x
	 *            The x position to check
	 * @param y
	 *            The y position to check
	 */
	public void calculateCorners(double x, double y) {
		int currTileX = (int) x / tileSize;
		int currTileY = (int) y / tileSize;
		int leftTile = (int) ((x - cwidth / 2 <= 0 ? x - cwidth / 2 - 15 : x - cwidth / 2) / tileSize);
		int rightTile = (int) ((x + cwidth / 2 - 1) / tileSize);
		int topTile = (int) ((y - cheight / 2 <= 0 ? y - cheight / 2 - 15 : y - cheight / 2) / tileSize);
		int bottomTile = (int) ((y + cheight / 2 - 1) / tileSize);
		topLeft = tileMap.getTile(leftTile, topTile).isBottomSolid() || tileMap.getTile(leftTile, topTile).isRightSolid();
		topRight = tileMap.getTile(rightTile, topTile).isBottomSolid() || tileMap.getTile(rightTile, topTile).isLeftSolid();
		bottomLeft = tileMap.getTile(leftTile, bottomTile).isTopSolid() || tileMap.getTile(leftTile, bottomTile).isRightSolid();
		bottomRight = tileMap.getTile(rightTile, bottomTile).isTopSolid() || tileMap.getTile(rightTile, bottomTile).isLeftSolid();

		if (dy != ytemp) {
			if (dy > 0 || tileMap.getTile(currTileX, topTile).isBottomSolid()) {
				tileMap.getTile(currTileX, topTile).onEntityCollision(currTileX, topTile, this, EnumDirection.DOWN);
			}

			if (dy < 0 || tileMap.getTile(currTileX, bottomTile).isTopSolid()) {
				tileMap.getTile(currTileX, bottomTile).onEntityCollision(currTileX, bottomTile, this, EnumDirection.UP);
			}
		}

		if (dx != xtemp) {
			if (dx < 0 || tileMap.getTile(leftTile, currTileY).isRightSolid()) {
				tileMap.getTile(leftTile, currTileY).onEntityCollision(leftTile, currTileY, this, EnumDirection.RIGHT);
			}

			if (dx > 0 || tileMap.getTile(rightTile, currTileY).isLeftSolid()) {
				tileMap.getTile(rightTile, currTileY).onEntityCollision(rightTile, currTileY, this, EnumDirection.LEFT);
			}
		}

		if (dy > 0) {
			tileMap.getTile(leftTile, topTile).onEntityIntersection(leftTile, topTile, this, EnumDirection.DOWN);
			tileMap.getTile(rightTile, topTile).onEntityIntersection(rightTile, topTile, this, EnumDirection.DOWN);
		}

		if (dy < 0) {
			tileMap.getTile(leftTile, bottomTile).onEntityIntersection(leftTile, bottomTile, this, EnumDirection.UP);
			tileMap.getTile(rightTile, bottomTile).onEntityIntersection(rightTile, bottomTile, this, EnumDirection.UP);
		}

		if (dx < 0) {
			tileMap.getTile(leftTile, topTile).onEntityIntersection(leftTile, topTile, this, EnumDirection.RIGHT);
			tileMap.getTile(leftTile, bottomTile).onEntityIntersection(leftTile, bottomTile, this, EnumDirection.RIGHT);
		}

		if (dx > 0) {
			tileMap.getTile(rightTile, topTile).onEntityIntersection(rightTile, topTile, this, EnumDirection.LEFT);
			tileMap.getTile(rightTile, bottomTile).onEntityIntersection(rightTile, bottomTile, this, EnumDirection.LEFT);
		}
	}

	// /**
	// * Whether or not the entity has collided in the specified directions.
	// *
	// * @param x
	// * The x point to check
	// * @param y
	// * The y point to check
	// * @param xa
	// * The new x position
	// * @param ya
	// * The new y position
	// * @param axis
	// * The axis to check
	// * @return Whether or not that position is solid
	// */
	// protected boolean hasCollided(double x, double y, double xa, double ya, EnumAxis axis) {
	// if (xa != 0 && ya != 0) {
	// if (hasCollided(x, y, xa, 0, axis))
	// return true;
	// if (hasCollided(x, y, 0, ya, axis))
	// return true;
	// return false;
	// }
	//
	// boolean xAxis = axis.isVerticalAxis();
	// boolean yAxis = axis.isHorizontalAxis();
	//
	// int nextTileX = (int) (x + xa) / tileSize;
	// int nextTileY = (int) (y + ya) / tileSize;
	// int currTileX = (int) x / tileSize;
	// int currTileY = (int) y / tileSize;
	//
	// List<AxisAlignedBB> collisionBoxes = new ArrayList<AxisAlignedBB>();
	//
	// for (int xx = -1; xx < 2; xx++) {
	// for (int yy = -1; yy < 2; yy++) {
	// int tileX = (xAxis ? currTileX : nextTileX);
	// int tileY = (yAxis ? currTileY : nextTileY);
	// Tile tempTile = tileMap.getTile(tileX, tileY);
	// tempTile.addCollisionBoxToList(this, new AxisAlignedBB(this.entityBox.getX() + tileX, this.entityBox.getY() + tileY, this.entityBox.getWidth(), this.entityBox.getWidth()), collisionBoxes);
	// }
	// }
	//
	// return collisionBoxes.size() > 0;
	// }
	//
	// public void move(double xa, double ya) {
	// if (xa != 0 && ya != 0) {
	// move(xa, 0);
	// move(0, ya);
	// return;
	// }
	//
	// for (int y = 0; y < Math.abs(ya); y++) {
	// if (!this.hasCollided(this.x, this.y, xa, MarioCollisionHelper.abs(ya), EnumAxis.X)) {
	// this.setPosition(this.x, this.y + MarioCollisionHelper.abs(ya));
	// }
	// }
	//
	// for (int x = 0; x < Math.abs(xa); x++) {
	// if (!this.hasCollided(this.x, this.y, MarioCollisionHelper.abs(xa), ya, EnumAxis.Y)) {
	// this.setPosition(this.x + MarioCollisionHelper.abs(xa), this.y);
	// }
	// }
	// }

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

		calculateCorners(x, y + 1);
		if (!falling) {
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}

	/**
	 * Called when the game window loses focus.
	 */
	public void onLoseFocus() {
	}

	/**
	 * @return The entity's game instance
	 */
	public GameTemplate getGame() {
		return game;
	}

	/**
	 * @return The entity's level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @return The entity's x position
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return The entity's y position
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return The entity's last x position
	 */
	public double getLastX() {
		return lastX;
	}

	/**
	 * @return The entity's last y position
	 */
	public double getLastY() {
		return lastY;
	}

	/**
	 * @return The entity's width
	 */
	public double getWidth() {
		return cwidth;
	}

	/**
	 * @return The entity's height
	 */
	public double getHeight() {
		return cheight;
	}

	/**
	 * @return The entity's x speed
	 */
	public double getDx() {
		return dx;
	}

	/**
	 * @return The entity's y speed
	 */
	public double getDy() {
		return dy;
	}

	/**
	 * @return The partial x position to smooth out the x movement
	 */
	public double getPartialRenderX() {
		return (x - lastX) * Minecraft.getMinecraft().getRenderPartialTicks();
	}

	/**
	 * @return The partial y position to smooth out the y movement
	 */
	public double getPartialRenderY() {
		return (y - lastY) * Minecraft.getMinecraft().getRenderPartialTicks();
	}

	/**
	 * @return The tile map x position
	 */
	public double getTileMapX() {
		return tileMap.getLastX() + tileMap.getPartialRenderX();
	}

	/**
	 * @return The tile map y position
	 */
	public double getTileMapY() {
		return tileMap.getLastY() + tileMap.getPartialRenderY();
	}

	/**
	 * @return The entity's collision box
	 */
	public AxisAlignedBB getEntityBox() {
		return new AxisAlignedBB(this.x - this.cwidth / 2, this.y - this.cheight / 2, this.cwidth, this.cheight);
	}

	/**
	 * @return The direction this entity is moving
	 */
	public EnumDirection getMovingDirection() {
		if (falling) {
			if (falling) {
				return EnumDirection.UP;
			} else {
				return EnumDirection.DOWN;
			}
		} else {
			if (dx > 0) {
				return EnumDirection.LEFT;
			} else {
				return EnumDirection.RIGHT;
			}
		}
	}

	/**
	 * Whether or not this entity has collided with the entity provided.
	 * 
	 * @param entity
	 *            The entity to check with
	 * @return Whether or not these two objects have collided
	 */
	public boolean intersects(Entity entity) {
		return this.getEntityBox().intersects(entity.getEntityBox());
	}

	/**
	 * Checks if the entity is on the screen.
	 * 
	 * @return Whether or not the entity is on screen
	 */
	public boolean isOnScreen() {
		return this.x + this.cwidth / 2 >= tileMap.getX() - 16 && this.x - this.cwidth / 2 < tileMap.getX() + game.getWidth() + 16 && this.y - this.cheight / 2 >= tileMap.getY() - 16 && this.y + this.cheight / 2 < tileMap.getY() + game.getHeight() + 16;
	}

	/**
	 * @return If this entity needs to be removed from the level
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Sets the position of this entity to the specified coords.
	 * 
	 * @param x
	 *            The new x position
	 * @param y
	 *            The new y position
	 */
	public Entity setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Sets the x position of this entity to the specified x coord.
	 * 
	 * @param x
	 *            The new x position
	 */
	public Entity setX(double x) {
		this.x = x;
		return this;
	}

	/**
	 * Sets the y position of this entity to the specified y coord.
	 * 
	 * @param y
	 *            The new y position
	 */
	public Entity setY(double y) {
		this.y = y;
		return this;
	}

	/**
	 * Sets the last position of this entity to the specified coords.
	 * 
	 * @param x
	 *            The last x position
	 * @param y
	 *            The last y position
	 */
	public Entity setLastPosition(double x, double y) {
		this.lastX = x;
		this.lastY = y;
		return this;
	}

	/**
	 * Sets the last x position of this entity to the specified x coord.
	 * 
	 * @param x
	 *            The last x position
	 */
	public Entity setLastX(double x) {
		this.lastX = x;
		return this;
	}

	/**
	 * Sets the last y position of this entity to the specified y coord.
	 * 
	 * @param y
	 *            The last y position
	 */
	public Entity setLastY(double y) {
		this.lastY = y;
		return this;
	}

	/**
	 * Sets this entity's width to the specified width
	 * 
	 * @param width
	 *            The new width of the entity
	 */
	protected Entity setWidth(int width) {
		this.cwidth = width;
		return this;
	}

	/**
	 * Sets this entity's height to the specified height
	 * 
	 * @param height
	 *            The new height of the entity
	 */
	protected Entity setHeight(int height) {
		this.cheight = height;
		return this;
	}

	/**
	 * Sets this entity's size to the specified values.
	 * 
	 * @param width
	 *            The new width of the entity
	 * @param height
	 *            The new height of the entity
	 */
	protected Entity setSize(double width, double height) {
		this.cwidth = width;
		this.cheight = height;
		return this;
	}

	/**
	 * Remove this entity the next time it is updated.
	 */
	public Entity setDead() {
		this.dead = true;
		return this;
	}

	/**
	 * Removes the entity if you want it to be dead.
	 * 
	 * @param dead
	 *            Whether or not the entity is dead
	 */
	public Entity setDead(boolean dead) {
		this.dead = dead;
		return this;
	}

	/**
	 * Sets the x direction of the entity to the specified value
	 * 
	 * @param dx
	 *            The new x speed of the entity
	 */
	public Entity setDX(double dx) {
		this.dx = dx;
		return this;
	}

	/**
	 * Sets the y direction of the entity to the specified value
	 * 
	 * @param dy
	 *            The new y speed of the entity
	 */
	public Entity setDY(double dy) {
		this.dy = dy;
		return this;
	}

	/**
	 * Sets the x and y speed of this entity to the specified values.
	 * 
	 * @param dx
	 *            The new x speed of the entity
	 * @param dy
	 *            The new y speed of the entity
	 */
	public Entity setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
		return this;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + x + "," + y + "]";
	}
}