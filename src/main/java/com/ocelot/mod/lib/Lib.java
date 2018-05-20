package com.ocelot.mod.lib;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.ocelot.mod.Mod;
import com.ocelot.mod.Usernames;
import com.ocelot.mod.game.core.gfx.Sprite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
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
public class Lib implements IResourceManagerReloadListener {

	/** The instance of this class */
	public static final Lib INSTANCE = new Lib();

	/**
	 * Initializes the lib. This should only be called once.
	 */
	public static void pre() {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(INSTANCE);
	}

	/**
	 * Loads all the sprites specified from a buffered image.
	 * 
	 * @param image
	 *            The image to fetch the sprites from
	 * @param spriteWidth
	 *            The width of each sprite
	 * @param spriteHeight
	 *            The height of each sprite
	 * @param spritesPerRow
	 *            The number of sprites in each row
	 * @return The sprites loaded
	 */
	public static List<BufferedImage[]> loadSpritesFromBufferedImage(BufferedImage image, int spriteWidth, int spriteHeight, int[] spritesPerRow) {
		List<BufferedImage[]> sprites = new ArrayList<BufferedImage[]>();
		for (int i = 0; i < spritesPerRow.length; i++) {
			BufferedImage[] sprite = new BufferedImage[spritesPerRow[i]];
			for (int j = 0; j < spritesPerRow[i]; j++) {
				sprite[j] = image.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
			}
			sprites.add(sprite);
		}
		return sprites;
	}

	/**
	 * Flips a sprite horizontally.
	 * 
	 * @param sprite
	 *            The sprite to flip
	 */
	public static Sprite flipHorizontal(Sprite sprite) {
		if (MemoryLib.FLIP_SPRITE_HORIZONTAL_IMAGES.containsKey(sprite.getTextureData())) {
			sprite.setData(MemoryLib.FLIP_SPRITE_HORIZONTAL_IMAGES.get(sprite.getTextureData()));
			return sprite;
		} 
						
		if (sprite.getType() == Sprite.EnumType.BUFFERED_IMAGE) {
			BufferedImage returned = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < returned.getHeight(); y++) {
				for (int x = 0; x < returned.getWidth(); x++) {
					returned.setRGB(sprite.getWidth() - x - 1, y, sprite.getTextureData()[x + y * sprite.getWidth()]);
				}
			}
			sprite.setData(returned);
			MemoryLib.FLIP_SPRITE_HORIZONTAL_IMAGES.put(sprite.getTextureData(), returned);
		} else {
			Mod.logger().warn("Can not flip sprite with type " + sprite.getType());
		}
		return sprite;
	}

	/**
	 * Flips a buffered image horizontally.
	 * 
	 * @param image
	 *            The image to flip
	 */
	public static BufferedImage flipHorizontal(BufferedImage image) {
		if (MemoryLib.FLIP_BUFFERED_IMAGE_HORIZONTAL_IMAGES.containsKey(image)) {
			return MemoryLib.FLIP_BUFFERED_IMAGE_HORIZONTAL_IMAGES.get(image);
		}

		BufferedImage returned = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < returned.getHeight(); y++) {
			for (int x = 0; x < returned.getWidth(); x++) {
				returned.setRGB(image.getWidth() - x - 1, y, image.getRGB(x, y));
			}
		}
		MemoryLib.FLIP_BUFFERED_IMAGE_HORIZONTAL_IMAGES.put(image, returned);
		return returned;
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
		return loadImage(location, true);
	}

	private static BufferedImage loadImage(ResourceLocation location, boolean checkCache) {
		try {
			if (!MemoryLib.LOADED_IMAGES.containsKey(location) || !checkCache)
				MemoryLib.LOADED_IMAGES.put(location, ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream()));
			return MemoryLib.LOADED_IMAGES.get(location);
		} catch (IOException e) {
			Mod.logger().warn("Could not load image " + location + ". Could cause issues later on.");
		}
		return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * @param obj
	 *            The objects to put into an array.
	 * @param <T>
	 *            The type of array that will be returned
	 * @return The array generated with the params
	 */
	public static <T> T[] asArray(T... obj) {
		return obj;
	}

	/**
	 * Helps when calculating the score obtained from multiple jumps.
	 * 
	 * @param enemyJumpCount
	 *            The number of jumps to get the score for
	 * @return The amount of score calculated from the jumps
	 */
	public static int getScoreFromJumps(int enemyJumpCount) {
		if (enemyJumpCount == 1)
			return 200;
		if (enemyJumpCount == 2)
			return 400;
		if (enemyJumpCount == 3)
			return 800;
		if (enemyJumpCount == 4)
			return 1000;
		if (enemyJumpCount == 5)
			return 2000;
		if (enemyJumpCount == 6)
			return 4000;
		if (enemyJumpCount == 7)
			return 8000;
		return 0;
	}

	/**
	 * @param player
	 *            The player to check the username of
	 * @return Whether or not the player is MrCrayfish
	 */
	public static boolean isUserMrCrayfish(EntityPlayer player) {
		return Usernames.MR_CRAYFISH.equalsIgnoreCase(player.getName());
	}

	/**
	 * @param player
	 *            The player to check the username of
	 * @return Whether or not the player is Ocelot5836
	 */
	public static boolean isUserOcelot5836(EntityPlayer player) {
		return Usernames.OCELOT5836.equalsIgnoreCase(player.getName());
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		for (ResourceLocation key : MemoryLib.LOADED_IMAGES.keySet()) {
			loadImage(key, false);
		}
	}
}