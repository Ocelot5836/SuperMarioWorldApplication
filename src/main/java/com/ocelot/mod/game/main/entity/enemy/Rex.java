package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.entity.IPlayerDamager;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.ai.AIBasicWalk;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Rex extends Enemy implements IPlayerDamagable, IPlayerDamager {

	public static final BufferedImage REX_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/enemy/rex.png"));

	private boolean big;

	private int currentAction;
	private Sprite sprite;
	private BufferedAnimation animation;

	private static int[] delays = { -1, 200, 150, -1, 50, 300 };
	private static List<BufferedImage[]> sprites;

	public static final int IDLE_BIG = 0;
	public static final int WALKING_SIDE_BIG = 1;
	public static final int CRUSH_BIG = 2;
	public static final int IDLE_SMALL = 3;
	public static final int WALKING_SIDE_SMALL = 4;
	public static final int CRUSH_SMALL = 5;

	public Rex(GameTemplate game) {
		this(game, 0, 0);
	}

	public Rex(GameTemplate game, double x, double y) {
		super(game);
		delays = new int[]{ -1, 200, 150, -1, 25, 300 };
		this.setSize(20, 16);
		this.setPosition(x, y);
		this.setLastPosition(x, y);

		this.big = true;

		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		if (this.sprites == null) {
			this.sprites = new ArrayList<BufferedImage[]>();
			this.loadSprites();
		}
		this.setAnimation(IDLE_BIG);

		this.moveSpeed = 0.15;
		this.maxSpeed = 0.5;
		this.stopSpeed = 0.25;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.0;
		this.stopJumpSpeed = 0.3;
	}

	private void loadSprites() {
		BufferedImage[] walkingBig = new BufferedImage[] { REX_SHEET.getSubimage(0, 0, 20, 32), REX_SHEET.getSubimage(20, 0, 20, 32) };
		BufferedImage[] walkingSmall = new BufferedImage[] { REX_SHEET.getSubimage(0, 32, 16, 16), REX_SHEET.getSubimage(0, 48, 16, 16) };

		sprites.add(Lib.asArray(walkingBig[0]));
		sprites.add(walkingBig);
		sprites.add(Lib.asArray(REX_SHEET.getSubimage(16, 32, 18, 24)));

		sprites.add(Lib.asArray(walkingSmall[0]));
		sprites.add(walkingSmall);
		sprites.add(Lib.asArray(REX_SHEET.getSubimage(16, 56, 16, 8)));
	}

	@Override
	public void initAI() {
		super.registerAI(new AIBasicWalk());
	}

	@Override
	public void update() {
		super.update();

		boolean updateAnimations = true;

		if (currentAction == CRUSH_BIG || currentAction == CRUSH_SMALL) {
			updateAnimations = false;
			if (animation.hasPlayedOnce()) {
				updateAnimations = true;
				if (currentAction == CRUSH_SMALL) {
					setDead();
				}
				getNextPosition();
				getNextPosition();
				getNextPosition();
			}
		}

		if (updateAnimations) {
			getNextPosition();
			getNextPosition();
			getNextPosition();
			
			if (big) {
				if (left || right) {
					if (currentAction != WALKING_SIDE_BIG) {
						currentAction = WALKING_SIDE_BIG;
						this.setAnimation(currentAction);
					}
				} else {
					if (currentAction != IDLE_BIG) {
						currentAction = IDLE_BIG;
						this.setAnimation(currentAction);
					}
				}
			} else {
				if (left || right) {
					if (currentAction != WALKING_SIDE_SMALL) {
						currentAction = WALKING_SIDE_SMALL;
						this.setAnimation(currentAction);
					}
				} else {
					if (currentAction != IDLE_SMALL) {
						currentAction = IDLE_SMALL;
						this.setAnimation(currentAction);
					}
				}
			}
		}

		this.animation.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (right)
			facingRight = true;
		if (left)
			facingRight = false;

		sprite.setData(animation.getImage());
		if (facingRight) {
			sprite = Lib.flipHorizontal(sprite);
		}

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight());
	}

	public void setBig(boolean big) {
		this.big = big;
		if (big) {
			this.maxSpeed = 0.5;
		} else {
			this.maxSpeed = 2.75;
		}
	}

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.size()) {
			this.animation.setFrames(this.sprites.get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(animation));
		this.animation.setDelay(this.delays[animation]);
	}

	@Override
	public boolean damagePlayer(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible) {
		if (sideHit != EnumDirection.UP && !isPlayerInvincible) {
			player.damage();
			return true;
		}
		return false;
	}

	@Override
	public void damageEnemy(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible) {
		if (sideHit == EnumDirection.UP && !isPlayerInvincible) {
			player.setPosition(player.getX(), y - cheight);
			if (isPlayerSpinning) {
				defaultSpinStompEnemy(player);
				setDead();
			} else {
				player.setJumping(true);
				player.setFalling(false);
				defaultStompEnemy(player);
				player.getProperties().increaseScore(Lib.getScoreFromJumps(player.getProperties().getEnemyJumpCount()));
				level.add(new TextFX(game, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0, -0.4, Lib.getScoreFromJumps(player.getProperties().getEnemyJumpCount()) + "", 0xffffff, 1));
				if (big) {
					setBig(false);
					currentAction = CRUSH_BIG;
					setAnimation(CRUSH_BIG);
				} else {
					currentAction = CRUSH_SMALL;
					setAnimation(CRUSH_SMALL);
				}
			}
		}
	}
}