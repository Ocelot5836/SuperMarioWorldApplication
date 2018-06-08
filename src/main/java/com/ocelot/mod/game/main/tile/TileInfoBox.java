package com.ocelot.mod.game.main.tile;

import java.util.Locale;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.BasicTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.PropertyEnum;
import com.ocelot.mod.game.core.level.tile.property.PropertyInteger;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.gui.Guis;

import net.minecraft.util.IStringSerializable;

public class TileInfoBox extends BasicTile {

	public static final PropertyEnum<TextType> TEXT = PropertyEnum.<TextType>create("text", TextType.class);
	public static final PropertyInteger BOUNCE = PropertyInteger.create("bounce", 0, 10);

	public TileInfoBox() {
		super(new Sprite(Tile.TILES_SHEET.getSubimage(32, 0, 16, 16)), "info_box");
		this.setSolid();
	}

	@Override
	public void onEntityCollision(int x, int y, Entity entity, EnumDirection hitDirection) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (hitDirection == EnumDirection.DOWN) {
				TextType type = getValue(TEXT);
				if (type != null) {
					player.openGui(type.getGuiId());
					player.getGame().playSound(Sounds.TILE_MESSAGE_HIT, 1.0F);
					setValue(BOUNCE, 1);
				}
			}
		}
	}

	@Override
	public TileStateContainer modifyContainer(int x, int y, TileMap tileMap, TileStateContainer container) {
		int bounce = getValue(BOUNCE);
		if (bounce != 0) {
			if (bounce + 1 < BOUNCE.getMaxValue()) {
				container.setValue(BOUNCE, bounce + 1);
			} else {
				container.setValue(BOUNCE, 0);
			}
		}
		System.out.println(bounce);
		return container;
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, TEXT, BOUNCE);
	}

	public enum TextType implements IStringSerializable {
		YOSHI_HOUSE(Guis.YOSHI_TEXT_BUBBLE), MR_CRAYFISH_HELLO(Guis.PLAYER_HELLO);

		private int guiId;

		private TextType(int guiId) {
			this.guiId = guiId;
		}

		public int getGuiId() {
			return guiId;
		}

		@Override
		public String getName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}