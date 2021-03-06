package com.ocelot.mod.game.core.level.tile;

import com.ocelot.api.utils.TextureUtils;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The default implementation of a tile. All tiles should extend this, but they do not have to.
 * 
 * @author Ocelot5836
 */
public class BasicTile extends Tile {

	/** The sprite used to render */
	protected Sprite sprite;

	public BasicTile(String unlocalizedName) {
		this((Sprite) null, unlocalizedName);
	}

	public BasicTile(ItemStack stack, String unlocalizedName) {
		this(new Sprite(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack).getParticleTexture()), unlocalizedName);
	}

	public BasicTile(Sprite sprite, String unlocalizedName) {
		super(unlocalizedName);
		if (sprite == null) {
			this.sprite = new Sprite(TextureUtils.getMissingSprite());
		} else {
			this.sprite = sprite;
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void render(double x, double y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (sprite != null) {
			sprite.render(x, y, tileMap.getTileSize(), tileMap.getTileSize());
		}
	}
}