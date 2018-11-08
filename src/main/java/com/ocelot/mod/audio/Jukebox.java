package com.ocelot.mod.audio;

import com.ocelot.mod.config.ModConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The new and improved audio player. Plays audio and keeps track of it to stop and remove later.
 * 
 * @author Ocelot5836
 */
@SideOnly(Side.CLIENT)
public class Jukebox {

	private static MarioMusic music = null;
	private static MarioSFX[] soundEffects = new MarioSFX[5];

	/**
	 * Adds the specified sound effect to the batch of playing sound effects.
	 * 
	 * @param soundEffect
	 *            The sound effect to play
	 * @return Whether or not the sound could be added to the batch
	 */
	private static boolean addSoundEffect(MarioSFX soundEffect) {
		for (int i = 0; i < soundEffects.length; i++) {
			MarioSFX sound = soundEffects[i];
			if (!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(sound)) {
				sound = null;
			}

			if (sound == null) {
				soundEffects[i] = soundEffect;
				return true;
			}
		}
		return false;
	}

	/**
	 * Stops all music and sound effects.
	 */
	public static void stop() {
		stopMusic();
		stopSoundEffects();
	}

	/**
	 * Plays a piece of music.
	 * 
	 * @param music
	 *            The music to play
	 */
	public static void playMusic(ResourceLocation music) {
		playMusic(new MarioMusic(music));
	}

	/**
	 * Plays a piece of mario music.
	 * 
	 * @param music
	 *            The music to play
	 */
	private static void playMusic(MarioMusic music) {
		if (ModConfig.enableMarioMusic) {
			stopMusic();
			Minecraft.getMinecraft().getSoundHandler().playSound(music);
			Jukebox.music = music;
		}
	}

	/**
	 * Stops all currently playing music.
	 */
	public static void stopMusic() {
		if (music != null) {
			if (Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(music)) {
				Minecraft.getMinecraft().getSoundHandler().stopSound(music);
			}
			music.stop();
			music = null;
		}
	}

	/**
	 * Checks if the sound is playing.
	 * 
	 * @param sound
	 *            The sound to check for
	 * @return Whether or not it is playing
	 */
	public static boolean isMusicPlaying(ResourceLocation sound) {
		return music != null ? music.getSoundLocation().equals(sound) : false;
	}

	/**
	 * Plays a sound effect.
	 * 
	 * @param music
	 *            The sound effect to play
	 */
	public static void playSoundEffect(ResourceLocation sound, float pitch, float volume) {
		playSoundEffect(new MarioSFX(sound, pitch, volume));
	}

	/**
	 * Plays a sound effect.
	 * 
	 * @param sound
	 *            The sound effect to play
	 */
	private static void playSoundEffect(MarioSFX sound) {
		if (ModConfig.enableMarioSFX) {
			if (!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(sound) && addSoundEffect(sound)) {
				Minecraft.getMinecraft().getSoundHandler().playSound(sound);
			}
		}
	}

	/**
	 * Stops all currently playing sound effects.
	 */
	public static void stopSoundEffects() {
		for (int i = 0; i < soundEffects.length; i++) {
			MarioSFX soundEffect = soundEffects[i];
			if (soundEffect != null) {
				if (Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(soundEffect)) {
					Minecraft.getMinecraft().getSoundHandler().stopSound(soundEffect);
				}
				soundEffect = null;
			}
		}
	}

	/**
	 * Checks if the sound is playing.
	 * 
	 * @param sound
	 *            The sound to check for
	 * @return Whether or not it is playing
	 */
	public static boolean isSoundEffectPlaying(ResourceLocation sound) {
		for (int i = 0; i < soundEffects.length; i++) {
			MarioSFX soundEffect = soundEffects[i];
			if (soundEffect != null && soundEffect.getSoundLocation().equals(sound)) {
				return true;
			}
		}
		return false;
	}
}