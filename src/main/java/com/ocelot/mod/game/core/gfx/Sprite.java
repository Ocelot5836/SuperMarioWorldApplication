package com.ocelot.mod.game.core.gfx;

import java.awt.image.BufferedImage;

import com.ocelot.api.utils.TextureUtils;
import com.ocelot.mod.Lib;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
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

	private DynamicTexture dynamicTexture;
	private ResourceLocation texture;
	private int u;
	private int v;
	private int width;
	private int height;
	private EnumType type;

	/**
	 * Creates a new sprite with the missing image texture.
	 */
	public Sprite() {
		this(TextureUtils.getMissingSprite());
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
	public Sprite(ResourceLocation texture, int u, int v, int width, int height) {
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
	public Sprite(TextureAtlasSprite sprite) {
		if (sprite == null)
			sprite = TextureUtils.getMissingSprite();
		this.texture = TextureMap.LOCATION_BLOCKS_TEXTURE;
		this.u = sprite.getOriginX();
		this.v = sprite.getOriginY();
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
	public Sprite(BufferedImage image) {
		this.dynamicTexture = new DynamicTexture(image);
		this.texture = TextureUtils.createBufferedImageTexture(this.dynamicTexture);
		this.u = 0;
		this.v = 0;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.type = EnumType.BUFFERED_IMAGE;
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
		this.texture = TextureMap.LOCATION_BLOCKS_TEXTURE;
		this.u = sprite.getOriginX();
		this.v = sprite.getOriginY();
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
		this.dynamicTexture = new DynamicTexture(image);
		this.texture = TextureUtils.createBufferedImageTexture(this.dynamicTexture);
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
	public void render(double x, double y, int width, int height) {
		TextureUtils.bindTexture(this.getTexture());
		int imageWidth = 256;
		int imageHeight = 256;
		if (this.type == EnumType.MISSING || this.type == EnumType.TEXTURE_ATLAS_SPRITE) {
			imageWidth = 1024;
			imageHeight = 512;
		} else if (this.type == EnumType.BUFFERED_IMAGE) {
			imageWidth = this.getWidth();
			imageHeight = this.getHeight();
		}
		Lib.drawScaledCustomSizeModalRect(x, y, this.getU(), this.getV(), this.getWidth(), this.getHeight(), width, height, imageWidth, imageHeight);
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
	 * @return The texture data if the texture came form a buffered image
	 */
	public int[] getTextureData() {
		if (this.getType() == EnumType.BUFFERED_IMAGE) {
			return this.dynamicTexture.getTextureData();
		} else {
			return new int[0];
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