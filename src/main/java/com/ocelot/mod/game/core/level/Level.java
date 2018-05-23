package com.ocelot.mod.game.core.level;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.entity.IPlayerDamager;
import com.ocelot.mod.game.core.entity.fx.EntityFX;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;
import com.ocelot.mod.lib.MemoryLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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

	private LevelProperties properties;

	private TileMap tileMap;
	private List<EntityFX> effects = Lists.<EntityFX>newArrayList();
	private List<Entity> entities = Lists.<Entity>newArrayList();
	private List<Player> players = Lists.<Player>newArrayList();

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
	 * Updates the tile map as well as the entities.
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

		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			p.update();
			if (p.isDead()) {
				players.remove(p);
				i--;
			}
		}

		for (int i = 0; i < effects.size(); i++) {
			EntityFX e = effects.get(i);
			e.update();
			if (e.isDead()) {
				effects.remove(e);
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
		this.renderEntities(gui, mc, mouseX, mouseY, partialTicks);
	}

	private void renderEntities(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		boolean showCollisionBoxes = true;

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.isOnScreen()) {
				e.render(gui, mc, mouseX, mouseY, partialTicks);
			}
			if (showCollisionBoxes) {
				int color = 0xff00ff00;
				if (e instanceof IPlayerDamager && e instanceof IPlayerDamagable) {
					color = 0xff0000ff;
				} else if (e instanceof IPlayerDamager) {
					color = 0xffff0000;
				}
				Lib.drawCollisionBox(e, color);
			}
		}

		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if (p.isOnScreen()) {
				p.render(gui, mc, mouseX, mouseY, partialTicks);
			}
			if (showCollisionBoxes) {
				Lib.drawCollisionBox(p, 0xff00ff00);
			}
		}

		for (int i = 0; i < effects.size(); i++) {
			EntityFX e = effects.get(i);
			if (e.isOnScreen()) {
				e.render(gui, mc, mouseX, mouseY, partialTicks);
			}
		}
	}

	/**
	 * Called when the window loses focus.
	 */
	public void onLoseFocus() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onLoseFocus();
		}

		for (int i = 0; i < players.size(); i++) {
			players.get(i).onLoseFocus();
		}

		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).onLoseFocus();
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

		for (int i = 0; i < players.size(); i++) {
			players.get(i).onKeyPressed(keyCode, typedChar);
		}

		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).onKeyPressed(keyCode, typedChar);
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

		for (int i = 0; i < players.size(); i++) {
			players.get(i).onKeyReleased(keyCode, typedChar);
		}

		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).onKeyReleased(keyCode, typedChar);
		}
	}

	/**
	 * Adds an entity to the level.
	 * 
	 * @param entity
	 *            The entity to add
	 */
	public void add(Entity entity) {
		if (entity == null)
			return;
		entity.init(this);
		if (entity instanceof Player) {
			players.add((Player) entity);
		} else {
			entities.add(entity);
		}
	}

	/**
	 * Adds an effect to the level.
	 * 
	 * @param effect
	 *            The entity to add
	 */
	public void add(EntityFX effect) {
		effect.init(this);
		effects.add(effect);
	}

	/**
	 * @return The tilemap.
	 */
	public TileMap getMap() {
		return tileMap;
	}

	/**
	 * Searches for the nearest entity that is not the checker at a point.
	 * 
	 * @param checker
	 *            The entity to check
	 * @return The nearest entity that is not the checker to that point
	 */
	@Nullable
	public Entity getNearestEntity(Entity checker) {
		Comparator<Entity> comparator = MemoryLib.GET_NEAREST_ENTITY_ENTITY.get(checker.getX() + "," + checker.getY());
		if (comparator == null) {
			comparator = new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					int dist1 = (int) Math.sqrt(Math.pow(checker.getX() - e1.getX(), 2) + Math.pow(checker.getY() - e1.getY(), 2));
					int dist2 = (int) Math.sqrt(Math.pow(checker.getX() - e2.getX(), 2) + Math.pow(checker.getY() - e2.getY(), 2));

					if (checker == e1 || checker == e2)
						return -1;
					if (dist1 < dist2)
						return -1;
					if (dist1 > dist2)
						return 1;
					return 0;
				}
			};
			MemoryLib.GET_NEAREST_ENTITY_ENTITY.put(checker.getX() + "," + checker.getY(), comparator);
		}
		return entities.size() > 0 ? Collections.min(entities, comparator) : null;
	}

	/**
	 * Searches for the nearest entity at a point.
	 * 
	 * @param x
	 *            The x position to check
	 * @param y
	 *            The y position to check
	 * @return The nearest entity to that point
	 */
	@Nullable
	public Entity getNearestEntity(double x, double y) {
		Comparator<Entity> comparator = MemoryLib.GET_NEAREST_ENTITY_POINT.get(x + "," + y);
		if (comparator == null) {
			comparator = new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					int dist1 = (int) Math.sqrt(Math.pow(x - e1.getX(), 2) + Math.pow(y - e1.getY(), 2));
					int dist2 = (int) Math.sqrt(Math.pow(x - e2.getX(), 2) + Math.pow(y - e2.getY(), 2));

					if (dist1 < dist2)
						return -1;
					if (dist1 > dist2)
						return 1;
					return 0;
				}
			};
			MemoryLib.GET_NEAREST_ENTITY_POINT.put(x + "," + y, comparator);
		}
		return entities.size() > 0 ? Collections.min(entities, comparator) : null;
	}

	/**
	 * Searches for the nearest player that is not the checker at a point.
	 * 
	 * @param checker
	 *            The entity to check
	 * @return The nearest entity that is not the checker to that point
	 */
	@Nullable
	public Player getNearestPlayer(Entity checker) {
		Comparator<Player> comparator = MemoryLib.GET_NEAREST_PLAYER_ENTITY.get(checker.getX() + "," + checker.getY());
		if (comparator == null) {
			comparator = new Comparator<Player>() {
				@Override
				public int compare(Player e1, Player e2) {
					int dist1 = (int) Math.sqrt(Math.pow(checker.getX() - e1.getX(), 2) + Math.pow(checker.getY() - e1.getY(), 2));
					int dist2 = (int) Math.sqrt(Math.pow(checker.getX() - e2.getX(), 2) + Math.pow(checker.getY() - e2.getY(), 2));

					if (checker == e1 || checker == e2)
						return -1;
					if (dist1 < dist2)
						return -1;
					if (dist1 > dist2)
						return 1;
					return 0;
				}
			};
			MemoryLib.GET_NEAREST_PLAYER_ENTITY.put(checker.getX() + "," + checker.getY(), comparator);
		}
		return players.size() > 0 ? Collections.min(players, comparator) : null;
	}

	/**
	 * Searches for the nearest player at a point.
	 * 
	 * @param x
	 *            The x position to check
	 * @param y
	 *            The y position to check
	 * @return The nearest entity to that point
	 */
	@Nullable
	public Player getNearestPlayer(double x, double y) {
		Comparator<Player> comparator = MemoryLib.GET_NEAREST_PLAYER_POINT.get(x + "," + y);
		if (comparator == null) {
			comparator = new Comparator<Player>() {
				@Override
				public int compare(Player e1, Player e2) {
					int dist1 = (int) Math.sqrt(Math.pow(x - e1.getX(), 2) + Math.pow(y - e1.getY(), 2));
					int dist2 = (int) Math.sqrt(Math.pow(x - e2.getX(), 2) + Math.pow(y - e2.getY(), 2));

					if (dist1 < dist2)
						return -1;
					if (dist1 > dist2)
						return 1;
					return 0;
				}
			};
			MemoryLib.GET_NEAREST_PLAYER_POINT.put(x + "," + y, comparator);
		}
		return players.size() > 0 ? Collections.min(players, comparator) : null;
	}

	/**
	 * @return The entities array. Plz no hax or breaks
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * @return The players array. Plz no hax or breaks
	 */
	public List<Player> getPlayers() {
		return players;
	}
}