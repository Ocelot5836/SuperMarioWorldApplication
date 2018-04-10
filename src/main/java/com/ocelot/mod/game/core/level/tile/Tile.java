package com.ocelot.mod.game.core.level.tile;

import java.awt.image.BufferedImage;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.main.level.tile.GrassWallTile;
import com.ocelot.mod.game.main.level.tile.InfoBoxTile;
import com.ocelot.mod.game.main.level.tile.TileCoin;
import com.ocelot.mod.game.main.level.tile.QuestionBlockTile;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic block that can be placed into a level that renders a sprite and entities collide with it.
 * 
 * @author Ocelot5836
 */
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
	public static final Tile COIN = new TileCoin(); // 6
	public static final Tile GRASS = new ConnectedTile(CONNECTED_TILES_SHEET.getSubimage(0, 0, 48, 48)).setSolid(); // 7
	public static final Tile GRASS_WALL = new GrassWallTile(); // 8
	public static final Tile QUESTION_BLOCK = new QuestionBlockTile(); // 9

	/**
	 * The class that stores all specific data and data properties for this tile. It has an unlimited amount of storage for properties and can be used to make advanced tiles.
	 * 
	 * @see TileStateContainer
	 */
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
	 * @param d
	 *            The x position of the tile
	 * @param e
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
	public abstract void render(double d, double e, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);

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
	public void onEntityCollision(int x, int y, Entity entity, EnumDirection hitDirection) {
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
	 * Called to modify any properties in the tileStateContainer.
	 * 
	 * @param tileMap
	 *            The tilemap instance
	 * @param container
	 *            The container that is being modified
	 */
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		return container;
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
		return this.container == null ? null : this.container.getValue(property);
	}

	/**
	 * @return The id for this tile
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The name before it is localized
	 */
	public String getUnlocalizedName() {
		return "tile." + Mod.MOD_ID + "." + this.id + ".name";
	}

	/**
	 * @return The name of the tile converted to the proper language
	 */
	public String getLocalizedName() {
		return I18n.format(this.getUnlocalizedName());
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
	 * Sets all four sides to not be solid.
	 */
	protected Tile setPassable() {
		this.topSolid = false;
		this.bottomSolid = false;
		this.leftSolid = false;
		this.rightSolid = false;
		return this;
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
		if (this.container != null) {
			this.container.setValue(property, value);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tile) {
			Tile tile = (Tile) obj;
			return tile.getId() == this.getId();
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Tile[" + this.getLocalizedName() + "/" + this.getUnlocalizedName() + ":" + this.getId() + "]";
	}
}