package com.ocelot.mod.game.main.entity.enemy;

import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IDamager;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.entity.ai.AIBasicWalk;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class Rex extends Enemy implements IDamagable, IDamager {

	public static final ResourceLocation REX_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/enemy/rex.png");

	private boolean big;

	private int currentAction;
	private Animation<Sprite> animation;

	private static int[] delays = { -1, 200, 150, -1, 50, 300 };
	private static List<Sprite[]> sprites;

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
		delays = new int[] { -1, 200, 150, -1, 25, 300 };
		this.setSize(20, 16);
		this.setPosition(x, y);
		this.setLastPosition(x, y);

		this.big = true;

		this.animation = new Animation<Sprite>();
		if (sprites == null) {
			sprites = new ArrayList<Sprite[]>();
			loadSprites();
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

	private static void loadSprites() {
		Sprite[] walkingBig = Lib.asArray(new Sprite(REX_SHEET, 0, 0, 20, 32, 40, 64), new Sprite(REX_SHEET, 20, 0, 20, 32, 40, 64));
		Sprite[] walkingSmall = Lib.asArray(new Sprite(REX_SHEET, 0, 32, 16, 16, 40, 64), new Sprite(REX_SHEET, 0, 48, 16, 16, 40, 64));

		sprites.add(Lib.asArray(walkingBig[0]));
		sprites.add(walkingBig);
		sprites.add(Lib.asArray(new Sprite(REX_SHEET, 16, 32, 18, 24, 40, 64)));

		sprites.add(Lib.asArray(walkingSmall[0]));
		sprites.add(walkingSmall);
		sprites.add(Lib.asArray(new Sprite(REX_SHEET, 16, 56, 16, 8, 40, 64)));
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

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.animation.get().render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - this.animation.get().getHeight(), facingRight ? 0x01 : 0x00);
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
		if (animation < 0 || animation >= sprites.size()) {
			this.animation.setFrames(sprites.get(0));
			this.animation.setDelay(delays[0]);
		}
		this.animation.setFrames(sprites.get(animation));
		this.animation.setDelay(delays[animation]);
	}

	@Override
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (currentAction != CRUSH_BIG && currentAction != CRUSH_SMALL) {
			if (entity instanceof IDamagable) {
				if (sideHit != EnumDirection.UP) {
					((IDamagable) entity).takeDamage(this, MarioDamageSource.REX, sideHit, false);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill) {
		if (currentAction != CRUSH_BIG && currentAction != CRUSH_SMALL) {
			if (source == MarioDamageSource.SHELL || source.isPlayerProjectile()) {
				defaultKillEntity(this);
				return true;
			}

			if (entity instanceof Player) {
				Player player = (Player) entity;
				if (sideHit == EnumDirection.UP && !this.invulnerable) {
					player.setPosition(player.getX(), y - cheight);
					if (isInstantKill) {
						defaultSpinStompEnemy(player);
						setDead();
						return true;
					} else {
						player.setJumping(true);
						player.setFalling(false);
						defaultStompEnemy(player);
						if (big) {
							setBig(false);
							currentAction = CRUSH_BIG;
							setAnimation(CRUSH_BIG);
						} else {
							currentAction = CRUSH_SMALL;
							setAnimation(CRUSH_SMALL);
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}