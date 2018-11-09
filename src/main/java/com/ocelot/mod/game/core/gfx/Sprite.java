package com.ocelot.mod.game.core.gfx;

import com.ocelot.api.utils.TextureUtils;
import com.ocelot.mod.lib.RenderHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Represents an image that can be drawn to the screen.
 * 
 * @author Ocelot5836
 */
public class Sprite {

	private TextureAtlasSprite textureAtlasSprite;
	private ResourceLocation texture;
	private int u;
	private int v;
	private int width;
	private int height;
	private int textureWidth;
	private int textureHeight;
	private EnumType type;

	/**
	 * Creates a new sprite with the missing image texture.
	 */
	public Sprite() {
		this.setData();
	}

	/**
	 * Creates a new sprite using the supplied resource and uv coords.
	 * 
	 * @param texture
	 *            The resource location of the texture
	 * @param u
	 *            The x coord on the texture
	 * @param v
	 *            The y coord on the texture
	 * @param width
	 *            The width of the sprite
	 * @param height
	 *            The height of the sprite
	 * @param textureWidth
	 *            The width of the texture image
	 * @param textureHeight
	 *            the height of the texture image
	 */
	public Sprite(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this.setData(texture, u, v, width, height, textureWidth, textureHeight);
	}

	/**
	 * Creates a new sprite using a texture atlas sprite.
	 * 
	 * @param sprite
	 *            The sprite to use
	 */
	public Sprite(TextureAtlasSprite sprite) {
		this.setData(sprite);
	}

	/**
	 * Creates a new sprite using an item stack.
	 * 
	 * @param stack
	 *            The stack's sprite to use
	 */
	public Sprite(ItemStack stack) {
		this(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack).getParticleTexture());
	}

	/**
	 * Creates a new sprite using a block.
	 * 
	 * @param stack
	 *            The block's particle sprite to use
	 */
	public Sprite(Block block) {
		this(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(block)).getParticleTexture());
	}

	/**
	 * Creates a new sprite using an item.
	 * 
	 * @param stack
	 *            The item's sprite to use
	 */
	public Sprite(Item item) {
		this(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(item)).getParticleTexture());
	}

	/**
	 * Creates a new sprite with the missing image texture.
	 */
	public void setData() {
		this.setData(TextureUtils.getMissingSprite());
		this.type = EnumType.MISSING;
	}

	/**
	 * Creates a new sprite using the supplied resource and uv coords.
	 * 
	 * @param texture
	 *            The resource location of the texture
	 * @param u
	 *            The x coord on the texture
	 * @param v
	 *            The y coord on the texture
	 * @param width
	 *            The width of the sprite
	 * @param height
	 *            The height of the sprite
	 * @param textureWidth
	 *            The width of the texture image
	 * @param textureHeight
	 *            the height of the texture image
	 */
	public void setData(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.type = EnumType.RESOURCE_LOCATION;
	}

	/**
	 * Creates a new sprite using a texture atlas sprite.
	 * 
	 * @param sprite
	 *            The sprite to use
	 */
	public void setData(TextureAtlasSprite sprite) {
		if (sprite == null)
			sprite = TextureUtils.getMissingSprite();
		this.textureAtlasSprite = sprite;
		this.texture = TextureMap.LOCATION_BLOCKS_TEXTURE;
		this.u = (int) sprite.getMinU();
		this.v = (int) sprite.getMinV();
		this.width = sprite.getIconWidth();
		this.height = sprite.getIconHeight();
		this.type = EnumType.TEXTURE_ATLAS_SPRITE;
	}

	/**
	 * Renders the sprite at the supplied x and y position with the sprite's width and height.
	 * 
	 * @param x
	 *            The x position to render at
	 * @param y
	 *            The y position to render at
	 */
	public void render(double x, double y) {
		render(x, y, this.getWidth(), this.getHeight(), 0x00);
	}

	/**
	 * Renders the sprite at the supplied x and y position with the sprite's width and height.
	 * 
	 * @param x
	 *            The x position to render at
	 * @param y
	 *            The y position to render at
	 * @param flip
	 *            The way that the image should flip
	 */
	public void render(double x, double y, int flip) {
		render(x, y, this.getWidth(), this.getHeight(), flip);
	}

	/**
	 * Renders the sprite at the supplied x, y, width, and height.
	 * 
	 * @param x
	 *            The x position to render at
	 * @param y
	 *            The y position to render at
	 * @param width
	 *            The width the sprite should fit
	 * @param height
	 *            The height the sprite should fit
	 */
	public void render(double x, double y, double width, double height) {
		render(x, y, width, height, 0x00);
	}

	/**
	 * Renders the sprite at the supplied x, y, width, and height.
	 * 
	 * @param x
	 *            The x position to render at
	 * @param y
	 *            The y position to render at
	 * @param width
	 *            The width the sprite should fit
	 * @param height
	 *            The height the sprite should fit
	 * @param flip
	 *            The way that the image should flip
	 */
	public void render(double x, double y, double width, double height, int flip) {
		boolean flipX = false;
		boolean flipY = false;

		TextureUtils.bindTexture(this.getTexture());
		if (this.type == EnumType.TEXTURE_ATLAS_SPRITE || this.type == EnumType.MISSING) {
			RenderHelper.drawTexturedModalRect(x, y, this.textureAtlasSprite, width, height);
			return;
		}
		RenderHelper.drawScaledCustomSizeModalRect(x, y, this.getU(), this.getV(), this.getWidth(), this.getHeight(), width, height, this.textureWidth, this.textureHeight);
	}

	/**
	 * @return The image location used to render
	 */
	public ResourceLocation getTexture() {
		return texture;
	}

	/**
	 * @return The texture x
	 */
	public int getU() {
		return u;
	}

	/**
	 * @return The texture y
	 */
	public int getV() {
		return v;
	}

	/**
	 * @return The texture width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The texture height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return The width of the texture image
	 */
	public int getTextureWidth() {
		return textureWidth;
	}

	/**
	 * @return The height of the texture image
	 */
	public int getTextureHeight() {
		return textureHeight;
	}

	/**
	 * @return The image type
	 */
	public EnumType getType() {
		return type;
	}

	/**
	 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
	 * 
	 * <br>
	 * </br>
	 * 
	 * The types of images that can be loaded.
	 * 
	 * @author Ocelot5836
	 */
	public enum EnumType {
		MISSING, RESOURCE_LOCATION, TEXTURE_ATLAS_SPRITE
	}
}