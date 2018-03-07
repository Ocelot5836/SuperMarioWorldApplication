package com.ocelot.mod.game.core.gfx;

import com.ocelot.mod.game.Game;

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

	private Sprite image;

	private double x;
	private double y;
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
		this.image = image;
		this.moveScale = moveScale;
		this.heightScale = heightScale;
	}

	/**
	 * Creates a new background that copies the supplied background's properties.
	 * 
	 * @param bg
	 *            The background to clone
	 */
	public Background(Background bg) {
		this.image = bg.image;
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
		this.x = (x * moveScale) % Game.WIDTH;
		this.y = (y * moveScale) % Game.HEIGHT;
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
	 * Updates the background. Used only if it has a vector not equal to zero.
	 */
	public void update() {
		setPosition(x + dx, y + dy);
	}

	/**
	 * Renders the background to the screen.
	 */
	public void render() {
		if (heightScale < 0) {
			image.render((int) x, (int) y, Game.WIDTH, Game.HEIGHT);

			if (x < 0) {
				image.render((int) (x + Game.WIDTH), (int) y, Game.WIDTH, Game.HEIGHT);
			}

			if (x > 0) {
				image.render((int) (x - Game.WIDTH), (int) y, Game.WIDTH, Game.HEIGHT);
			}
		} else {
			image.render((int) x, (int) y, Game.WIDTH, (int) (image.getHeight() / (1 / heightScale)));

			if (x < 0) {
				image.render((int) (x + Game.WIDTH), (int) y, Game.WIDTH, (int) (image.getHeight() / (1 / heightScale)));
			}

			if (x > 0) {
				image.render((int) (x - Game.WIDTH), (int) y, Game.WIDTH, (int) (image.getHeight() / (1 / heightScale)));
			}
		}
	}
}