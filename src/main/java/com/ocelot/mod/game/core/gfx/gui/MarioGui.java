package com.ocelot.mod.game.core.gfx.gui;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.main.entity.player.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic mario gui that can be rendered over the screen.
 * 
 * @author Ocelot5836
 */
public class MarioGui {

	/** A minecraft instance */
	protected Minecraft mc;
	/** The player that opened the gui */
	protected Player player;
	/** A game instance */
	protected GameTemplate game;
	/** The width of the screen */
	protected int width;
	/** The height of the screen */
	protected int height;

	/**
	 * Initializes the gui.
	 */
	public void initGui() {
	}

	/**
	 * Updates the gui. Called 20 times per second.
	 */
	public void update() {
	}

	/**
	 * Called when a key is pressed.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyPressed(int keyCode, char typedChar) {
		if (keyCode == Keyboard.KEY_RETURN) {
			this.player.closeGui();
		}
	}

	/**
	 * Called when a key is released.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyReleased(int keyCode, char typedChar) {
	}

	/**
	 * Called when the mouse is pressed.
	 * 
	 * @param mouseButton
	 *            The button pressed
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
	}

	/**
	 * Called when the mouse is released.
	 * 
	 * @param mouseButton
	 *            The button released
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
	}

	/**
	 * Called when the mouse is scrolled.
	 * 
	 * @param direction
	 *            The direction the mouse was scrolled
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 */
	public void onMouseScrolled(boolean direction, int mouseX, int mouseY) {
	}

	/**
	 * Renders the gui to the screen.
	 * 
	 * @param gui
	 *            A gui instance
	 * @param mouseX
	 *            The x position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param partialTicks
	 *            The partial ticks
	 */
	public void render(Gui gui, int mouseX, int mouseY, float partialTicks) {
	}

	/**
	 * Called when the gui is about to close.
	 */
	public void onClosed() {		
	}

	/**
	 * Sets the game, player, width, and height variables to their proper values.
	 * 
	 * @param game
	 *            The game instance
	 * @param player
	 *            The player that opened the gui
	 */
	public final MarioGui setSizeAndWorld(GameTemplate game, Player player) {
		this.game = game;
		this.width = game.getWidth();
		this.height = game.getHeight();
		this.player = player;
		this.mc = Minecraft.getMinecraft();
		return this;
	}
}