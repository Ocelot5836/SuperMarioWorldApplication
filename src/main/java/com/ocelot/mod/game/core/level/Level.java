package com.ocelot.mod.game.core.level;

import java.util.List;

import com.google.common.collect.Lists;
import com.ocelot.mod.game.core.entity.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Level {

	private TileMap tileMap;
	private List<Entity> entities = Lists.<Entity>newArrayList();

	public Level(int tileSize, ResourceLocation mapLocation) {
		this.tileMap = new TileMap(tileSize);
		this.tileMap.loadMap(mapLocation);
	}

	public void update() {
		tileMap.update();

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
			e.update();
			e.update();
			if (e.isDead()) {
				entities.remove(e);
				i--;
			}
		}
	}

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

	public void add(Entity entity) {
		entity.init(this);
		entities.add(entity);
	}

	public TileMap getMap() {
		return tileMap;
	}
}