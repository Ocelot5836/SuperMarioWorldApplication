package com.ocelot.mod.game.entity;

import java.awt.image.BufferedImage;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.ocelot.mod.Lib;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.gui.Guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Player extends Mob {

	private boolean running;
	private boolean small;

	private double baseMaxSpeed;
	private double baseStopSpeed;
	private int runAnimationSpeed;

	private int currentAction;
	private Sprite sprite;
	private List<BufferedImage[]> sprites = Lists.<BufferedImage[]>newArrayList();
	private BufferedAnimation animation;
	private int[] numFrames = { 1, 2, 1, 1, 3, 3 };
	private int[] delays = { -1, 100, -1, -1, 100, 100 };

	private static final int IDLE_SMALL = 0;
	private static final int WALKING_SMALL = 1;
	private static final int JUMPING_SMALL = 2;
	private static final int FALLING_SMALL = 3;
	private static final int SWIMMING_SMALL = 4;
	private static final int SWIMMING_STROKE_SMALL = 5;

	public Player(GameTemplate game) {
		this(game, 0, 0);
	}

	public Player(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.setSize(12, 14);
		this.setMaxHealth(5);
		this.setHealth(this.getMaxHealth());
		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		this.loadSprites();

		this.moveSpeed = 0.3;
		this.maxSpeed = baseMaxSpeed = 1.6;
		this.stopSpeed = baseStopSpeed = 0.4;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.8;
		this.stopJumpSpeed = 0.3;

		this.small = true;
		this.runAnimationSpeed = 100;

		this.facingRight = true;
	}

	private void loadSprites() {
		int width = 16;
		int height = 24;
		BufferedImage sheet = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/mario.png"));

		for (int i = 0; i < numFrames.length; i++) {
			BufferedImage[] sprite = new BufferedImage[numFrames[i]];
			for (int j = 0; j < numFrames[i]; j++) {
				sprite[j] = sheet.getSubimage(j * width, i * height, width, height);
			}
			this.sprites.add(sprite);
		}
		this.animation.setFrames(this.sprites.get(0));
	}

	private void getNextPosition() {
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx += stopSpeed;
			}
			setRunningAnimations();
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx -= stopSpeed;
			}
			setRunningAnimations();
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		if (falling) {
			dy += fallSpeed;
			if (dy > 0)
				jumping = false;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed) {
				dy = maxFallSpeed;
			}
		}

		if (running) {
			maxSpeed = baseMaxSpeed * 2;
			stopSpeed++;
			if (stopSpeed > baseStopSpeed * 4)
				stopSpeed = baseStopSpeed * 4;
		} else {
			maxSpeed = baseMaxSpeed;
			stopSpeed--;
			if (stopSpeed < baseStopSpeed)
				stopSpeed = baseStopSpeed;
			runAnimationSpeed += 3;
			if (runAnimationSpeed > 100) {
				runAnimationSpeed = 100;
			}
		}

		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}

	private void setRunningAnimations() {
		if (running) {
			runAnimationSpeed -= 3;
			if (runAnimationSpeed < 20)
				runAnimationSpeed = 20;
		}
	}

	public void openGui(int id) {
		if (game instanceof Game) {
			((Game) game).currentDisplayedGui = Guis.INSTANCE.openGui(id, this).setSizeAndWorld(game, this);
		}
	}

	public void closeGui() {
		if (game instanceof Game) {
			((Game) game).currentDisplayedGui = null;
		}
	}

	@Override
	public void update() {
		super.update();
		getNextPosition();
		getNextPosition();
		getNextPosition();

		if (dy > 0) {
			if (currentAction != FALLING_SMALL) {
				currentAction = FALLING_SMALL;
				this.setAnimation(currentAction);
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING_SMALL) {
				currentAction = JUMPING_SMALL;
				this.setAnimation(currentAction);
			}
		} else if (left || right) {
			if (currentAction != WALKING_SMALL) {
				currentAction = WALKING_SMALL;
				this.setAnimation(currentAction);
			}
			this.animation.setDelay(runAnimationSpeed);
		} else {
			if (currentAction != IDLE_SMALL) {
				currentAction = IDLE_SMALL;
				this.setAnimation(currentAction);
			}
		}

		this.animation.update();

		if (right)
			facingRight = true;
		if (left)
			facingRight = false;

		if (game instanceof Game) {
			if (((Game) game).currentDisplayedGui == null) {
				this.left = Keyboard.isKeyDown(Keyboard.KEY_A);
				this.right = Keyboard.isKeyDown(Keyboard.KEY_D);
				this.running = Keyboard.isKeyDown(Keyboard.KEY_E);

				this.jumping = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
			}
		} else {
			this.left = Keyboard.isKeyDown(Keyboard.KEY_A);
			this.right = Keyboard.isKeyDown(Keyboard.KEY_D);
			this.running = Keyboard.isKeyDown(Keyboard.KEY_E);

			this.jumping = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);

		if (flinching) {
			if (getFlinchElapsedTime() / 100 % 2 == 0) {
				return;
			}
		}

		sprite.setData(animation.getImage());
		if (!facingRight) {
			sprite = Lib.flipHorizontal(sprite);
		}

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - tileMap.getX() * 2 - cwidth / 2 - 2, posY - tileMap.getY() * 2 - cheight / 2 - 10);
	}

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.size()) {
			this.animation.setFrames(this.sprites.get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(animation));
		this.animation.setDelay(this.delays[animation]);
	}
}