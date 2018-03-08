package com.ocelot.mod.game.core.entity;

import java.awt.Rectangle;

import com.ocelot.mod.game.core.EnumDir;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.TileMap;

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

	/** The width of this entity */
	protected int cwidth;
	/** The height of this entity */
	protected int cheight;
	/** Collision variables */ // TODO redo collisions so they are more flexible
	protected boolean topLeft, topRight, bottomLeft, bottomRight;
	/**
	 * The x position in the map
	 * 
	 * @deprecated This is not a good way to declare my coords. Will be removed.
	 */
	protected double xmap;
	/**
	 * The y position in the map
	 * 
	 * @deprecated This is not a good way to declare my coords. Will be removed.
	 */
	protected double ymap;

	/** The current collumn this entity is in */
	protected int currCol;
	/** The current row this entity is in */
	protected int currRow;

	/** The x speed */
	protected double dx;
	/** The y speed */
	protected double dy;

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
	 * Renders the tntity to the screen.
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
		this.setMapPosition();
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
	 * Updates the {@link #topLeft}, {@link #topRight}, {@link #bottomLeft}, and {@link #bottomRight} variables with this entity's current position. Also notifies tiles of when the entity collides with it.
	 * 
	 * @param x
	 *            The x position to check
	 * @param y
	 *            The y position to check
	 */
	protected void calculateCorners(double x, double y) {
		int currTileX = (int) x / tileSize;
		int currTileY = (int) y / tileSize;
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		topLeft = tileMap.getTile(leftTile, topTile).isBottomSolid() || tileMap.getTile(leftTile, topTile).isRightSolid();
		topRight = tileMap.getTile(rightTile, topTile).isBottomSolid() || tileMap.getTile(rightTile, topTile).isLeftSolid();
		bottomLeft = tileMap.getTile(leftTile, bottomTile).isTopSolid() || tileMap.getTile(leftTile, bottomTile).isRightSolid();
		bottomRight = tileMap.getTile(rightTile, bottomTile).isTopSolid() || tileMap.getTile(rightTile, bottomTile).isLeftSolid();

		if (dy != 0) {
			if (tileMap.getTile(currTileX, topTile).isBottomSolid()) {
				tileMap.getTile(currTileX, topTile).onEntityCollision(currTileX, topTile, this, EnumDir.DOWN);
			}

			// if (tileMap.getTile(currTileX, bottomTile).isTopSolid()) {
			// tileMap.getTile(currTileX, bottomTile).onEntityCollision(currTileX, bottomTile, this, EnumDir.UP);
			// }
		}

		if (dx != 0) {
			if (tileMap.getTile(leftTile, currTileY).isRightSolid()) {
				tileMap.getTile(leftTile, currTileY).onEntityCollision(leftTile, currTileY, this, EnumDir.RIGHT);
			}

			if (tileMap.getTile(rightTile, currTileY).isLeftSolid()) {
				tileMap.getTile(rightTile, currTileY).onEntityCollision(rightTile, currTileY, this, EnumDir.LEFT);
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
	 * @return The entity's width
	 */
	public int getWidth() {
		return cwidth;
	}

	/**
	 * @return The entity's height
	 */
	public int getHeight() {
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
	 * Whether or not this entity has collided with the entity provided.
	 * 
	 * @param entity
	 *            The entity to check with
	 * @return Whether or not these two objects have collided
	 */
	public boolean intersects(Entity entity) {
		return this.getCollisionBox().intersects(entity.getCollisionBox());
	}

	/**
	 * @deprecated This is a very bad way of doing collisions. Try not to use it.
	 * 
	 * @return The x, y, width, and height put into a rectangle
	 */
	public Rectangle getCollisionBox() {
		return new Rectangle((int) x, (int) y, cwidth, cheight);
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
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the x position of this entity to the specified x coord.
	 * 
	 * @param x
	 *            The new x position
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y position of this entity to the specified y coord.
	 * 
	 * @param y
	 *            The new y position
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets this entity's width to the specified width
	 * 
	 * @param width
	 *            The new width of the entity
	 */
	protected void setWidth(int width) {
		this.cwidth = width;
	}

	/**
	 * Sets this entity's height to the specified height
	 * 
	 * @param height
	 *            The new height of the entity
	 */
	protected void setHeight(int height) {
		this.cheight = height;
	}

	/**
	 * Sets this entity's size to the specified values.
	 * 
	 * @param width
	 *            The new width of the entity
	 * @param height
	 *            The new height of the entity
	 */
	protected void setSize(int width, int height) {
		this.cwidth = width;
		this.cheight = height;
	}

	/**
	 * Remove this entity the next time it is updated.
	 */
	public void setDead() {
		this.dead = true;
	}

	/**
	 * Sets the x direction of the entity to the specified value
	 * 
	 * @param dx
	 *            The new x speed of the entity
	 */
	public void setDX(double dx) {
		this.dx = dx;
	}

	/**
	 * Sets the y direction of the entity to the specified value
	 * 
	 * @param dy
	 *            The new y speed of the entity
	 */
	public void setDY(double dy) {
		this.dy = dy;
	}

	/**
	 * Sets the x and y speed of this entity to the specified values.
	 * 
	 * @param dx
	 *            The new x speed of the entity
	 * @param dy
	 *            The new y speed of the entity
	 */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Sets the x and y map position of the entity.
	 * 
	 * @deprecated This is pretty much useless and will be removed.
	 */
	protected void setMapPosition() {
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
}