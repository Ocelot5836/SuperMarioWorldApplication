package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.AnimatedTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.PropertyDouble;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class TileQuestionBlock extends AnimatedTile {

	public static final PropertyDouble BOUNCE = PropertyDouble.create("bounce", 0, 10);

	public TileQuestionBlock() {
		super("question_box", 100, new Sprite(Tile.TILES_SHEET.getSubimage(0, 80, 16, 16)), new Sprite(Tile.TILES_SHEET.getSubimage(16, 80, 16, 16)), new Sprite(Tile.TILES_SHEET.getSubimage(32, 80, 16, 16)), new Sprite(Tile.TILES_SHEET.getSubimage(48, 80, 16, 16)));
		this.setSolid();
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		double bounce = getValue(BOUNCE);
		double lift = bounce;
		if (bounce >= BOUNCE.getMaxValue() / 2) {
			lift = BOUNCE.getMaxValue() - bounce;
		}

		GlStateManager.translate(0, -lift, 0);
		super.render(x, y, tileMap, gui, mc, mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();
	}

	@Override
	public void onEntityCollision(int x, int y, Entity entity, EnumDirection hitDirection) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (hitDirection == EnumDirection.DOWN) {
				setValue(BOUNCE, 1.0);
			}
		}
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		Double bounce = getValue(BOUNCE);
		if (bounce != null && bounce != 0) {
			if (bounce + 0.5 < BOUNCE.getMaxValue()) {
				container.setValue(BOUNCE, bounce + 0.5);
			} else {
				container.setValue(BOUNCE, 0.0);
				tileMap.setTile(x, y, Tile.EMPTY_QUESTION_BLOCK);
			}
		}
		return container;
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, BOUNCE);
	}
}