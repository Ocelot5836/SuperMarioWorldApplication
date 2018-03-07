package com.ocelot.mod.game.core.level;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.level.tile.QuestionMarkTile;
import com.ocelot.mod.game.core.level.tile.QuestionMarkTile.ItemType;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A class to hold the tiles in a level.
 * 
 * @author Ocelot5836
 */
public class TileMap implements IResourceManagerReloadListener {

	private double x;
	private double y;

	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	private double tween;

	private ResourceLocation mapLocation;
	private TileStateContainer[] containers;
	private int[] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		this.numRowsToDraw = Game.HEIGHT / tileSize + 2;
		this.numColsToDraw = Game.WIDTH / tileSize + 2;
		this.tween = 1;
	}

	public void loadMap(ResourceLocation mapLocation) {
		this.mapLocation = mapLocation;
		String loadedTile = "null";
		int lastX = 0;
		int lastY = 0;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(mapLocation).getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			this.numCols = Integer.parseInt(br.readLine());
			this.numRows = Integer.parseInt(br.readLine());
			this.map = new int[numCols * numRows];
			this.containers = new TileStateContainer[map.length];
			this.width = numCols * tileSize;
			this.height = numRows * tileSize;

			this.xmin = 0;
			this.xmax = width - Game.WIDTH;
			this.ymin = 0;
			this.ymax = height - Game.HEIGHT;

			for (int y = 0; y < numRows; y++) {
				String line = br.readLine();
				String[] tokens = line.split(" ");
				for (int x = 0; x < numCols; x++) {
					map[x + y * numCols] = Integer.parseInt(tokens[x]);
					containers[x + y * numCols] = getTile(x, y).createContainer();
					loadedTile = tokens[x];
					lastX = y;
					lastY = x;
				}
			}
		} catch (Exception e) {
			Game.stop(e, "Could not load map " + mapLocation + "! Tile errored at " + loadedTile + "-" + lastX + ":" + lastY);
		}
	}

	public void setPosition(double x, double y) {
		if (x < xmin)
			x = xmin;
		if (x > xmax)
			x = xmax;
		if (y < ymin)
			y = ymin;
		if (y > ymax)
			y = ymax;

		this.x += (x - this.x) * this.tween;
		this.y += (y - this.y) * this.tween;

		this.colOffset = (int) this.x / tileSize;
		this.rowOffset = (int) this.y / tileSize;
	}

	public void update() {
		for (int y = rowOffset; y < rowOffset + numRowsToDraw; y++) {
			for (int x = colOffset; x < colOffset + numColsToDraw; x++) {
				if (x >= numCols)
					break;

				if (x > 0 && x < numCols && y > 0 && y < numRows) {
					this.getTile(x, y).setContainer(containers[x + y * numCols]);
				}

				this.getTile(x, y).update();
			}
		}
	}

	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		for (int y = rowOffset - 1; y < rowOffset + numRowsToDraw; y++) {
			for (int x = colOffset - 1; x < colOffset + numColsToDraw; x++) {
				if (x >= numCols)
					break;

				if (!getTile(x, y).shouldRender())
					continue;

				this.getTile(x, y).render((int) -this.x + x * this.tileSize, (int) -this.y + y * this.tileSize, this, gui, mc, mouseX, mouseY, partialTicks);
			}
		}
	}

	public int getTileSize() {
		return tileSize;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || x >= numCols || y < 0 || y >= numRows)
			return Tile.VOID;
		if (Tile.TILES[map[x + y * numCols]] == null)
			return Tile.VOID;
		return Tile.TILES[map[x + y * numCols]];
	}

	public void setTile(int x, int y, Tile tile) {
		if (x < 0 || x >= numCols || y < 0 || y >= numRows)
			return;
		map[x + y * numCols] = tile.getId();
		containers[x + y * numCols] = tile.createContainer();
	}

	public void setValue(int x, int y, IProperty property, Object value) {
		if (x < 0 || x >= numCols || y < 0 || y >= numRows || containers[x + y * numCols] == null)
			return;
		containers[x + y * numCols].setValue(property, value);
	}

	public double getTween() {
		return tween;
	}

	public TileMap setTween(double tween) {
		this.tween = tween;
		return this;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		this.loadMap(this.mapLocation);
	}
}