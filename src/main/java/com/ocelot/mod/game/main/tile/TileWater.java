package com.ocelot.mod.game.main.tile;

import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.PropertyBoolean;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class TileWater extends Tile {

	public static final PropertyBoolean UP = PropertyBoolean.create("up");

	private Sprite[] sprites;

	public TileWater() {
		super("water");
		this.sprites = new Sprite[] { new Sprite(TILES_SHEET.getSubimage(176, 240, 16, 16)), new Sprite(TILES_SHEET.getSubimage(176, 224, 16, 16)) };
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
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

		return container;
	}

	@Override
	public void update() {
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (getValue(UP) != null) {
			sprites[(boolean) getValue(UP) ? 0 : 1].render(x, y, 16, 16);
		} else {
			sprites[1].render(x, y, 16, 16);
		}
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, UP);
	}
}