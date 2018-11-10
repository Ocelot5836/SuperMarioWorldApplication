package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.PropertyBoolean;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class TileWater extends Tile {

	public static final PropertyBoolean UP = PropertyBoolean.create("up");

	private boolean transparent;
	private Sprite[] sprites;

	public TileWater(boolean transparent) {
		super(transparent ? "water_transparent" : "water");
		this.transparent = transparent;
		if (transparent) {
			this.setShouldNotRender();
		}
		this.sprites = Lib.asArray(new Sprite(TILES_SHEET, 176, 240, 16, 16, 256, 416), new Sprite(TILES_SHEET, 176, 224, 16, 16, 256, 416));
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		if (!transparent) {
			boolean up = (boolean) container.getValue(UP);

			if (tileMap.getTile(x, y - 1).equals(this) || tileMap.getTile(x, y - 1).equals(Tile.VOID)) {
				if (!up) {
					container.setValue(UP, true);
				}
			} else {
				if (up) {
					container.setValue(UP, false);
				}
			}

		}
		return container;
	}

	@Override
	public void update() {
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.enableBlend();
		if (getValue(UP) != null) {
			sprites[(boolean) getValue(UP) ? 0 : 1].render(x, y, 16, 16);
		} else {
			sprites[1].render(x, y, 16, 16);
		}
		GlStateManager.disableBlend();
	}

	@Override
	public TileStateContainer createContainer() {
		return !transparent ? new TileStateContainer(this, UP) : super.createContainer();
	}
}