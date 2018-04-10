package com.ocelot.mod.game.core.entity.fx;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic particle that has physics.
 * 
 * @author Ocelot5836
 */
public abstract class Particle extends EntityFX {

	public static final BufferedImage PARTICLE_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/effect/particles.png"));
	public static final Sprite DEFAULT_SPRITE = new Sprite(PARTICLE_SHEET.getSubimage(0, 0, 8, 8));
	public static final Sprite CHEESE_SPRITE = new Sprite(PARTICLE_SHEET.getSubimage(56, 56, 8, 8));

	protected static final Map<Integer, Sprite> SPRITES = Maps.<Integer, Sprite>newHashMap();

	static {
		SPRITES.put(0, DEFAULT_SPRITE);
		SPRITES.put(1, CHEESE_SPRITE);
	}

	/** The particle's sprite. */
	protected Sprite sprite;

	private Stopwatch timer;
	private long life;

	/** The x position the particle wants to travel to. */
	protected double xa;
	/** The y position the particle wants to travel to. */
	protected double ya;
	/** Used for physics */
	protected double xx, yy, zz, lastZ, za;

	/** Particle attributes. */
	protected double tileBouncePower, weight, dragPercentLoss;

	public Particle(GameTemplate game, double x, double y, int life, int lifeDifference, double bounce, double tileBouncePower, double weight, double dragPercentLoss) {
		this(game, x, y, life, lifeDifference, bounce, tileBouncePower, weight, dragPercentLoss, DEFAULT_SPRITE);
	}

	public Particle(GameTemplate game, double x, double y, int life, int lifeDifference, double bounce, double tileBouncePower, double weight, double dragPercentLoss, Sprite sprite) {
		super(game);
		this.setPosition(x, y);
		this.setSize(sprite.getWidth(), sprite.getHeight());
		this.xx = x;
		this.yy = y;
		this.lastX = x;
		this.lastY = y;
		this.life = (life + (random.nextInt(lifeDifference) - lifeDifference / 2)) / 20;
		this.tileBouncePower = tileBouncePower;
		this.weight = weight;
		this.dragPercentLoss = Math.min(dragPercentLoss, 1.0);
		this.sprite = sprite;

		this.xa = random.nextGaussian();
		this.ya = random.nextGaussian();
		this.zz = random.nextFloat() * bounce;

		this.timer = Stopwatch.createStarted();
	}

	@Override
	public void update() {
		super.update();
		this.lastZ = zz;

		if (timer.elapsed(TimeUnit.SECONDS) >= life) {
			setDead();
		}

		getNextPosition();
		getNextPosition();
		getNextPosition();
	}

	private void move(double x, double y) {
		if (hasCollided(x + xa, y + ya - za, cwidth, cheight)) {
			this.ya *= -tileBouncePower;
			this.za *= -tileBouncePower;
			if (hasCollided(x + xa * 1.1, y + ya - za, cwidth, cheight))
				this.xa *= -tileBouncePower;
			else
				this.xa *= 1.0 - this.dragPercentLoss;
		}
		this.xx += xa;
		this.yy += ya;
		this.zz += za;
	}

	private void getNextPosition() {
		za -= weight;

		this.x = xx;
		this.y = yy - zz;

		move(x, y);
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		double tileMapX = tileMap.getLastX() + tileMap.getPartialRenderX();
		double tileMapY = tileMap.getLastY() + tileMap.getPartialRenderY();
		sprite.render(posX - tileMapX - cwidth / 2, posY - tileMapY - cheight / 2);
	}
}