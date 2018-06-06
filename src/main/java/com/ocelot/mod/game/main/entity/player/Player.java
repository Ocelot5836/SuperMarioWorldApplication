package com.ocelot.mod.game.main.entity.player;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.IItemCarriable.ThrowingType;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.entity.IPlayerDamager;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.gui.Guis;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(Player.Summonable.class)
public class Player extends Mob {

	private EntityItem item = null;

	private double baseMaxSpeed;
	private double baseStopSpeed;
	private int runAnimationSpeed;

	private PlayerProperties properties;

	private Stopwatch deathAnimationTimer = Stopwatch.createUnstarted();
	private int currentAction;
	private Sprite sprite;
	private List<BufferedImage[]> sprites = Lists.<BufferedImage[]>newArrayList();
	private BufferedAnimation animation;
	private int[] numFrames = { 1, 2, 1, 1, 3, 3, 1, 1, 2, 1, 1, 1, 1, 1, 1 };
	private int[] delays = { -1, 100, -1, -1, 100, 100, -1, -1, 100, 50, -1, -1, -1, -1, -1 };

	private static final int IDLE_SMALL = 0;
	private static final int WALKING_SMALL = 1;
	private static final int JUMPING_SMALL = 2;
	private static final int FALLING_SMALL = 3;
	private static final int SWIMMING_SMALL = 4;
	private static final int SWIMMING_STROKE_SMALL = 5;
	private static final int DUCKING_SMALL = 6;
	private static final int DUCKING_ITEM_SMALL = 7;
	private static final int WALKING_ITEM_SMALL = 8;
	private static final int KICKING_ITEM_SMALL = 9;
	private static final int IDLE_ITEM_SMALL = 10;
	private static final int MIDAIR_ITEM_SMALL = 11;
	private static final int LOOK_UP_SMALL = 12;
	private static final int LOOK_UP_ITEM_SMALL = 13;
	private static final int DEAD = 14;

	public Player(GameTemplate game) {
		this(game, 0, 0);
	}

	public Player(GameTemplate game, double x, double y) {
		super(game);

		if (game instanceof Game) {
			properties = ((Game) game).getPlayerProperties();
		} else {
			properties = new PlayerProperties(game);
		}

		this.enableKeyboardInput(true);
		this.setPosition(x, y);
		this.setLastPosition(x, y);
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

		this.runAnimationSpeed = 100;

		this.facingRight = true;
	}

	private void loadSprites() {
		this.sprites.addAll(Lib.loadSpritesFromBufferedImage(Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/player/mario.png")), 16, 24, numFrames));
		this.animation.setFrames(this.sprites.get(0));
	}

	@Override
	protected void getNextPosition() {
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

		if (!this.properties.isSwimming() && this.properties.isRunning()) {
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
		if (this.properties.isRunning() && (left || right)) {
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
			((Game) game).currentDisplayedGui.onClosed();
			((Game) game).currentDisplayedGui = null;
		}
	}

	@Override
	public void update() {
		super.update();

		if (!this.isDead()) {
			getNextPosition();
			getNextPosition();
			getNextPosition();

			if (this.properties.isKeyboardInputEnabled()) {
				if (game instanceof Game) {
					if (((Game) game).currentDisplayedGui == null) {
						this.left = Keyboard.isKeyDown(Keyboard.KEY_LEFT) && (!this.down || (this.jumping || this.falling));
						this.right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && (!this.down || (this.jumping || this.falling));
						this.down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
						this.up = Keyboard.isKeyDown(Keyboard.KEY_UP) && !this.down;

						this.jumping = Keyboard.isKeyDown(Keyboard.KEY_W);
						this.properties.setRunning(Keyboard.isKeyDown(Keyboard.KEY_E) && !this.down);
						this.properties.setHolding(Keyboard.isKeyDown(Keyboard.KEY_E));
					}
				} else {
					this.left = Keyboard.isKeyDown(Keyboard.KEY_LEFT) && (!this.down || (this.jumping || this.falling));
					this.right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && (!this.down || (this.jumping || this.falling));
					this.down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
					this.up = Keyboard.isKeyDown(Keyboard.KEY_UP) && !this.down;

					this.jumping = Keyboard.isKeyDown(Keyboard.KEY_W);
					this.properties.setRunning(Keyboard.isKeyDown(Keyboard.KEY_E) && !this.down);
					this.properties.setHolding(Keyboard.isKeyDown(Keyboard.KEY_E));
				}
			}

			if (!falling) {
				this.properties.setEnemyJumpCount(0);
			}

			if (!this.properties.isHolding() && !down && !up && item != null) {
				currentAction = KICKING_ITEM_SMALL;
				this.setAnimation(currentAction);
			}

			if (currentAction != KICKING_ITEM_SMALL || animation.hasPlayedOnce()) {
				if (down) {
					if (item != null) {
						if (currentAction != DUCKING_ITEM_SMALL) {
							currentAction = DUCKING_ITEM_SMALL;
							this.setAnimation(currentAction);
						}
					} else {
						if (currentAction != DUCKING_SMALL) {
							currentAction = DUCKING_SMALL;
							this.setAnimation(currentAction);
						}
					}
				} else if (dy > 0) {
					if (item != null) {
						if (currentAction != MIDAIR_ITEM_SMALL) {
							currentAction = MIDAIR_ITEM_SMALL;
							this.setAnimation(currentAction);
						}
					} else {
						if (currentAction != FALLING_SMALL) {
							currentAction = FALLING_SMALL;
							this.setAnimation(currentAction);
						}
					}
				} else if (dy < 0) {
					if (item != null) {
						if (currentAction != MIDAIR_ITEM_SMALL) {
							currentAction = MIDAIR_ITEM_SMALL;
							this.setAnimation(currentAction);
						}
					} else {
						if (currentAction != JUMPING_SMALL) {
							currentAction = JUMPING_SMALL;
							this.setAnimation(currentAction);
						}
					}
				} else if (left || right) {
					if (item != null) {
						if (currentAction != WALKING_ITEM_SMALL) {
							currentAction = WALKING_ITEM_SMALL;
							this.setAnimation(currentAction);
						}
					} else {
						if (currentAction != WALKING_SMALL) {
							currentAction = WALKING_SMALL;
							this.setAnimation(currentAction);
						}
					}
					this.animation.setDelay(runAnimationSpeed);
				} else {
					if (up) {
						if (item != null) {
							if (currentAction != LOOK_UP_ITEM_SMALL) {
								currentAction = LOOK_UP_ITEM_SMALL;
								this.setAnimation(currentAction);
							}
						} else {
							if (currentAction != LOOK_UP_SMALL) {
								currentAction = LOOK_UP_SMALL;
								this.setAnimation(currentAction);
							}
						}
					} else {
						if (item != null) {
							if (currentAction != IDLE_ITEM_SMALL) {
								currentAction = IDLE_ITEM_SMALL;
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
			}

			if (this.down) {
				if (this.cheight != 8) {
					this.y += 3;
					this.lastY = y;
				}
				this.setSize(12, 8);
			} else {
				if (this.cheight != 14) {
					this.y -= 3;
					this.lastY = y;
				}
				this.setSize(12, 14);
			}

			if (right)
				facingRight = true;
			if (left)
				facingRight = false;

			if (jumping && !falling) {
				game.playSound(Sounds.PLAYER_JUMP, 1.0F);
			}

			if (item != null) {
				if (item instanceof IItemCarriable) {
					IItemCarriable carriable = (IItemCarriable) item;
					carriable.onHeldUpdate(this);
				}
				item.updateLastPosition();
				item.setPosition(x, y);
				item.setPosition(x + (facingRight ? 1 : -1) * (item.getWidth() / 2 + 4), y - 2);
			}

			if (this.properties.isHolding()) {
				if (item == null) {
					Entity e = level.getNearestEntity(this);
					if (e != null && e instanceof IItemCarriable && e instanceof EntityItem && e.intersects(this)) {
						IItemCarriable carriable = (IItemCarriable) e;
						if (carriable.canPickup(this)) {
							item = (EntityItem) e;
							e.setDead();
						}
					}
				} else {
					if (this.item instanceof IItemCarriable) {
						IItemCarriable carriable = (IItemCarriable) item;
						if (!carriable.canHold(this)) {
							this.dropCurrentItem(false);
						}
					}
				}
			} else {
				this.dropCurrentItem(!this.down);
			}

			checkAttackAndDamage(level.getEntities());
		}

		this.animation.update();
	}

	private void checkAttackAndDamage(List<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			EnumDirection direction = EnumDirection.UP;

			if (falling || jumping) {
				if (falling) {
					direction = EnumDirection.UP;
				} else {
					direction = EnumDirection.DOWN;
				}
			} else {
				if (dx > 0) {
					direction = EnumDirection.LEFT;
				} else {
					direction = EnumDirection.RIGHT;
				}
			}

			Entity e = entities.get(i);
			boolean flag = false;
			if (!e.intersects(this))
				continue;

			if (e instanceof IPlayerDamager) {
				IPlayerDamager damagable = (IPlayerDamager) e;
				if (!damagable.damagePlayer(this, direction, false, false)) {
					flag = true;
				}
			} else {
				flag = true;
			}

			if (flag) {
				if (e instanceof IPlayerDamagable) {
					IPlayerDamagable damagable = (IPlayerDamagable) e;
					if (e instanceof IItemCarriable) {
						IItemCarriable carriable = (IItemCarriable) e;
						if (!(properties.isHolding() && item == e)) {
							damagable.damageEnemy(this, direction, false, false);
						}
					} else {
						damagable.damageEnemy(this, direction, false, false);
					}
				}
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);

		sprite.setData(animation.getImage());
		if (!facingRight) {
			sprite = Lib.flipHorizontal(sprite);
		}

		if (item != null) {
			item.render(gui, mc, mouseX, mouseY, partialTicks);
		}

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2 - 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight());
	}

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.size()) {
			this.animation.setFrames(this.sprites.get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(animation));
		this.animation.setDelay(this.delays[animation]);
	}

	public void dropCurrentItem(boolean throwItem) {
		if (item != null) {
			item.resetVelocity();
			if (item instanceof IItemCarriable) {
				IItemCarriable carriable = (IItemCarriable) item;
				if (throwItem) {
					carriable.onThrow(this, up ? ThrowingType.UP : ThrowingType.SIDE);
					game.playSound(Sounds.PLAYER_KICK, 1.0F);
				} else {
					carriable.onDrop(this);
				}
			}
			level.add(item.setDead(false));
			item = null;
		}
	}

	public void damage() {
		if (!this.properties.isSmall()) {
			this.properties.setSmall();
		} else {
			this.properties.setDead();
		}
	}

	public void onDeath(GameState state) {

	}

	public Player enableKeyboardInput(boolean enableKeyboardInput) {
		this.properties.setKeyboardInputEnabled(enableKeyboardInput);
		return this;
	}

	public boolean isKeyboardInputEnabled() {
		return this.properties.isKeyboardInputEnabled();
	}

	public Player setRunning(boolean running) {
		this.properties.setRunning(running);
		return this;
	}

	public boolean isRunning() {
		return this.properties.isRunning();
	}

	public PlayerProperties getProperties() {
		return properties;
	}

	public EntityItem getHeldItem() {
		return item;
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 1) {
				try {
					level.add(new Player(game, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (NumberFormatException e) {
					throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".player.summon.numerical"));
				} catch (Exception e) {
					Mod.logger().catching(e);
				}
				if (args.length > 2) {
					try {
						Player player = new Player(game, Double.parseDouble(args[0]), Double.parseDouble(args[1]));
						player.enableKeyboardInput(Boolean.parseBoolean(args[2]));
						level.add(player);
					} catch (NumberFormatException e) {
						throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".player.summon.numerical"));
					} catch (Exception e) {
						Mod.logger().catching(e);
					}
				}
			} else {
				level.add(new Player(game));
			}
		}

		@Override
		public String getRegistryName() {
			return "Player";
		}
	}
}