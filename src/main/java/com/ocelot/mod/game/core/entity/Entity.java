package com.ocelot.mod.game.core.entity;

import java.awt.Rectangle;

import com.ocelot.mod.game.core.EnumDir;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class Entity {

	protected GameTemplate game;
	protected Level level;
	protected TileMap tileMap;
	protected int tileSize;
	protected double x, y;

	protected int cwidth, cheight;
	protected boolean topLeft, topRight, bottomLeft, bottomRight;
	protected double xmap;
	protected double ymap;

	protected int currCol;
	protected int currRow;

	protected double dx;
	protected double dy;

	private boolean dead;

	public Entity(GameTemplate game) {
		this.game = game;
	}

	public void init(Level level) {
		this.level = level;
		this.tileMap = level.getMap();
		this.tileSize = level.getMap().getTileSize();
	}

	public void update() {
	}

	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.setMapPosition();
	}

	protected void calculateCorners(double x, double y) {
		int currTileX = (int) x / tileSize;
		int currTileY = (int) y / tileSize;
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		topLeft = tileMap.getTile(leftTile, topTile).isBottomSolid() || tileMap.getTile(leftTile, topTile).isLeftSolid();
		topRight = tileMap.getTile(rightTile, topTile).isBottomSolid() || tileMap.getTile(rightTile, topTile).isRightSolid();
		bottomLeft = tileMap.getTile(leftTile, bottomTile).isTopSolid() || tileMap.getTile(leftTile, bottomTile).isLeftSolid();
		bottomRight = tileMap.getTile(rightTile, bottomTile).isTopSolid() || tileMap.getTile(rightTile, bottomTile).isRightSolid();

		if (dy != 0) {
			if (tileMap.getTile(currTileX, topTile).isBottomSolid()) {
				tileMap.getTile(currTileX, topTile).onEntityCollision(currTileX, topTile, this, EnumDir.DOWN);
			}

//			if (tileMap.getTile(currTileX, bottomTile).isTopSolid()) {
//				tileMap.getTile(currTileX, bottomTile).onEntityCollision(currTileX, bottomTile, this, EnumDir.UP);
//			}
		}

		if (dx != 0) {
			if (tileMap.getTile(leftTile, currTileY).isRightSolid()) {
				tileMap.getTile(leftTile, currTileY).onEntityCollision(leftTile, currTileY, this, EnumDir.RIGHT);
			}

			if (tileMap.getTile(rightTile, currTileY).isLeftSolid()) {
				tileMap.getTile(rightTile, currTileY).onEntityCollision(rightTile, currTileY, this, EnumDir.LEFT);
			}
		}

		// tileMap.getTile(leftTile, topTile).onEntityCollision(leftTile, topTile, this, EnumDir.DOWN);
		// tileMap.getTile(leftTile, topTile).onEntityCollision(leftTile, topTile, this, EnumDir.LEFT);
		// tileMap.getTile(rightTile, topTile).onEntityCollision(rightTile, topTile, this, EnumDir.DOWN);
		// tileMap.getTile(rightTile, topTile).onEntityCollision(rightTile, topTile, this, EnumDir.RIGHT);
		// tileMap.getTile(leftTile, bottomTile).onEntityCollision(leftTile, bottomTile, this, EnumDir.UP);
		// tileMap.getTile(leftTile, bottomTile).onEntityCollision(leftTile, bottomTile, this, EnumDir.LEFT);
		// tileMap.getTile(rightTile, bottomTile).onEntityCollision(rightTile, bottomTile, this, EnumDir.UP);
		// tileMap.getTile(rightTile, bottomTile).onEntityCollision(rightTile, bottomTile, this, EnumDir.LEFT);
	}

	public GameTemplate getGame() {
		return game;
	}

	public Level getLevel() {
		return level;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return cwidth;
	}

	public int getHeight() {
		return cheight;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public boolean intersects(Entity e) {
		return this.getCollisionBox().intersects(e.getCollisionBox());
	}

	public Rectangle getCollisionBox() {
		return new Rectangle((int) x, (int) y, cwidth, cheight);
	}

	public boolean isDead() {
		return dead;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	protected void setWidth(int cwidth) {
		this.cwidth = cwidth;
	}

	protected void setHeight(int cheight) {
		this.cheight = cheight;
	}

	protected void setSize(int width, int height) {
		this.cwidth = width;
		this.cheight = height;
	}

	public void setDead() {
		this.dead = true;
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

	protected void setMapPosition() {
		xmap = level.getMap().getX();
		ymap = level.getMap().getY();
	}
}