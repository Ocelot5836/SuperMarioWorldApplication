package com.ocelot.mod.game.main.tile;

import java.util.Locale;

import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;
import com.ocelot.mod.game.core.level.tile.BasicTile;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.core.level.tile.property.PropertyDouble;
import com.ocelot.mod.game.core.level.tile.property.PropertyEnum;
import com.ocelot.mod.game.core.level.tile.property.TileStateContainer;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.gui.Guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IStringSerializable;

public class TileInfoBox extends BasicTile {

	public static final PropertyEnum<TextType> TEXT = PropertyEnum.<TextType>create("text", TextType.class);
	public static final PropertyDouble BOUNCE = PropertyDouble.create("bounce", 0, 10);

	public TileInfoBox() {
		super(new Sprite(Tile.TILES_SHEET, 32, 0, 16, 16, 256, 416), "info_box");
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
				TextType type = getValue(TEXT);
				if (type != null) {
					player.openGui(type.getGuiId());
					player.getGame().playSound(Sounds.TILE_MESSAGE_HIT, 1.0F);
					setValue(BOUNCE, 1.0);
				}
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
			}
		}
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