package com.ocelot.mod.game.core.level;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.audio.Jukebox;

import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Contains the basic properties most level will require.
 * 
 * @author Ocelot5836
 */
public class LevelProperties {

	private long time;
	private long currTime;
	private ResourceLocation music;
	private ResourceLocation musicFast;

	private boolean playing;
	private int endLoop;

	private Stopwatch timer;

	public LevelProperties(long time, ResourceLocation music, ResourceLocation musicFast, int endLoop) {
		this.time = time;
		this.currTime = time;
		this.music = music;
		this.musicFast = musicFast;

		this.playing = false;
		this.endLoop = endLoop;

		this.timer = Stopwatch.createStarted();
	}

	public void update() {
		if (this.timer.elapsed(TimeUnit.SECONDS) >= 1 && this.currTime > 0) {
			this.currTime--;
			this.timer.reset().start();
		}

		if (this.currTime < 0) {
			this.currTime = 0;
		}

		if (playing) {
			if (musicFast != null && musicFast != music) {
				if (Jukebox.isMusicPlaying(music)) {
					if (currTime <= 100) {
						Jukebox.stopMusic();
						Jukebox.playMusic(musicFast, endLoop);
					}
				}
			}
		}
	}

	public void playMusic() {
		playing = true;
		if (currTime > 100) {
			if (music != null) {
				if (endLoop > 0) {
					Jukebox.playMusic(music, endLoop);
				} else {
					Jukebox.playMusic(music, endLoop);
				}
			}
		} else {
			if (musicFast != null) {
				if (endLoop > 0) {
					Jukebox.playMusic(musicFast, endLoop);
				} else {
					Jukebox.playMusic(musicFast, endLoop);
				}
			}
		}
	}

	public void stopMusic() {
		playing = false;
		Jukebox.stopMusic();
	}

	public long getTime() {
		return time;
	}

	public long getCurrTime() {
		return currTime;
	}

	public ResourceLocation getMusic() {
		return music;
	}

	public ResourceLocation getMusicFast() {
		return musicFast;
	}
}