package com.ocelot.mod.game.main.gamestate.worldmap;

import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.game.core.gfx.Sprite;

public class WorldMapIcon {

	private WorldMapState map;
	private WorldMapIcon parent;
	private List<WorldMapIcon> children;
	private Runnable onEnter;
	private Sprite sprite;
	private double x;
	private double y;
	private int width;
	private int height;

	public WorldMapIcon(WorldMapState map, Sprite sprite, double x, double y, Runnable onEnter) {
		this(map, sprite, x, y, sprite.getWidth(), sprite.getHeight(), onEnter);
	}

	public WorldMapIcon(WorldMapState map, Sprite sprite, double x, double y, int width, int height, Runnable onEnter) {
		this.map = map;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.onEnter = onEnter;
		this.children = new ArrayList<WorldMapIcon>();
	}

	public void render() {
		sprite.render(x - width / 2, y - height / 2, width, height);
	}

	public void onEnter() {
		onEnter.run();
	}

	public WorldMapIcon getNearestIcon() {
		return getNearestIcon(x, y);
	}

	public WorldMapIcon getNearestIcon(double x, double y) {
		if (children.size() <= 0)
			return null;

		WorldMapIcon nearest = children.get(0);
		for (int i = 0; i < children.size(); i++) {
			WorldMapIcon icon = children.get(i);
			if (x - icon.getX() >= nearest.getX() && y - icon.getY() >= nearest.getY()) {
				nearest = icon;
			}
		}
		return nearest;
	}

	public WorldMapIcon getParent() {
		return parent;
	}
	
	public List<WorldMapIcon> getChildren() {
		return new ArrayList<WorldMapIcon>(children);
	}

	public Sprite getSprite() {
		return sprite;
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

	public WorldMapIcon addChildren(WorldMapIcon... children) {
		for (int i = 0; i < children.length; i++) {
			children[i].parent = this;
			this.children.add(children[i]);
		}
		map.addIcons(children);
		return this;
	}
}