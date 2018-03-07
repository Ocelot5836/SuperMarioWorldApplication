package com.ocelot.mod.game.level.tile;

import com.ocelot.mod.game.core.EnumDir;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.BasicTile;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.PropertyEnum;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.entity.Player;

import net.minecraft.util.IStringSerializable;

public class InfoBoxTile extends BasicTile {

	public static final PropertyEnum<TextType> TEXT = PropertyEnum.<TextType>create("text", TextType.class);

	public InfoBoxTile() {
		super(new Sprite(MISC_LOCATION, 0, 0, 16, 16));
		this.setSolid();
	}

	@Override
	public void onEntityCollision(int x, int y, Entity entity, EnumDir hitDirection) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (hitDirection == EnumDir.DOWN) {
				player.openGui(0);
			}
		}
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, new IProperty[] { TEXT });
	}

	public enum TextType implements IStringSerializable {
		YOSHI_HOUSE();

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
	}
}