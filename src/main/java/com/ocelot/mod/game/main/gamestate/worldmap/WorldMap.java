package com.ocelot.mod.game.main.gamestate.worldmap;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.player.PlayerMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class WorldMap {

	private PlayerMap player;
	private List<IWorldMapPoint> points;
	private List<WorldMapPath> paths;

	protected WorldMap(GameTemplate game) {
		this.player = new PlayerMap(game, this);
		this.points = new ArrayList<IWorldMapPoint>();
		this.paths = new ArrayList<WorldMapPath>();
	}

	protected WorldMap(GameTemplate game, ResourceLocation folder) {
		this.player = new PlayerMap(game, this);
		this.points = new ArrayList<IWorldMapPoint>();
		this.paths = new ArrayList<WorldMapPath>();
	}

	public void update() {
		for (int i = 0; i < points.size(); i++) {
			this.points.get(i).update();
		}
		for (int i = 0; i < paths.size(); i++) {
			this.paths.get(i).update();
		}
		this.player.update();
	}

	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < points.size(); i++) {
			this.points.get(i).render(16, 16, gui, mc, mouseX, mouseY, partialTicks);
		}
		for (int i = 0; i < paths.size(); i++) {
			this.paths.get(i).render(gui, mc, mouseX, mouseY, partialTicks);
		}
		this.player.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	public void onKeyPressed(int keyCode, char typedChar) {
		this.player.onKeyPressed(keyCode, typedChar);
	}

	public void onKeyReleased(int keyCode, char typedChar) {
		this.player.onKeyReleased(keyCode, typedChar);
	}

	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		this.player.onMousePressed(mouseButton, mouseX, mouseY);
	}

	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
		this.player.onMouseReleased(mouseButton, mouseX, mouseY);
	}

	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
		this.player.onMouseScrolled(direction, mouseX, mouseY);
	}

	protected void clear() {
		this.points.clear();
		this.paths.clear();
	}

	protected void addPoint(IWorldMapPoint point) {
		this.points.add(point);
	}

	protected void mapPath(Sprite sprite, Vector2f... positions) {
		this.paths.add(new WorldMapPath(sprite, positions));
	}

	public PlayerMap getPlayer() {
		return player;
	}
}