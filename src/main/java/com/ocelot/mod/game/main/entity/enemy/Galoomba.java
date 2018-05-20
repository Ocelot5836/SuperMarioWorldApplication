package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.ai.AIBasicWalker;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Galoomba extends Enemy {

	public static final BufferedImage GALOOMBA_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/enemy/galoomba.png"));

	private int currentAction;
	private Sprite sprite;
	private BufferedAnimation animation;

	private static int[] delays = { 100, 100, 100, -1 };
	private static List<BufferedImage[]> sprites;

	public static final int WALKING_SIDE = 0;
	public static final int JUMPING = 1;
	public static final int FALLING = 2;
	public static final int IDLE = 3;

	public Galoomba(GameTemplate game) {
		this(game, 0, 0);
	}

	public Galoomba(GameTemplate game, double x, double y) {
		super(game);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;
		this.setSize(16, 16);
		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();

		if (sprites == null) {
			this.sprites = new ArrayList<BufferedImage[]>();
			this.loadSprites();
		}
		
		this.setAnimation(IDLE);
	}

	private Galoomba(GameTemplate game, Galoomba.Item item) {
		this(game, item.getX(), item.getY());
	}

	private void loadSprites() {
		BufferedImage[] walking = Lib.asArray(GALOOMBA_SHEET.getSubimage(0, 0, 16, 16), GALOOMBA_SHEET.getSubimage(16, 0, 16, 16));
		sprites.add(walking);
		sprites.add(walking);
		sprites.add(walking);
		sprites.add(Lib.asArray(walking[0]));
	}

	@Override
	public void initAI() {
		super.registerAI(new AIBasicWalker());
	}

	private void getNextPosition() {
		calculateCorners(xdest, y);

		if ((topLeft && topRight) || (bottomLeft && bottomRight)) {
			facingRight = !facingRight;
		}

		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx += stopSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx -= stopSpeed;
			}
		} else {
			if (dx > 0) {
				dx = 0;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx = 0;
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

		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}

	@Override
	public void update() {
		super.update();

		getNextPosition();
		getNextPosition();
		getNextPosition();
		
		if (dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				this.setAnimation(currentAction);
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				this.setAnimation(currentAction);
			}
		} else if (left || right) {
			if (currentAction != WALKING_SIDE) {
				currentAction = WALKING_SIDE;
				this.setAnimation(currentAction);
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				this.setAnimation(currentAction);
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (right)
			facingRight = true;
		if (left)
			facingRight = false;

		sprite.setData(animation.getImage());
		if (!facingRight) {
			sprite = Lib.flipHorizontal(sprite);
		}

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight());
	}

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.size()) {
			this.animation.setFrames(this.sprites.get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(animation));
		this.animation.setDelay(this.delays[animation]);
	}

	public static class Item extends EntityItem implements IItemCarriable {

		private int timer;

		public Item(GameTemplate game) {
			super(game);
		}

		@Override
		public void update() {
			super.update();

			timer++;
			if (timer >= 100) {
				this.setDead();
				level.add(new Galoomba(game, this));
			}
		}

		@Override
		public void onHeldUpdate(Player player) {
		}

		@Override
		public void onPickup(Player player) {
		}

		@Override
		public void onThrow(Player player, ThrowingType type) {
			setDefaultThrowing(player, type);
		}

		@Override
		public void onDrop(Player player) {
			setDefaultPlacing(player);
		}

		@Override
		public boolean canPickup(Player player) {
			return true;
		}

		@Override
		public boolean canHold(Player player) {
			return timer < 98;
		}
	}
}