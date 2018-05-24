package com.ocelot.mod.audio;

import java.util.List;

import com.google.common.collect.Lists;
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

	private static List<MarioMusic> musics = Lists.newArrayList();
	private static List<MarioSFX> soundEffects = Lists.newArrayList();

	/**
	 * Plays a piece of music.
	 * 
	 * @param music
	 *            The music to play
	 */
	@SideOnly(Side.CLIENT)
	public static void playMusic(ResourceLocation music) {
		playMusic(new MarioMusic(music));
	}

	/**
	 * Plays a piece of mario music.
	 * 
	 * @param music
	 *            The music to play
	 */
	@SideOnly(Side.CLIENT)
	private static void playMusic(MarioMusic music) {
		if (ModConfig.enableMarioMusic) {
			Minecraft.getMinecraft().getSoundHandler().playSound(music);
			musics.add(music);
		}
	}

	/**
	 * Stops all currently playing music.
	 */
	@SideOnly(Side.CLIENT)
	public static void stopMusic() {
		for (int i = 0; i < musics.size(); i++) {
			MarioMusic music = musics.remove(i);
			Minecraft.getMinecraft().getSoundHandler().stopSound(music);
			music.stop();
		}
	}

	/**
	 * Checks if the sound is playing.
	 * 
	 * @param sound
	 *            The sound to check for
	 * @return Whether or not it is playing
	 */
	@SideOnly(Side.CLIENT)
	public static boolean isMusicPlaying(ResourceLocation sound) {
		for (int i = 0; i < musics.size(); i++) {
			if (musics.get(i).getSoundLocation().equals(sound)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Plays a sound effect.
	 * 
	 * @param music
	 *            The sound effect to play
	 */
	@SideOnly(Side.CLIENT)
	public static void playSoundEffect(ResourceLocation sound, float pitch, float volume) {
		playSoundEffect(new MarioSFX(sound, pitch, volume));
	}

	/**
	 * Plays a sound efefct.
	 * 
	 * @param sound
	 *            The sound effect to play
	 */
	@SideOnly(Side.CLIENT)
	private static void playSoundEffect(MarioSFX sound) {
		if (ModConfig.enableMarioSFX) {
			Minecraft.getMinecraft().getSoundHandler().playSound(sound);
			soundEffects.add(sound);
		}
	}

	/**
	 * Stops all currently playing sound effects.
	 */
	@SideOnly(Side.CLIENT)
	public static void stopSoundEffects() {
		for (int i = 0; i < soundEffects.size(); i++) {
			MarioSFX music = soundEffects.remove(i);
			Minecraft.getMinecraft().getSoundHandler().stopSound(music);
		}
	}

	/**
	 * Checks if the sound is playing.
	 * 
	 * @param sound
	 *            The sound to check for
	 * @return Whether or not it is playing
	 */
	@SideOnly(Side.CLIENT)
	public static boolean isSoundEffectPlaying(ResourceLocation sound) {
		for (int i = 0; i < soundEffects.size(); i++) {
			if (soundEffects.get(i).getSoundLocation().equals(sound)) {
				return true;
			}
		}
		return false;
	}
}