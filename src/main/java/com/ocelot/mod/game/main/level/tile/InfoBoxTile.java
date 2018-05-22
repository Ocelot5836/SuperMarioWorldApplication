package com.ocelot.mod.game.main.level.tile;

import java.util.Locale;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.BasicTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.PropertyEnum;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.gui.Guis;

import net.minecraft.util.IStringSerializable;

public class InfoBoxTile extends BasicTile {

	public static final PropertyEnum<TextType> TEXT = PropertyEnum.<TextType>create("text", TextType.class);

	public InfoBoxTile() {
		super(new Sprite(Tile.TILES_SHEET.getSubimage(32, 0, 16, 16)), "info_box");
		this.setSolid();
	}

	@Override
	public void onEntityCollision(int x, int y, Entity entity, EnumDirection hitDirection) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (hitDirection == EnumDirection.DOWN) {
				TextType type = (TextType) getValue(TEXT);
				if (type != null) {
					player.openGui(type.getGuiId());
					player.getGame().playSound(Sounds.TILE_MESSAGE_HIT, 1.0F);
				}
			}
		}
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, new IProperty[] { TEXT });
	}

	public enum TextType implements IStringSerializable {
		YOSHI_HOUSE(Guis.YOSHI_TEXT_BUBBLE), MR_CRAYFISH_HELLO(Guis.MR_CRAYFISH_HELLO);

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