package com.ocelot.mod;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Contains some basic library methods.
 * 
 * @author Ocelot5836
 */
public class Lib {

	/**
	 * Flips a sprite horizontally.
	 * 
	 * @param sprite
	 *            The sprite to flip
	 */
	public static Sprite flipHorizontal(Sprite sprite) {
		if (sprite.getType() == Sprite.EnumType.BUFFERED_IMAGE) {
			BufferedImage returned = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < returned.getHeight(); y++) {
				for (int x = 0; x < returned.getWidth(); x++) {
					returned.setRGB(sprite.getWidth() - x - 1, y, sprite.getTextureData()[x + y * sprite.getWidth()]);
				}
			}
			sprite.setData(returned);
		}
		return sprite;
	}

	/**
	 * Loads a buffered image image from memory.
	 * 
	 * <br>
	 * </br>
	 * 
	 * <i><b>PLEASE NOTE!</b> This will return a 16x16 buffered image if the specified image could not be found!</i>
	 * 
	 * @param location
	 *            The location of that image
	 * @return The image that was found
	 */
	public static BufferedImage loadImage(ResourceLocation location) {
		try {
			return ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream());
		} catch (IOException e) {
			Mod.logger().warn("Could not load image " + location + ". Could cause issues later on.");
		}
		return new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	}
}