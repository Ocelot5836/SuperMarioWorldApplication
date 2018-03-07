package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.EnumDir;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class Tile {

	public static final Tile[] TILES = new Tile[256];

	public static final ResourceLocation YOSHI_HOUSE_LOCATION = new ResourceLocation(Mod.MOD_ID, "textures/tilesets/yoshi_house/tiles.png");
	public static final ResourceLocation MISC_LOCATION = new ResourceLocation(Mod.MOD_ID, "textures/tilesets/misc.png");
	public static final ResourceLocation GRASS_LOCATION = new ResourceLocation(Mod.MOD_ID, "textures/tilesets/ground/grass.png");
	public static final ResourceLocation GRASS_MISC_LOCATION = new ResourceLocation(Mod.MOD_ID, "textures/tilesets/ground/misc.png");

	private static int nextId = 0;

	public static final Tile AIR = new AirTile(); // 0
	public static final Tile VOID = new VoidTile(); // 1
	public static final Tile TEST_DIRT = new BasicTile(new ItemStack(Blocks.DIRT)).setSolid(); // 2

	public static final Tile YOSHI_HOUSE_GRASS = new BasicTile(new Sprite(YOSHI_HOUSE_LOCATION, 0, 0, 16, 16)).setSolid(); // 3
	public static final Tile YOSHI_HOUSE_DIRT = new BasicTile(new Sprite(YOSHI_HOUSE_LOCATION, 16, 0, 16, 16)).setSolid(); // 4

	public static final Tile GRASS_TOP_LEFT = new BasicTile(new Sprite(GRASS_LOCATION, 0, 0, 16, 16)).setSolid(); // 5
	public static final Tile GRASS_TOP_MIDDLE = new BasicTile(new Sprite(GRASS_LOCATION, 16, 0, 16, 16)).setSolid(); // 6
	public static final Tile GRASS_TOP_RIGHT = new BasicTile(new Sprite(GRASS_LOCATION, 32, 0, 16, 16)).setSolid(); // 7
	public static final Tile GRASS_CENTER_LEFT = new BasicTile(new Sprite(GRASS_LOCATION, 0, 16, 16, 16)).setSolid(); // 8
	public static final Tile GRASS_CENTER_MIDDLE = new BasicTile(new Sprite(GRASS_LOCATION, 16, 16, 16, 16)).setSolid(); // 9
	public static final Tile GRASS_CENTER_RIGHT = new BasicTile(new Sprite(GRASS_LOCATION, 32, 16, 16, 16)).setSolid(); // 10
	public static final Tile GRASS_BOTTOM_LEFT = new BasicTile(new Sprite(GRASS_LOCATION, 0, 32, 16, 16)).setSolid(); // 11
	public static final Tile GRASS_BOTTOM_MIDDLE = new BasicTile(new Sprite(GRASS_LOCATION, 16, 32, 16, 16)).setSolid(); // 12
	public static final Tile GRASS_BOTTOM_RIGHT = new BasicTile(new Sprite(GRASS_LOCATION, 32, 32, 16, 16)).setSolid(); // 13

	public static final Tile GRASS_WALL_TOP_LEFT = new BasicTile(new Sprite(GRASS_LOCATION, 48, 0, 16, 16)).setTopSolid(); // 14
	public static final Tile GRASS_WALL_TOP_MIDDLE = new BasicTile(new Sprite(GRASS_LOCATION, 64, 0, 16, 16)).setTopSolid(); // 15
	public static final Tile GRASS_WALL_TOP_RIGHT = new BasicTile(new Sprite(GRASS_LOCATION, 80, 0, 16, 16)).setTopSolid(); // 16
	public static final Tile GRASS_WALL_CENTER_LEFT = new BasicTile(new Sprite(GRASS_LOCATION, 48, 16, 16, 16)); // 17
	public static final Tile GRASS_WALL_CENTER_MIDDLE = new BasicTile(new Sprite(GRASS_LOCATION, 64, 16, 16, 16)); // 18
	public static final Tile GRASS_WALL_CENTER_RIGHT = new BasicTile(new Sprite(GRASS_LOCATION, 80, 16, 16, 16)); // 19
	public static final Tile GRASS_WALL_BOTTOM_LEFT = new BasicTile(new Sprite(GRASS_LOCATION, 48, 32, 16, 16)).setBottomSolid(); // 20
	public static final Tile GRASS_WALL_BOTTOM_MIDDLE = new BasicTile(new Sprite(GRASS_LOCATION, 64, 32, 16, 16)).setBottomSolid(); // 21
	public static final Tile GRASS_WALL_BOTTOM_RIGHT = new BasicTile(new Sprite(GRASS_LOCATION, 80, 32, 16, 16)).setBottomSolid(); // 22

	public static final Tile INFO_BOX = new InfoBoxTile(); // 23

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

	public abstract void update();

	public abstract void render(int x, int y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);

	public void onEntityCollision(int x, int y, Entity entity, EnumDir hitDirection) {
	}
	
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, new IProperty[0]);
	}

	public TileStateContainer getContainer() {
		return container;
	}

	protected Object getValue(IProperty property) {
		return this.container.getValue(property);
	}

	protected void setValue(IProperty property, Object value) {
		this.container.setValue(property, value);
	}

	public int getId() {
		return id;
	}

	public boolean isSolid() {
		return topSolid && bottomSolid && leftSolid && rightSolid;
	}

	public boolean isTopSolid() {
		return topSolid;
	}

	public boolean isBottomSolid() {
		return bottomSolid;
	}

	public boolean isLeftSolid() {
		return leftSolid;
	}

	public boolean isRightSolid() {
		return rightSolid;
	}

	public boolean shouldRender() {
		return render;
	}

	public void setContainer(TileStateContainer container) {
		this.container = container;
	}

	protected Tile setSolid() {
		this.topSolid = true;
		this.bottomSolid = true;
		this.leftSolid = true;
		this.rightSolid = true;
		return this;
	}

	protected Tile setTopSolid() {
		this.topSolid = true;
		return this;
	}

	protected Tile setBottomSolid() {
		this.bottomSolid = true;
		return this;
	}

	protected Tile setLeftSolid() {
		this.leftSolid = true;
		return this;
	}

	protected Tile setRightSolid() {
		this.rightSolid = true;
		return this;
	}

	protected Tile setShouldNotRender() {
		this.render = false;
		return this;
	}

	@Override
	public String toString() {
		return "Tile:" + this.getId();
	}
}