package com.ocelot.mod.game.core.level.tile;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.property.IProperty;
import com.ocelot.mod.game.core.level.tile.property.PropertyBase;
import com.ocelot.mod.game.core.level.tile.property.PropertyEnum;
import com.ocelot.mod.game.core.level.tile.property.PropertyInteger;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;

import net.minecraft.util.IStringSerializable;

public class QuestionMarkTile extends BasicTile {

	public static final PropertyEnum ITEM = PropertyEnum.create("item", ItemType.class);

	public QuestionMarkTile(Sprite sprite) {
		super(sprite);
		this.setSolid();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public TileStateContainer createContainer() {
		return new TileStateContainer(this, new IProperty[] { ITEM });
	}
	
	public enum ItemType implements IStringSerializable {
		TEST("test");
		
		private String name;
		
		private ItemType(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}