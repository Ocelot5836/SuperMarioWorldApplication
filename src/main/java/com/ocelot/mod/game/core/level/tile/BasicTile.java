package com.ocelot.mod.game.core.level.tile;

import com.ocelot.api.utils.TextureUtils;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;

public class BasicTile extends Tile {

	protected Sprite sprite;

	public BasicTile(ItemStack stack) {
		this(new Sprite(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack).getParticleTexture()));
	}

	public BasicTile(Sprite sprite) {
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
	public void render(int x, int y, TileMap tileMap, Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		sprite.render(x, y, tileMap.getTileSize(), tileMap.getTileSize());
	}
}