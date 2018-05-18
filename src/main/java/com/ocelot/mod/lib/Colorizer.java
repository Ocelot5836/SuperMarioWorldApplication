package com.ocelot.mod.lib;

import java.awt.image.BufferedImage;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Contains helper methods that have to deal with colors and the modification of RGB data.
 * 
 * @author Ocelot5836
 */
public class Colorizer {

	/**
	 * Adds color to a gray scale image form darkest to lightest.
	 * 
	 * @param image
	 *            The image to add color to
	 * @param color1
	 *            The color that will replace the first color in the image
	 * @param color2
	 *            The color that will replace the second color in the image
	 * @param color3
	 *            The color that will replace the third color in the image
	 * @param color4
	 *            The color that will replace the fourth color in the image
	 * @param color5
	 *            The color that will replace the fifth color in the image
	 * @param color6
	 *            The color that will replace the sixth color in the image
	 * @param color7
	 *            The color that will replace the seventh color in the image
	 * @param color8
	 *            The color that will replace the eighth color in the image
	 * @return The image with the colors applied
	 */
	public static BufferedImage colorize(BufferedImage image, int color1, int color2, int color3, int color4, int color5, int color6, int color7, int color8) {
		BufferedImage newImage = MemoryLib.COLORIZE_8.get(image + "," + color1 + "," + color2 + "," + color3 + "," + color4 + "," + color5 + "," + color6 + "," + color7 + "," + color8);
		if (newImage == null) {
			newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			image.copyData(newImage.getRaster());
			for (int y = 0; y < newImage.getHeight(); y++) {
				for (int x = 0; x < newImage.getWidth(); x++) {
					if (newImage.getRGB(x, y) == 0xff000000) {
						newImage.setRGB(x, y, color1);
					}
					
					if (newImage.getRGB(x, y) == 0xff262626) {
						newImage.setRGB(x, y, color2);
					}
					
					if (newImage.getRGB(x, y) == 0xff4a4a4a) {
						newImage.setRGB(x, y, color3);
					}
					
					if (newImage.getRGB(x, y) == 0xff6e6e6e) {
						newImage.setRGB(x, y, color4);
					}
					
					if (newImage.getRGB(x, y) == 0xff929292) {
						newImage.setRGB(x, y, color5);
					}
					
					if (newImage.getRGB(x, y) == 0xffb6b6b6) {
						newImage.setRGB(x, y, color6);
					}
					
					if (newImage.getRGB(x, y) == 0xffdadada) {
						newImage.setRGB(x, y, color7);
					}
					
					if (newImage.getRGB(x, y) == 0xffffffff) {
						newImage.setRGB(x, y, color8);
					}
				}
			}
			MemoryLib.COLORIZE_8.put(image + "," + color1 + "," + color2 + "," + color3 + "," + color4 + "," + color5 + "," + color6 + "," + color7 + "," + color8, newImage);
		}
		return newImage;
	}

	/**
	 * Replaces all the pixels of the specified color in a buffered image.
	 * 
	 * @param image
	 *            The image to modify the pixels of
	 * @param oldColor
	 *            The color that will be replaced
	 * @param newColor
	 *            The color that will replace the old color
	 * @return The image with the modified pixels
	 */
	public static BufferedImage replacePixels(BufferedImage image, int oldColor, int newColor) {
		BufferedImage newImage = MemoryLib.COLORIZER_REPLACE_BUFFERED_PIXELS.get(image + "," + oldColor + "," + newColor);
		if (newImage == null) {
			newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			image.copyData(newImage.getRaster());
			for (int y = 0; y < newImage.getHeight(); y++) {
				for (int x = 0; x < newImage.getWidth(); x++) {
					if (newImage.getRGB(x, y) == oldColor) {
						newImage.setRGB(x, y, newColor);
					}
				}
			}
			MemoryLib.COLORIZER_REPLACE_BUFFERED_PIXELS.put(image + "," + oldColor + "," + newColor, newImage);
		}
		return newImage;
	}
}