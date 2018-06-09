package com.ocelot.mod.lib;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.ocelot.mod.Mod;
import com.ocelot.mod.Usernames;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.TileMap;

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
	 * Draws an entity collision box.
	 * 
	 * @param entity
	 *            The entity to draw the box of
	 * @param color
	 *            The color of the collision box
	 */
	public static void drawCollisionBox(Entity entity, int color) {
		TileMap tileMap = entity.getLevel().getMap();
		AxisAlignedBB box = entity.getEntityBox();
		double boxX = (entity.getLastX() + entity.getPartialRenderX()) - (tileMap.getLastX() + tileMap.getPartialRenderX()) - box.getWidth() / 2;
		double boxY = (entity.getLastY() + entity.getPartialRenderY()) - (tileMap.getLastY() + tileMap.getPartialRenderY()) - box.getHeight() / 2;
		RenderHelper.drawRect(boxX, boxY, boxX + box.getWidth(), boxY + box.getHeight(), color);
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
	 * Loads a buffered image image from file into memory.
	 * 
	 * <br>
	 * </br>
	 * 
	 * <i><b>PLEASE NOTE!</b> This will return a 64x64 buffered image if the specified image could not be found!</i>
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
	 * Loads text to a string from a resource location.
	 * 
	 * @param location
	 *            The location of the text file
	 * @return The text compressed into a string
	 */
	public static String loadTextToString(ResourceLocation location) {
		try {
			return IOUtils.toString(Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream(), Charset.defaultCharset());
		} catch (IOException e) {
			Mod.logger().warn("Could not load text " + location + ". Could cause issues later on.");
		}
		return "";
	}

	/**
	 * Allows the easy ability to turn multiple objects or just one into an array.
	 * 
	 * @param obj
	 *            The objects to put into an array.
	 * @param <T>
	 *            The type of array that will be returned
	 * @return The array generated with the parameters
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
	 * Parses a single-step equation.
	 * 
	 * @param equation
	 *            The equation
	 * @return The number that was calculated from the equation
	 */
	public static double parseEquation(String equation) throws Exception {
		try {
			String[] values = equation.split("+");
			if (equation.startsWith("+")) {
				double value1 = Double.parseDouble(values[1]);
				double value2 = Double.parseDouble(values[2]);
				return value1 + value2;
			} else {
				double value1 = Double.parseDouble(values[0]);
				double value2 = Double.parseDouble(values[1]);
				return value1 + value2;
			}
		} catch (Exception e1) {
			try {
				String[] values = equation.split("-");
				if (equation.startsWith("-")) {
					double value1 = Double.parseDouble(values[1]);
					double value2 = Double.parseDouble(values[2]);
					return value1 - value2;
				} else {
					double value1 = Double.parseDouble(values[0]);
					double value2 = Double.parseDouble(values[1]);
					return value1 - value2;
				}
			} catch (Exception e2) {
				try {
					String[] values = equation.split("*");
					double value1 = Double.parseDouble(values[0]);
					double value2 = Double.parseDouble(values[1]);
					return value1 * value2;
				} catch (Exception e3) {
					try {
						String[] values = equation.split("/");
						double value1 = Double.parseDouble(values[0]);
						double value2 = Double.parseDouble(values[1]);
						return value1 / value2;
					} catch (Exception e4) {
						throw new Exception("Could not parse equation from \'" + equation + "\'", e4);
					}
				}
			}
		}
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

	/**
	 * Converts a string of text into the Creyfush language.
	 * 
	 * @param message
	 *            The message to convert
	 * @return The converted message
	 */
	public static String convertToCrayfish(String message) {
		return message.replaceAll(Usernames.MR_CRAYFISH, "MrCreyfush");
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		Mod.logger().info("Reloading Buffered Images");
		for (ResourceLocation key : MemoryLib.LOADED_IMAGES.keySet()) {
			loadImage(key, false);
		}
	}
}