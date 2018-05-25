package com.ocelot.mod.game.core.gfx;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic font renderer. Allows for multiple different types of font renderers
 * 
 * @author Ocelot5836
 */
public interface FontRenderer {

	/**
	 * Renders a string to the screen.
	 * 
	 * @param text
	 *            The text to render
	 * @param x
	 *            The x position to render the text at
	 * @param y
	 *            The y position to render the text at
	 * @param dropShadow
	 *            Whether or not to render a shadow
	 */
	void renderString(String text, double x, double y, boolean dropShadow);

	/**
	 * Renders a centered string to the screen.
	 * 
	 * @param text
	 *            The text to render
	 * @param x
	 *            The x position to render the text at
	 * @param y
	 *            The y position to render the text at
	 * @param dropShadow
	 *            Whether or not to render a shadow
	 */
	void renderCenteredString(String text, double x, double y, boolean dropShadow);

	/**
	 * Calculates the width of the supplied string.
	 * 
	 * @param text
	 *            The text to get the width of
	 * @return The width of the text
	 */
	int getStringWidth(String text);

}