package com.ocelot.mod.game.core.entity.fx;

import java.util.Random;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.Tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic effect that can be added and rendered in a level. This is not considered an entity because it is simply a fun effect that is rendered then removed afterwards.
 * 
 * @author Ocelot5836
 */
public abstract class EntityFX {

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
	/** The collision width */
	protected int cwidth;
	/** The collision height */
	protected int cheight;
	/** A random instance */
	protected Random random;

	private boolean dead;

	public EntityFX(GameTemplate game) {
		this.game = game;
		this.random = new Random();
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
	 * Called 20 times per second. Updates the effect's logic if it has any.
	 */
	public void update() {
		this.lastX = x;
		this.lastY = y;
	}

	/**
	 * Renders the effect to the screen.
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
	 * Called when the game window loses focus.
	 */
	public void onLoseFocus() {
	}

	/**
	 * Checks if an effect has collided with a tile.
	 * 
	 * @param x
	 *            The x position to check
	 * @param y
	 *            The y position to check
	 * @param width
	 *            The width to check
	 * @param height
	 *            The height to check
	 * 
	 * @return Whether or not the effect has collided with a tile
	 */
	protected boolean hasCollided(double x, double y, int width, int height) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = (int) (((x - width / 2) - c % 2 * (width / 2)) / tileSize);
			int yt = (int) (((y - height / 2) - c / 2 * (height / 2)) / tileSize);
			Tile tile = tileMap.getTile(xt, yt);
			if (tile.isLeftSolid() || tile.isRightSolid() || tile.isTopSolid() || tile.isBottomSolid()) {
				solid = true;
			}
		}
		return solid;
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
	 * @return The collision width
	 */
	public int getWidth() {
		return cwidth;
	}

	/**
	 * @return The collision height
	 */
	public int getHeight() {
		return cheight;
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
	 * @return If this entity needs to be removed from the level
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Checks if the effect is on the screen.
	 * 
	 * @return Whether or not the effect is on screen
	 */
	public boolean isOnScreen() {
		return this.x >= tileMap.getX() && this.x < tileMap.getX() + tileMap.getWidth() && this.y >= tileMap.getY() && this.y < tileMap.getY() + tileMap.getHeight();
		// return true;
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
	 * Sets the position of this entity to the specified coords.
	 * 
	 * @param width
	 *            The new collision width
	 * @param height
	 *            The new collision height
	 */
	public void setSize(int width, int height) {
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
	 * Removes the entity if you want it to be dead.
	 * 
	 * @param dead
	 *            Whether or not the entity is dead
	 */
	public EntityFX setDead(boolean dead) {
		this.dead = dead;
		return this;
	}
}