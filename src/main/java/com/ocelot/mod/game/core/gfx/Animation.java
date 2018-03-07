package com.ocelot.mod.game.core.gfx;

public class Animation {

	private Sprite[] frames;
	private int currentFrame;

	private long startTime;
	private long delay;

	private int numTimesPlayed;

	public Animation() {
		this.frames = new Sprite[0];
		this.currentFrame = 0;
		this.delay = -1;
		this.numTimesPlayed = 0;
	}

	public void update() {
		if (delay < 0)
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

	public void restart() {
		this.currentFrame = 0;
		this.startTime = System.nanoTime();
		this.numTimesPlayed = 0;
	}

	public int getFrame() {
		return currentFrame;
	}

	public Sprite getSprite() {
		return frames[currentFrame];
	}

	public Sprite[] getFrames() {
		return frames;
	}

	public int getNumTimesPlayed() {
		return numTimesPlayed;
	}

	public boolean hasPlayedOnce() {
		return numTimesPlayed > 0;
	}

	public void setFrames(Sprite... frames) {
		this.frames = frames;
		this.restart();
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
}