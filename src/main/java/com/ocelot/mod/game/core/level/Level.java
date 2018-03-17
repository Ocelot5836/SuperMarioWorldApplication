package com.ocelot.mod.game.core.level;

import java.util.List;

import com.google.common.collect.Lists;
import com.ocelot.mod.game.core.entity.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A class that has a tile map as well as entity storage and handling.
 * 
 * @author Ocelot5836
 */
public class Level {

	private TileMap tileMap;
	private List<Entity> entities = Lists.<Entity>newArrayList();

	/**
	 * Creates a new level with the specified map.
	 * 
	 * @param tileSize
	 *            The size of each tile
	 * @param mapLocation
	 *            The location of the map file
	 */
	public Level(int tileSize, ResourceLocation mapLocation) {
		this.tileMap = new TileMap(tileSize);
		this.tileMap.loadMap(mapLocation);
	}

	/**
	 * Updates the tilemap as well as the entities.
	 */
	public void update() {
		tileMap.update();

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			if (e.isDead()) {
				entities.remove(e);
				i--;
			}
		}
	}

	/**
	 * Renders the tiles and entities to the screen.
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
		this.tileMap.render(gui, mc, mouseX, mouseY, partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate((int) tileMap.getX(), (int) tileMap.getY(), 0);
		this.renderEntities(gui, mc, mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();
	}

	private void renderEntities(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(gui, mc, mouseX, mouseY, partialTicks);
		}
	}

	/**
	 * Called when the window loses focus.
	 */
	public void onLoseFocus() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onLoseFocus();
		}
	}

	/**
	 * Called when a key is pressed.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyPressed(int keyCode, char typedChar) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onKeyPressed(keyCode, typedChar);
		}
	}

	/**
	 * Called when a key is released.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyReleased(int keyCode, char typedChar) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onKeyReleased(keyCode, typedChar);
		}
	}

	/**
	 * Adds an entity to the level.
	 * 
	 * @param entity
	 *            The entity to add
	 */
	public void add(Entity entity) {
		entity.init(this);
		entities.add(entity);
	}

	/**
	 * @return The tilemap.
	 */
	public TileMap getMap() {
		return tileMap;
	}
}