package com.ocelot.mod.game.core;

import java.io.File;
import java.io.FileWriter;

import com.ocelot.mod.Mod;
import com.ocelot.mod.config.ModConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The base of any game. Has the basic capability to save and catch error.
 * 
 * @author Ocelot5836
 */
public abstract class GameTemplate {

	protected static File errorFile;
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
	 * Called when the application is closed.
	 */
	public void onClose() {
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
	public static void stop(Throwable e) {
		stop(e, I18n.format("exception." + Mod.MOD_ID + ".noinfo"));
	}

	/**
	 * Stops the application and prints the info.
	 * 
	 * @param e
	 *            The exception thrown, null if none
	 * @param info
	 *            The information provided that may be the cause of close or crash
	 */
	public static void stop(Throwable e, String info) {
		Mod.logger().catching(new Throwable(info, e));
		closeInfo = "" + TextFormatting.BLUE + e + "\n" + TextFormatting.DARK_RED + info;
		errorFile = new File(Loader.instance().getConfigDir(), Mod.MOD_ID + "/latest-crash.log");

		try {
			if (!errorFile.getParentFile().exists()) {
				errorFile.getParentFile().mkdirs();
			}

			if (!errorFile.exists()) {
				if (errorFile.createNewFile()) {
				} else {
				}
			}

			CrashReport crash = CrashReport.makeCrashReport(e, closeInfo);
			crash.makeCategory("Stop");

			FileWriter writer = new FileWriter(errorFile);
			writer.write(crash.getCompleteReport());
			writer.close();
		} catch (Exception e1) {
			Mod.logger().catching(e1);
		}

		closed = true;
	}

	/**
	 * Plays the specified sound on the client side. Plays at the default sound volume.
	 * 
	 * @param sound
	 *            The sound to be played
	 * @param pitch
	 *            The pitch of the sound
	 */
	public void playSound(SoundEvent sound, float pitch) {
		playSound(sound, (float) ModConfig.marioSFXVolume, pitch);
	}

	/**
	 * Plays the specified sound on the client side.
	 * 
	 * @param sound
	 *            The sound to be played
	 * @param volume
	 *            The volume of the sound
	 * @param pitch
	 *            The pitch of the sound
	 */
	public void playSound(SoundEvent sound, float volume, float pitch) {
		if (ModConfig.enableMarioSFX) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Minecraft.getMinecraft().player.playSound(sound, volume, pitch);
			});
		}
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

	/**
	 * @return The file the error info was written to. null if no error has happened
	 */
	public static File getErrorFile() {
		return errorFile;
	}
}