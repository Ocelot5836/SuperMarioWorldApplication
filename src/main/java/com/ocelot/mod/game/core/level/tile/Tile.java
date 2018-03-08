package com.ocelot.mod.game.core.level.tile;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Lib;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.EnumDir;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.level.tile.InfoBoxTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class Tile {

	/** All of the registered tiles */
	public static final Tile[] TILES = new Tile[256];

	public static final ResourceLocation TILES_LOCATION = new ResourceLocation(Mod.MOD_ID, "textures/tiles.png");
	public static final BufferedImage TILES_SHEET = Lib.loadImage(TILES_LOCATION);

	public static final ResourceLocation CONNECTED_TILES_LOCATION = new ResourceLocation(Mod.MOD_ID, "textures/connected_tiles.png");
	public static final BufferedImage CONNECTED_TILES_SHEET = Lib.loadImage(CONNECTED_TILES_LOCATION);

	private static int nextId = 0;

	public static final Tile AIR = new AirTile(); // 0
	public static final Tile VOID = new VoidTile(); // 1
	public static final Tile TEST_DIRT = new BasicTile(new ItemStack(Blocks.DIRT)).setSolid(); // 2

	public static final Tile YOSHI_HOUSE_GRASS = new BasicTile(new Sprite(TILES_SHEET.getSubimage(0, 0, 16, 16))).setSolid(); // 3
	public static final Tile YOSHI_HOUSE_DIRT = new BasicTile(new Sprite(TILES_SHEET.getSubimage(16, 0, 16, 16))).setSolid(); // 4
	public static final Tile INFO_BOX = new InfoBoxTile(); // 5

	public static final Tile GRASS_TOP_LEFT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(0, 0, 16, 16))).setSolid(); // 6
	public static final Tile GRASS_TOP_MIDDLE = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(16, 0, 16, 16))).setSolid(); // 7
	public static final Tile GRASS_TOP_RIGHT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(32, 0, 16, 16))).setSolid(); // 8
	public static final Tile GRASS_CENTER_LEFT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(0, 16, 16, 16))).setSolid(); // 9
	public static final Tile GRASS_CENTER_MIDDLE = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(16, 16, 16, 16))).setSolid(); // 10
	public static final Tile GRASS_CENTER_RIGHT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(32, 16, 16, 16))).setSolid(); // 11
	public static final Tile GRASS_BOTTOM_LEFT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(0, 32, 16, 16))).setSolid(); // 12
	public static final Tile GRASS_BOTTOM_MIDDLE = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(16, 32, 16, 16))).setSolid(); // 13
	public static final Tile GRASS_BOTTOM_RIGHT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(32, 32, 16, 16))).setSolid(); // 14

	public static final Tile GRASS_WALL_TOP_LEFT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(48, 0, 16, 16))).setTopSolid(); // 15
	public static final Tile GRASS_WALL_TOP_MIDDLE = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(64, 0, 16, 16))).setTopSolid(); // 16
	public static final Tile GRASS_WALL_TOP_RIGHT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(80, 0, 16, 16))).setTopSolid(); // 17
	public static final Tile GRASS_WALL_CENTER_LEFT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(48, 16, 16, 16))); // 18
	public static final Tile GRASS_WALL_CENTER_MIDDLE = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(64, 16, 16, 16))); // 19
	public static final Tile GRASS_WALL_CENTER_RIGHT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(80, 16, 16, 16))); // 20
	public static final Tile GRASS_WALL_BOTTOM_LEFT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(48, 32, 16, 16))).setBottomSolid(); // 21
	public static final Tile GRASS_WALL_BOTTOM_MIDDLE = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(64, 32, 16, 16))).setBottomSolid(); // 22
	public static final Tile GRASS_WALL_BOTTOM_RIGHT = new BasicTile(new Sprite(CONNECTED_TILES_SHEET.getSubimage(80, 32, 16, 16))).setBottomSolid(); // 23

	/** The value that stores all specific data for this block */
	private TileStateContainer container;
	private int id;
	private boolean topSolid;
	private boolean bottomSolid;
	private boolean leftSolid;
	private boolean rightSolid;
	private boolean render;

	public Tile() {
		this(nextId);
		nextId++;
	}

	private Tile(int id) {
		this.id = id;
		if (TILES[id] == null) {
			TILES[id] = this;
		} else {
			throw new RuntimeException("Attempted to register a tile over another. OLD: " + id + ", NEW: " + id);
		}
		this.topSolid = false;
		this.bottomSolid = false;
		this.leftSolid = false;
		this.rightSolid = false;
		this.render = true;
	}

	/**
	 * Updates the tile.
	 */
	public abstract void update();

	/**
	 * Renders the tile.
	 * 
	 * @param x
	 *            The x position of the tile
	 * @param y
	 *            The y position of the tile
	 * @param tileMap
	 *            The tile map this tile is a part of
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
	public abstract void render(int x, int y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);

	/**
	 * Called when an entity collides with this tile.
	 * 
	 * @param x
	 *            The x of the tile
	 * @param y
	 *            The y of the tile
	 * @param entity
	 *            The entity that collided
	 * @param hitDirection
	 *            The direction this block was hit from
	 */
	public void onEntityCollision(int x, int y, Entity entity, EnumDir hitDirection) {
	}

	/**
	 * Called when the tile is added to the tilemap.
	 * 
	 * @param tileMap
	 *            The tilemap instance
	 */
	public void onAdd(TileMap tileMap) {
	}

	/**
	 * Called when the tile is removed from the tilemap.
	 * 
	 * @param tileMap
	 *            The tilemap instance
	 */
	public void onRemove(TileMap tileMap) {
	}

	/**
	 * Creates a new {@link TileStateContainer}. If you have custom properties, you MUST override this method to register them!
	 * 
	 * @return The container created
	 */
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, new IProperty[0]);
	}

	/**
	 * @return This block's container for properties
	 */
	public TileStateContainer getContainer() {
		return container;
	}

	/**
	 * Returns the value for the specified property as specified in the {@link #container}.
	 * 
	 * @param property
	 *            The property to get the value of
	 * @return The value for this property in the {@link #container}. Will throw an exception if it is not registered with this tile
	 * @throws IllegalArgumentException
	 *             If the property is not in the {@link #container}
	 */
	protected Object getValue(IProperty property) {
		return this.container.getValue(property);
	}

	/**
	 * Sets the value for the property in the {@link #container}.
	 * 
	 * @param property
	 *            The property to get the value of
	 * @param value
	 *            The new value for this property. Will throw an exception if it is not registered with this tile
	 * @throws IllegalArgumentException
	 *             If the property is not in the {@link #container}
	 */
	protected void setValue(IProperty property, Object value) {
		this.container.setValue(property, value);
	}

	/**
	 * @return The id for this tile
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return If all four sides are solid
	 */
	public boolean isSolid() {
		return topSolid && bottomSolid && leftSolid && rightSolid;
	}

	/**
	 * @return If the top is solid
	 */
	public boolean isTopSolid() {
		return topSolid;
	}

	/**
	 * @return If the bottom is solid
	 */
	public boolean isBottomSolid() {
		return bottomSolid;
	}

	/**
	 * @return If the left is solid
	 */
	public boolean isLeftSolid() {
		return leftSolid;
	}

	/**
	 * @return If the right is solid
	 */
	public boolean isRightSolid() {
		return rightSolid;
	}

	/**
	 * @return Whether or not this tile should render
	 */
	public boolean shouldRender() {
		return render;
	}

	/**
	 * Sets the container for this tile. Used for proper container mapping.
	 * 
	 * @param container
	 *            The new container
	 */
	public final void setContainer(TileStateContainer container) {
		this.container = container;
	}

	/**
	 * Sets all four sides to be solid.
	 */
	protected Tile setSolid() {
		this.topSolid = true;
		this.bottomSolid = true;
		this.leftSolid = true;
		this.rightSolid = true;
		return this;
	}

	/**
	 * Sets the top to be solid.
	 */
	protected Tile setTopSolid() {
		this.topSolid = true;
		return this;
	}

	/**
	 * Sets the bottom to be solid.
	 */
	protected Tile setBottomSolid() {
		this.bottomSolid = true;
		return this;
	}

	/**
	 * Sets the left to be solid.
	 */
	protected Tile setLeftSolid() {
		this.leftSolid = true;
		return this;
	}

	/**
	 * Sets the right to be solid.
	 */
	protected Tile setRightSolid() {
		this.rightSolid = true;
		return this;
	}

	/**
	 * Sets this tile to not render.
	 */
	protected Tile setShouldNotRender() {
		this.render = false;
		return this;
	}

	@Override
	public String toString() {
		return "Tile:" + this.getId();
	}
}