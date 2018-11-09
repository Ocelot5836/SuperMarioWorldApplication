package com.ocelot.mod.game.core.gfx;

import javax.annotation.Nullable;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * An animation that uses sprites.
 * 
 * @author Ocelot5836
 * @param <T>
 *            The type of object that is returned from the animation
 */
public class Animation<T> {

	private T[] frames;
	private int currentFrame;

	private long startTime;
	private long delay;

	private int numTimesPlayed;

	public Animation() {
		this.frames = null;
		this.currentFrame = 0;
		this.delay = -1;
		this.numTimesPlayed = 0;
	}

	/**
	 * Updates the animation.
	 */
	public void update() {
		if (delay < 0 || frames == null)
			return;

		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame >= frames.length) {
			currentFrame = 0;
			numTimesPlayed++;
		}
	}

	/**
	 * Restarts the animation.
	 */
	public void restart() {
		this.currentFrame = 0;
		this.startTime = System.nanoTime();
		this.numTimesPlayed = 0;
	}

	/**
	 * @return The current frame
	 */
	public int getFrame() {
		return currentFrame;
	}

	/**
	 * @return The current sprite to be displayed
	 */
	@Nullable
	public T get() {
		return frames == null ? null : frames[currentFrame];
	}

	/**
	 * @return The frames
	 */
	@Nullable
	public T[] getFrames() {
		return frames;
	}

	/**
	 * @return How many times this animation has played
	 */
	public int getNumTimesPlayed() {
		return numTimesPlayed;
	}

	/**
	 * @return If this animation has played once
	 */
	public boolean hasPlayedOnce() {
		return numTimesPlayed > 0;
	}

	/**
	 * Sets the frames in the animation.
	 * 
	 * @param frames
	 *            The frames in the animation
	 */
	public void setFrames(T... frames) {
		this.frames = frames;
		this.restart();
	}

	/**
	 * Sets the animation switch delay.
	 * 
	 * @param delay
	 *            The new delay of the animation
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * Sets the currently selected frame.
	 * 
	 * @param currentFrame
	 *            The new selected frame
	 */
	public void setFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
}