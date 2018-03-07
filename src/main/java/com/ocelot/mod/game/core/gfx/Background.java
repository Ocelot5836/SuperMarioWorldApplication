package com.ocelot.mod.game.core.gfx;

import com.ocelot.mod.game.Game;

public class Background {

	private Sprite image;

	private double x;
	private double y;
	private double dx;
	private double dy;
	private double heightScale;

	private double moveScale;

	public Background(Sprite image, double moveScale) {
		this(image, moveScale, -1);
	}

	public Background(Sprite image, double moveScale, double heightScale) {
		this.image = image;
		this.moveScale = moveScale;
		this.heightScale = heightScale;
	}

	public Background(Background bg) {
		this.image = bg.image;
		this.moveScale = bg.moveScale;
		this.x = bg.x;
		this.y = bg.y;
		this.dx = bg.dx;
		this.dy = bg.dy;
		this.heightScale = bg.heightScale;
	}

	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % Game.WIDTH;
		this.y = (y * moveScale) % Game.HEIGHT;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void update() {
		setPosition(x + dx, y + dy);
	}

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
			image.render((int) x, (int) y, Game.WIDTH, (int) (image.getHeight() / (1/heightScale)));

			if (x < 0) {
				image.render((int) (x + Game.WIDTH), (int) y, Game.WIDTH, (int) (image.getHeight() / (1/heightScale)));
			}

			if (x > 0) {
				image.render((int) (x - Game.WIDTH), (int) y, Game.WIDTH, (int) (image.getHeight() / (1/heightScale)));
			}
		}
	}
}