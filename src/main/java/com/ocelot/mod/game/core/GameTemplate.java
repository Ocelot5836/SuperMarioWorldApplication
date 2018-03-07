package com.ocelot.mod.game.core;

import com.mrcrayfish.device.core.Laptop;
import com.ocelot.mod.Mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public abstract class GameTemplate {

	protected static String closeInfo;
	protected static boolean closed;

	protected int width;
	protected int height;

	public GameTemplate(int width, int height) {
		this.width = width;
		this.height = height;
		this.closed = false;
	}

	/**
	 * Initializes the game.
	 */
	public abstract void init();

	/**
	 * Updates the logic of the game.
	 */
	public abstract void update();

	/**
	 * Renders the game to the screen.
	 * 
	 * @param gui
	 *            A gui instance
	 * @param mc
	 *            A minecraft instance
	 * @param mouseX
	 *            The c position of the mouse
	 * @param mouseY
	 *            The y position of the mouse
	 * @param partialTicks
	 *            The partial ticks
	 */
	public abstract void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks);

	/**
	 * Called when a key is pressed.
	 * 
	 * @param keyCode
	 *            The code pressed
	 * @param typedChar
	 *            The char typed
	 */
	public void onKeyPressed(int keyCode, char typedChar) {
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
	 * Loads data from an NBTTagCompound.
	 * 
	 * @param nbt
	 *            The tag to read from
	 */
	public void load(NBTTagCompound nbt) {
	}

	/**
	 * Saves data to an NBTTagCompound.
	 * 
	 * @param nbt
	 *            The tag to save to
	 */
	public void save(NBTTagCompound nbt) {
	}

	/**
	 * @return The window width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The window height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Stops the application and prints a message.
	 * 
	 * @param e
	 *            The exception thrown, null if none
	 */
	public static void stop(Exception e) {
		stop(e, "No information provided");
	}

	/**
	 * Stops the application and prints the info.
	 * 
	 * @param e
	 *            The exception thrown, null if none
	 * @param info
	 *            The information provided that may be the cause of close or crash
	 */
	public static void stop(Exception e, String info) {
		Mod.logger().catching(new Throwable(info, e));
		closeInfo = "" + TextFormatting.BLUE + e + "\n" + TextFormatting.DARK_RED + info;
		closed = true;
		Laptop.getSystem().closeContext();
	}

	/**
	 * @return Whether or not the app was stopped
	 */
	public static boolean isClosed() {
		return closed;
	}

	/**
	 * @return The info provided as the reason of close
	 */
	public static String getCloseInfo() {
		return closeInfo;
	}
}