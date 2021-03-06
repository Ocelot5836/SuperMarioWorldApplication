package com.ocelot.mod.game.core.gfx;

import com.ocelot.mod.game.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A background that can be rendered into the game. Has simple parralex support.
 * 
 * @author Ocelot5836
 */
public class Background {

	private Sprite[] images;
	private Animation<Sprite> animation;

	private double lastX;
	private double lastY;
	private double x;
	private double y;
	private double startX;
	private double startY;
	private double dx;
	private double dy;
	private double heightScale;

	private double moveScale;

	/**
	 * Creates a new background with the specified sprite and move scale.
	 * 
	 * @param image
	 *            The image to render
	 * @param moveScale
	 *            The scale at which is moves when it's position is set
	 */
	public Background(Sprite image, double moveScale) {
		this(image, moveScale, -1);
	}

	/**
	 * Creates a new background with the specified sprite, move scale, and height scale.
	 * 
	 * @param image
	 *            The image to render
	 * @param moveScale
	 *            The scale at which is moves when it's position is set
	 * @param heightScale
	 *            The scale of the height in pixels. Used for advanced backgrounds
	 */
	public Background(Sprite image, double moveScale, double heightScale) {
		this(new Sprite[] { image }, -1, moveScale, heightScale);
	}

	/**
	 * Creates a new background with the specified sprite and move scale.
	 * 
	 * @param frames
	 *            The images to render in an animation pattern
	 * @param delay
	 *            The time it takes to switch from 1 sprite to another
	 * @param moveScale
	 *            The scale at which is moves when it's position is set
	 */
	public Background(Sprite[] frames, long delay, double moveScale) {
		this(frames, delay, moveScale, -1);
	}

	/**
	 * Creates a new background with the specified sprite, move scale, and height scale.
	 * 
	 * @param frames
	 *            The images to render in an animation pattern
	 * @param delay
	 *            The time it takes to switch from 1 sprite to another
	 * @param moveScale
	 *            The scale at which is moves when it's position is set
	 * @param heightScale
	 *            The scale of the height in pixels. Used for advanced backgrounds
	 */
	public Background(Sprite[] frames, long delay, double moveScale, double heightScale) {
		this.images = frames;
		this.moveScale = moveScale;
		this.heightScale = heightScale;
		this.animation = new Animation();
		this.animation.setFrames(images);
		this.animation.setDelay(delay);
	}

	/**
	 * Creates a new background that copies the supplied background's properties.
	 * 
	 * @param bg
	 *            The background to clone
	 */
	public Background(Background bg) {
		this.images = bg.images;
		this.moveScale = bg.moveScale;
		this.x = bg.x;
		this.y = bg.y;
		this.dx = bg.dx;
		this.dy = bg.dy;
		this.heightScale = bg.heightScale;
	}

	/**
	 * Sets the position of the background.
	 * 
	 * @param x
	 *            The new x position of the background
	 * @param y
	 *            The new y position of the background
	 */
	public void setPosition(double x, double y) {
		this.x = x * moveScale % Game.WIDTH;
		this.y = y * moveScale % Game.HEIGHT;
	}

	/**
	 * Sets the starting position of the background. Used as the base offset from the map's x and y.
	 * 
	 * @param x
	 *            The x offset for the background
	 * @param y
	 *            The y offset for the background
	 */
	public void setStartingPosition(double x, double y) {
		this.startX = x;
		this.startY = y;
	}

	/**
	 * The auto scroll speed in the two directions.
	 * 
	 * @param dx
	 *            The x scrolling speed
	 * @param dy
	 *            The y scrolling speed
	 */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Updates the background. This must be called or the background will flash between the origional position and the new position.
	 */
	public void update() {
		this.animation.update();
		this.lastX = x;
		this.lastY = y;
		this.setPosition(x + dx, y + dy);
	}

	/**
	 * Renders the background to the screen.
	 */
	public void render() {
		GlStateManager.pushMatrix();
		GlStateManager.scale(1 / this.heightScale, 1 / this.heightScale, 0);
		Sprite image = this.animation.get();
		double x = -(this.lastX + (this.x - this.lastX) * Minecraft.getMinecraft().getRenderPartialTicks()) + this.startX;
		double y = -(this.lastY + (this.y - this.lastY) * Minecraft.getMinecraft().getRenderPartialTicks()) + this.startY;
		if (this.heightScale < 0) {
			image.render(x, y, Game.WIDTH, Game.HEIGHT);

			if (x < 0) {
				image.render(x + Game.WIDTH, y, Game.WIDTH, Game.HEIGHT);
			}

			if (x > 0) {
				image.render(x - Game.WIDTH, y, Game.WIDTH, Game.HEIGHT);
			}
		} else {
			image.render(x, y, Game.WIDTH, (int) (image.getHeight() / (1 / this.heightScale)));

			if (x < 0) {
				image.render(x + Game.WIDTH, y, Game.WIDTH, (int) (image.getHeight() / (1 / this.heightScale)));
			}

			if (x > 0) {
				image.render(x - Game.WIDTH, y, Game.WIDTH, (int) (image.getHeight() / (1 / this.heightScale)));
			}
		}
		GlStateManager.popMatrix();
	}
}