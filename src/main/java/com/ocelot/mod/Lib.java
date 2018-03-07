package com.ocelot.mod;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Lib {

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

	public static BufferedImage loadImage(ResourceLocation location) {
		try {
			return ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream());
		} catch (IOException e) {
			Mod.logger().warn("Could not load image " + location + ". Could cause issues later on.");
		}
		return new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	}
}