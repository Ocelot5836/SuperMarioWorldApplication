package com.ocelot.mod.game.core.level.tile;

import java.awt.image.BufferedImage;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.property.PropertyBoolean;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows a tile's sprite to link with those around them that are the same tile.
 * 
 * @author Ocelot5836
 */
public class BasicConnectedTile extends BasicTile {

	/** Whether or not the above tile is this one */
	public static final PropertyBoolean UP = PropertyBoolean.create("up");
	/** Whether or not the below tile is this one */
	public static final PropertyBoolean DOWN = PropertyBoolean.create("down");
	/** Whether or not the left tile is this one */
	public static final PropertyBoolean LEFT = PropertyBoolean.create("left");
	/** Whether or not the right tile is this one */
	public static final PropertyBoolean RIGHT = PropertyBoolean.create("right");

	protected Sprite[] sprites;

	/**
	 * The harder constructor.
	 * 
	 * @param sprites
	 *            The sprites in order of TL, TM, TR, ML, MM, MR, BL, BM, BR. There MUST be at least 9 sprites in the array!
	 */
	public BasicConnectedTile(String unlocalizedName, Sprite... sprites) {
		super(unlocalizedName);
		this.sprites = new Sprite[9];

		if (sprites.length < this.sprites.length) {
			throw new IllegalArgumentException("Sprites for connected tile " + this.getLocalizedName() + " are not valid.");
		}

		for (int i = 0; i < this.sprites.length; i++) {
			this.sprites[i] = sprites[i];
		}
	}

	/**
	 * The easier of the two constructors because it automatically adds the sprites.
	 * 
	 * @param sheet
	 *            The sheet is the 9 sprites linked as if they were a 3x3 in the world
	 */
	public BasicConnectedTile(String unlocalizedName, BufferedImage sheet) {
		super(unlocalizedName);
		this.sprites = new Sprite[9];
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				sprites[x + y * 3] = new Sprite(sheet.getSubimage(x * 16, y * 16, 16, 16));
			}
		}
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		boolean up = (boolean) container.getValue(UP);
		boolean down = (boolean) container.getValue(DOWN);
		boolean left = (boolean) container.getValue(LEFT);
		boolean right = (boolean) container.getValue(RIGHT);

		if (tileMap.getTile(x, y - 1).equals(this) || tileMap.getTile(x, y - 1).equals(Tile.VOID)) {
			if (!up) {
				container.setValue(UP, true);
			}
		} else {
			if (up) {
				container.setValue(UP, false);
			}
		}

		if (tileMap.getTile(x, y + 1).equals(this) || tileMap.getTile(x, y + 1).equals(Tile.VOID)) {
			if (!down) {
				container.setValue(DOWN, true);
			}
		} else {
			if (down) {
				container.setValue(DOWN, false);
			}
		}

		if (tileMap.getTile(x - 1, y).equals(this) || tileMap.getTile(x - 1, y).equals(Tile.VOID)) {
			if (!left) {
				container.setValue(LEFT, true);
			}
		} else {
			if (left) {
				container.setValue(LEFT, false);
			}
		}

		if (tileMap.getTile(x + 1, y).equals(this) || tileMap.getTile(x + 1, y).equals(Tile.VOID)) {
			if (!right) {
				container.setValue(RIGHT, true);
			}
		} else {
			if (right) {
				container.setValue(RIGHT, false);
			}
		}

		return container;
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (getValue(UP) != null && getValue(DOWN) != null && getValue(LEFT) != null && getValue(RIGHT) != null) {
			boolean up = (boolean) getValue(UP);
			boolean down = (boolean) getValue(DOWN);
			boolean left = (boolean) getValue(LEFT);
			boolean right = (boolean) getValue(RIGHT);

			if (up && down) {
				if (left && right) {
					sprites[4].render(x, y, 16, 16);
				} else if (left) {
					sprites[5].render(x, y, 16, 16);
				} else if (right) {
					sprites[3].render(x, y, 16, 16);
				}
			} else if (up) {
				if (left && right) {
					sprites[7].render(x, y, 16, 16);
				} else if (left) {
					sprites[8].render(x, y, 16, 16);
				} else if (right) {
					sprites[6].render(x, y, 16, 16);
				}
			} else if (down) {
				if (left && right) {
					sprites[1].render(x, y, 16, 16);
				} else if (left) {
					sprites[2].render(x, y, 16, 16);
				} else if (right) {
					sprites[0].render(x, y, 16, 16);
				}
			} else {
				sprites[1].render(x, y, 16, 16);
			}
		} else {
			sprites[1].render(x, y, 16, 16);
		}
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, UP, DOWN, LEFT, RIGHT);
	}
}