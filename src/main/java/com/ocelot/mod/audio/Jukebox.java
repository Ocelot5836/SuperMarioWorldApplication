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
	public static boolean isPlaying(ResourceLocation sound) {
		for (int i = 0; i < musics.size(); i++) {
			if (musics.get(i).getSoundLocation().equals(sound)) {
				return true;
			}
		}
		return false;
	}
}