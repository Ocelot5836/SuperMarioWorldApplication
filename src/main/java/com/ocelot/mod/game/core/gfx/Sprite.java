package com.ocelot.mod.game.core.gfx;

import java.awt.image.BufferedImage;

import javax.annotation.Nullable;

import com.ocelot.api.utils.TextureUtils;
import com.ocelot.mod.lib.Lib;
import com.ocelot.mod.lib.MemoryLib;
import com.ocelot.mod.lib.RenderHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
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

	private BufferedImage image;
	private DynamicTexture dynamicTexture;
	private TextureAtlasSprite textureAtlasSprite;
	private ResourceLocation texture;
	private int u;
	private int v;
	private int width;
	private int height;
	/** The type of image data stored. Used if this is a buffered image or something else like a resource location */
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
	 */
	public Sprite(ResourceLocation texture, int u, int v, int width, int height) {
		this.setData(texture, u, v, width, height);
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
	 * Creates a new sprite using a buffered image.
	 * 
	 * @param image
	 *            The image to use
	 */
	public Sprite(BufferedImage image) {
		this.setData(image);
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
	 */
	public void setData(ResourceLocation texture, int u, int v, int width, int height) {
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
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
	 * Creates a new sprite using a buffered image.
	 * 
	 * @param image
	 *            The image to use
	 */
	public void setData(BufferedImage image) {
		this.dynamicTexture = MemoryLib.SPRITE_DYNAMIC_TEXTURES.get(image);
		if (this.dynamicTexture == null) {
			this.dynamicTexture = new DynamicTexture(image);
			MemoryLib.SPRITE_DYNAMIC_TEXTURES.put(image, this.dynamicTexture);
		}

		this.texture = MemoryLib.SPRITE_DYNAMIC_TEXTURE_LOCATIONS.get(this.dynamicTexture);
		if (this.texture == null) {
			this.texture = TextureUtils.createBufferedImageTexture(this.dynamicTexture);
			MemoryLib.SPRITE_DYNAMIC_TEXTURE_LOCATIONS.put(this.dynamicTexture, this.texture);
		}

		this.image = image;
		this.u = 0;
		this.v = 0;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.type = EnumType.BUFFERED_IMAGE;
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
		render(x, y, this.getWidth(), this.getHeight());
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
		TextureUtils.bindTexture(this.getTexture());
		if (this.type == EnumType.TEXTURE_ATLAS_SPRITE || this.type == EnumType.MISSING) {
			RenderHelper.drawTexturedModalRect(x, y, this.textureAtlasSprite, width, height);
			return;
		}
		double imageWidth = this.type == EnumType.BUFFERED_IMAGE ? this.getWidth() : 256;
		double imageHeight = this.type == EnumType.BUFFERED_IMAGE ? this.getHeight() : 256;
		RenderHelper.drawScaledCustomSizeModalRect(x, y, this.getU(), this.getV(), this.getWidth(), this.getHeight(), width, height, imageWidth, imageHeight);
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
	 * @return The image type
	 */
	public EnumType getType() {
		return type;
	}

	/**
	 * Will be null if {@link #type} is not equal to BufferedImage.
	 * 
	 * @return The image of this sprite
	 */
	@Nullable
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Will be null if {@link #type} is not equal to BufferedImage.
	 * 
	 * @return The dynamic texture of this sprite
	 */
	@Nullable
	public DynamicTexture getDynamicTexture() {
		return dynamicTexture;
	}

	/**
	 * @return The texture data if the texture came form a buffered image
	 */
	public int[] getTextureData() {
		if (this.getType() == EnumType.BUFFERED_IMAGE) {
			return this.dynamicTexture.getTextureData();
		} else {
			return new int[0];
		}
	}

	@Override
	protected final void finalize() throws Throwable {
		super.finalize();
		if (MemoryLib.FLIP_SPRITE_HORIZONTAL_IMAGES.containsKey(this.getTextureData())) {
			MemoryLib.FLIP_SPRITE_HORIZONTAL_IMAGES.remove(this.getTextureData());
		}

		if (MemoryLib.SPRITE_DYNAMIC_TEXTURES.containsKey(image)) {
			MemoryLib.SPRITE_DYNAMIC_TEXTURES.remove(image);
		}

		if (MemoryLib.SPRITE_DYNAMIC_TEXTURE_LOCATIONS.containsKey(dynamicTexture)) {
			MemoryLib.SPRITE_DYNAMIC_TEXTURE_LOCATIONS.remove(dynamicTexture);
		}
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
		MISSING, RESOURCE_LOCATION, TEXTURE_ATLAS_SPRITE, BUFFERED_IMAGE
	}
}