package com.ocelot.mod.game.main.entity.player;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Stopwatch;
import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IDamager;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.IItemCarriable.ThrowingType;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gameState.GameState;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.enemy.Enemy.MarioDamageSource;
import com.ocelot.mod.game.main.entity.fx.particle.DustFX;
import com.ocelot.mod.game.main.entity.powerup.Powerup;
import com.ocelot.mod.game.main.gui.Guis;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(Player.Summonable.class)
public class Player extends Mob implements IDamagable {

	public static final BufferedImage SMALL_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/player/player_small.png"));
	public static final BufferedImage BIG_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/player/player_big.png"));

	private EntityItem item = null;

	private double runSpeed;
	private double runMaxSpeed;
	private double runStopSpeed;
	private double runGainSpeedAmount;

	private int runAnimationSpeed;

	private PlayerProperties properties;

	private Stopwatch deathAnimationTimer = Stopwatch.createUnstarted();

	private int currentAction;
	private Sprite sprite;
	private BufferedAnimation animation;

	private static List<BufferedImage[]> sprites;
	private static int[] delays = { -1, 100, -1, 100, 50, -1, -1, -1, -1, -1, -1, -1, 100, -1, 40, -1, 100, 100, -1, -1, -1, -1, -1, 80, -1, 80, 20, -1, -1, -1, -1, -1, -1, -1, 20, -1, 20, -1, 80, 80, -1, -1, -1 };

	private static final int IDLE_SMALL = 0;// -1
	private static final int WALKING_SMALL = 1;// 100
	private static final int IDLE_ITEM_SMALL = 2;// -1
	private static final int WALKING_ITEM_SMALL = 3;// 100
	private static final int KICKING_SMALL = 4;// 50
	private static final int SLIDING_SMALL = 5; // -1
	private static final int JUMPING_SMALL = 6;// -1
	private static final int FALLING_SMALL = 7;// -1
	private static final int STOPPING_SMALL = 8; // -1
	private static final int DUCKING_SMALL = 9;// -1
	private static final int DUCKING_ITEM_SMALL = 10;// -1
	private static final int FLOATING_FAT_SMALL = 11;// -1
	private static final int SPINNING_SMALL = 12;// 100
	private static final int COMPLETE_LEVEL_SMALL = 13;// -1
	private static final int RUNNING_SMALL = 14;// -1
	private static final int RUNNING_JUMPING_SMALL = 15;// -1
	private static final int SWIMMING_STROKE_SMALL = 16;// 100
	private static final int SWIMMING_SMALL = 17;// 100
	private static final int LOOK_UP_SMALL = 18;// -1
	private static final int LOOK_UP_ITEM_SMALL = 19;// -1
	private static final int MIDAIR_ITEM_SMALL = 20;// -1
	private static final int DEAD = 21;// -1

	private static final int IDLE_BIG = 22;// -1
	private static final int WALKING_BIG = 23;// 100
	private static final int IDLE_ITEM_BIG = 24;// -1
	private static final int WALKING_ITEM_BIG = 25;// 100
	private static final int KICKING_BIG = 26;// 50
	private static final int SLIDING_BIG = 27; // -1
	private static final int JUMPING_BIG = 28;// -1
	private static final int FALLING_BIG = 29;// -1
	private static final int STOPPING_BIG = 30; // -1
	private static final int DUCKING_BIG = 31;// -1
	private static final int DUCKING_ITEM_BIG = 32;// -1
	private static final int FLOATING_FAT_BIG = 33;// -1
	private static final int SPINNING_BIG = 34;// 100
	private static final int COMPLETE_LEVEL_BIG = 35;// -1
	private static final int RUNNING_BIG = 36;// -1
	private static final int RUNNING_JUMPING_BIG = 37;// -1
	private static final int SWIMMING_STROKE_BIG = 38;// 100
	private static final int SWIMMING_BIG = 39;// 100
	private static final int LOOK_UP_BIG = 40;// -1
	private static final int LOOK_UP_ITEM_BIG = 41;// -1
	private static final int MIDAIR_ITEM_BIG = 42;// -1

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
		if (sprites == null) {
			sprites = new ArrayList<BufferedImage[]>();
			this.loadSprites();
		}

		this.runSpeed = 0.02;
		this.runMaxSpeed = 2.6;
		this.runStopSpeed = 0.15;
		this.runGainSpeedAmount = 0.05;
		this.moveSpeed = 0.3;
		this.maxSpeed = 1.5;
		this.stopSpeed = 0.4;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.8;
		this.stopJumpSpeed = 0.3;

		this.runAnimationSpeed = 100;

		this.facingRight = true;
	}

	private void loadSprites() {
		BufferedImage[] walkingSmall = Lib.asArray(SMALL_SHEET.getSubimage(1, 1, 16, 24), SMALL_SHEET.getSubimage(18, 1, 16, 24));
		BufferedImage[] walkingItemSmall = Lib.asArray(SMALL_SHEET.getSubimage(35, 1, 16, 24), SMALL_SHEET.getSubimage(52, 1, 16, 24));
		BufferedImage[] spinningSmall = Lib.asArray(walkingSmall[0], SMALL_SHEET.getSubimage(205, 1, 16, 24), Lib.flipHorizontal(walkingSmall[0]), SMALL_SHEET.getSubimage(222, 1, 16, 24));
		BufferedImage[] runningSmall = Lib.asArray(SMALL_SHEET.getSubimage(18, 26, 16, 24), SMALL_SHEET.getSubimage(35, 26, 16, 24));
		BufferedImage[] swimmingStrokeSmall = Lib.asArray(SMALL_SHEET.getSubimage(103, 26, 16, 24), SMALL_SHEET.getSubimage(120, 26, 16, 24), SMALL_SHEET.getSubimage(137, 26, 16, 24));
		BufferedImage[] swimmingSmall = Lib.asArray(SMALL_SHEET.getSubimage(154, 26, 16, 24), SMALL_SHEET.getSubimage(171, 26, 16, 24), SMALL_SHEET.getSubimage(188, 26, 16, 24));

		sprites.add(Lib.asArray(walkingSmall[0]));
		sprites.add(walkingSmall);
		sprites.add(Lib.asArray(walkingItemSmall[0]));
		sprites.add(walkingItemSmall);
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(69, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(86, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(103, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(120, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(137, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(154, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(171, 1, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(188, 1, 16, 24)));
		sprites.add(spinningSmall);
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(1, 26, 16, 24)));
		sprites.add(runningSmall);
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(52, 26, 16, 24)));
		sprites.add(swimmingStrokeSmall);
		sprites.add(swimmingSmall);
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(69, 26, 16, 24)));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(1, 76, 16, 24)));
		sprites.add(Lib.asArray(walkingItemSmall[1]));
		sprites.add(Lib.asArray(SMALL_SHEET.getSubimage(86, 26, 16, 24)));

		BufferedImage[] walkingBig = Lib.asArray(BIG_SHEET.getSubimage(1, 1, 16, 32), BIG_SHEET.getSubimage(18, 1, 16, 32), BIG_SHEET.getSubimage(35, 1, 16, 32), BIG_SHEET.getSubimage(18, 1, 16, 32));
		BufferedImage[] walkingItemBig = Lib.asArray(BIG_SHEET.getSubimage(52, 1, 16, 32), BIG_SHEET.getSubimage(69, 1, 16, 32), BIG_SHEET.getSubimage(86, 1, 16, 32), BIG_SHEET.getSubimage(69, 1, 16, 32));
		BufferedImage[] spinningBig = Lib.asArray(Lib.flipHorizontal(walkingBig[0]), BIG_SHEET.getSubimage(222, 1, 16, 32), walkingBig[0], BIG_SHEET.getSubimage(1, 34, 16, 32));
		BufferedImage[] runningBig = Lib.asArray(BIG_SHEET.getSubimage(1, 67, 24, 32), BIG_SHEET.getSubimage(26, 67, 24, 32), BIG_SHEET.getSubimage(51, 67, 24, 32), BIG_SHEET.getSubimage(26, 67, 24, 32));
		BufferedImage[] swimmingStrokeBig = Lib.asArray(BIG_SHEET.getSubimage(101, 67, 24, 32), BIG_SHEET.getSubimage(126, 67, 24, 32), BIG_SHEET.getSubimage(151, 67, 24, 32), BIG_SHEET.getSubimage(126, 67, 24, 32));
		BufferedImage[] swimmingBig = Lib.asArray(BIG_SHEET.getSubimage(176, 67, 24, 32), BIG_SHEET.getSubimage(1, 100, 24, 32), BIG_SHEET.getSubimage(26, 100, 24, 32), BIG_SHEET.getSubimage(1, 100, 24, 32));

		sprites.add(Lib.asArray(walkingBig[0]));
		sprites.add(walkingBig);
		sprites.add(Lib.asArray(walkingItemBig[0]));
		sprites.add(walkingItemBig);
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(103, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(120, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(137, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(154, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(171, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(188, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(205, 1, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(205, 100, 32, 32)));
		sprites.add(spinningBig);
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(18, 34, 32, 32)));
		sprites.add(runningBig);
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(76, 67, 24, 32)));
		sprites.add(swimmingStrokeBig);
		sprites.add(swimmingBig);
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(35, 34, 16, 32)));
		sprites.add(Lib.asArray(BIG_SHEET.getSubimage(154, 34, 16, 32)));
		sprites.add(Lib.asArray(walkingItemBig[1]));
	}

	@Override
	protected void getNextPosition() {
		if (swimming) {
			double waterResistance = this.item == null ? 0.1 : 0.3;
			double waterFallResistance = this.item == null ? 0.08 : 0.025;

			runAnimationSpeed = 300;

			if (left) {
				dx -= moveSpeed * waterResistance;
				if (dx < -maxSpeed * waterResistance * 8) {
					dx += stopSpeed * waterResistance * 8;
				}
				setRunningAnimations();
			} else if (right) {
				dx += moveSpeed * waterResistance;
				if (dx > maxSpeed * waterResistance * 8) {
					dx -= stopSpeed * waterResistance * 8;
				}
				setRunningAnimations();
			} else {
				if (dx > 0) {
					dx -= stopSpeed * 0.25;
					if (dx < 0) {
						dx = 0;
					}
				} else if (dx < 0) {
					dx += stopSpeed * 0.25;
					if (dx > 0) {
						dx = 0;
					}
				}
			}

			if (dy < -1) {
				dy = -1;
			} else if (!enteredWaterFromAbove) {
				dy = 0;
			}

			if (this.item == null) {
				if (jumping && canSwim) {
					dy = -1;
					falling = true;
					canSwim = false;
				}

				if (!jumping) {
					canSwim = true;
				}
			} else {
				canSwim = false;
				if (up || jumping) {
					dy -= 0.25;
					if (dy < -2) {
						dy = -2;
					}
				} else if (down) {
					dy += 0.25;
					if (dy > 1) {
						dy = 1;
					}
				}
			}

			if (falling) {
				dy += fallSpeed * waterFallResistance;
				if (dy < 0)
					dy += stopJumpSpeed * waterFallResistance;

				if (dy > maxFallSpeed * 0.75) {
					dy = maxFallSpeed * 0.75;
				}
			}

			enteredWaterFromAbove = true;
		} else {
			if (enteredWaterFromAbove && up) {
				dy = -3;
				falling = true;
			}

			canSwim = false;
			enteredWaterFromAbove = false;

			if (left) {
				dx -= this.isRunning() ? (dx > 0 ? runStopSpeed : dx > -moveSpeed * 4 ? moveSpeed : runSpeed) : moveSpeed;
				if (dx < -(this.isRunning() ? runMaxSpeed : maxSpeed)) {
					dx += this.isRunning() ? runSpeed * 1.1 : moveSpeed * 1.1;
				}
				setRunningAnimations();
			} else if (right) {
				dx += this.isRunning() ? (dx < 0 ? runStopSpeed : dx < moveSpeed * 4 ? moveSpeed : runSpeed) : moveSpeed;
				if (dx > (this.isRunning() ? runMaxSpeed : maxSpeed)) {
					dx -= this.isRunning() ? runSpeed * 1.1 : moveSpeed * 1.1;
				}
				setRunningAnimations();
			} else {
				if (dx > 0) {
					dx -= 0.06;
					if (dx < 0) {
						dx = 0;
					}
					setRunningAnimations();
				} else if (dx < 0) {
					dx += 0.06;
					if (dx > 0) {
						dx = 0;
					}
					setRunningAnimations();
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
		}

		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}

	private void setRunningAnimations() {
		if (this.properties.isSmall()) {
			double percentage = Math.abs(dx / (this.isRunning() ? this.runMaxSpeed : this.maxSpeed));
			runAnimationSpeed = (int) (100.0 - 100.0 * percentage + (this.isRunning() ? 25.0 : 50.0));
		} else {
			double percentage = Math.abs(dx / (this.isRunning() ? this.runMaxSpeed : this.maxSpeed));
			runAnimationSpeed = (int) (100.0 - 100.0 * percentage + (this.isRunning() ? 20.0 : 45.0));
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

		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			tileMap.setTile((int) x / 16, (int) y / 16 + 1, Tile.GRASS);
		}

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
				if (this.properties.isSmall()) {
					currentAction = KICKING_SMALL;
				} else {
					currentAction = KICKING_BIG;
				}
				this.setAnimation(currentAction);
			}

			if (jumping && canSwim) {
				game.playSound(Sounds.PLAYER_SWIM, 1.0F);
			}

			calculateCorners(x, y + 1);
			if (this.properties.isSmall()) {
				if (swimming && (!(bottomLeft || bottomRight) || item != null)) {
					if (item == null) {
						if (jumping && canSwim) {
							currentAction = SWIMMING_STROKE_SMALL;
							this.setAnimation(SWIMMING_STROKE_SMALL);
						}
					}

					if (currentAction != SWIMMING_STROKE_SMALL || (currentAction == SWIMMING_STROKE_SMALL && animation.hasPlayedOnce())) {
						if (currentAction != SWIMMING_SMALL) {
							currentAction = SWIMMING_SMALL;
							this.setAnimation(currentAction);
						}
					}
				} else {
					if (currentAction != KICKING_SMALL || animation.hasPlayedOnce()) {
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
								if (this.isRunning() && (dx >= runMaxSpeed - 0.1) || (dx <= -runMaxSpeed + 0.1)) {
									if (currentAction != RUNNING_JUMPING_SMALL) {
										currentAction = RUNNING_JUMPING_SMALL;
										this.setAnimation(currentAction);
									}
								} else {
									if (currentAction != FALLING_SMALL) {
										currentAction = FALLING_SMALL;
										this.setAnimation(currentAction);
									}
								}
							}
						} else if (dy < 0) {
							if (item != null) {
								if (currentAction != MIDAIR_ITEM_SMALL) {
									currentAction = MIDAIR_ITEM_SMALL;
									this.setAnimation(currentAction);
								}
							} else {
								if (this.isRunning() && (dx >= runMaxSpeed - 0.1) || (dx <= -runMaxSpeed + 0.1)) {
									if (currentAction != RUNNING_JUMPING_SMALL) {
										currentAction = RUNNING_JUMPING_SMALL;
										this.setAnimation(currentAction);
									}
								} else {
									if (currentAction != JUMPING_SMALL) {
										currentAction = JUMPING_SMALL;
										this.setAnimation(currentAction);
									}
								}
							}
						} else if (dx != 0) {
							if (item != null) {
								if (currentAction != WALKING_ITEM_SMALL) {
									currentAction = WALKING_ITEM_SMALL;
									this.setAnimation(currentAction);
								}
								this.animation.setDelay(runAnimationSpeed);
							} else {
								if (!(left && dx < 0) && !(right && dx > 0) && ((left && dx > 0) || (right && dx < 0))) {
									if (currentAction != STOPPING_SMALL) {
										currentAction = STOPPING_SMALL;
										this.setAnimation(STOPPING_SMALL);
									}
									this.level.add(new DustFX(game, x, y + cheight / 2 - 4));
								} else {
									if (this.isRunning() && (dx >= runMaxSpeed - 0.1) || (dx <= -runMaxSpeed + 0.1)) {
										if (currentAction != RUNNING_SMALL) {
											currentAction = RUNNING_SMALL;
											this.setAnimation(currentAction);
										}
									} else {
										if (currentAction != WALKING_SMALL) {
											currentAction = WALKING_SMALL;
											this.setAnimation(currentAction);
										}
										this.animation.setDelay(runAnimationSpeed);
									}
								}
							}
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
			} else {
				if (swimming && (!(bottomLeft || bottomRight) || item != null)) {
					if (item == null) {
						if (jumping && canSwim) {
							currentAction = SWIMMING_STROKE_BIG;
							this.setAnimation(SWIMMING_STROKE_BIG);
						}
					}

					if (currentAction != SWIMMING_STROKE_BIG || (currentAction == SWIMMING_STROKE_BIG && animation.hasPlayedOnce())) {
						if (currentAction != SWIMMING_BIG) {
							currentAction = SWIMMING_BIG;
							this.setAnimation(currentAction);
						}
					}
				} else {
					if (currentAction != KICKING_BIG || animation.hasPlayedOnce()) {
						if (down) {
							if (item != null) {
								if (currentAction != DUCKING_ITEM_BIG) {
									currentAction = DUCKING_ITEM_BIG;
									this.setAnimation(currentAction);
								}
							} else {
								if (currentAction != DUCKING_BIG) {
									currentAction = DUCKING_BIG;
									this.setAnimation(currentAction);
								}
							}
						} else if (dy > 0) {
							if (item != null) {
								if (currentAction != MIDAIR_ITEM_BIG) {
									currentAction = MIDAIR_ITEM_BIG;
									this.setAnimation(currentAction);
								}
							} else {
								if (this.isRunning() && (dx >= runMaxSpeed - 0.1) || (dx <= -runMaxSpeed + 0.1)) {
									if (currentAction != RUNNING_JUMPING_BIG) {
										currentAction = RUNNING_JUMPING_BIG;
										this.setAnimation(currentAction);
									}
								} else {
									if (currentAction != FALLING_BIG) {
										currentAction = FALLING_BIG;
										this.setAnimation(currentAction);
									}
								}
							}
						} else if (dy < 0) {
							if (item != null) {
								if (currentAction != MIDAIR_ITEM_BIG) {
									currentAction = MIDAIR_ITEM_BIG;
									this.setAnimation(currentAction);
								}
							} else {
								if (this.isRunning() && (dx >= runMaxSpeed - 0.1) || (dx <= -runMaxSpeed + 0.1)) {
									if (currentAction != RUNNING_JUMPING_BIG) {
										currentAction = RUNNING_JUMPING_BIG;
										this.setAnimation(currentAction);
									}
								} else {
									if (currentAction != JUMPING_BIG) {
										currentAction = JUMPING_BIG;
										this.setAnimation(currentAction);
									}
								}
							}
						} else if (dx != 0) {
							if (item != null) {
								if (currentAction != WALKING_ITEM_BIG) {
									currentAction = WALKING_ITEM_BIG;
									this.setAnimation(currentAction);
								}
								this.animation.setDelay(runAnimationSpeed);
							} else {
								if (!(left && dx < 0) && !(right && dx > 0) && ((left && dx > 0) || (right && dx < 0))) {
									if (currentAction != STOPPING_BIG) {
										currentAction = STOPPING_BIG;
										this.setAnimation(STOPPING_BIG);
									}
									this.level.add(new DustFX(game, x, y + cheight / 2 - 4));
								} else {
									if (this.isRunning() && (dx >= runMaxSpeed - 0.1) || (dx <= -runMaxSpeed + 0.1)) {
										if (currentAction != RUNNING_BIG) {
											currentAction = RUNNING_BIG;
											this.setAnimation(currentAction);
										}
									} else {
										if (currentAction != WALKING_BIG) {
											currentAction = WALKING_BIG;
											this.setAnimation(currentAction);
										}
										this.animation.setDelay(runAnimationSpeed);
									}
								}
							}
						} else {
							if (up) {
								if (item != null) {
									if (currentAction != LOOK_UP_ITEM_BIG) {
										currentAction = LOOK_UP_ITEM_BIG;
										this.setAnimation(currentAction);
									}
								} else {
									if (currentAction != LOOK_UP_BIG) {
										currentAction = LOOK_UP_BIG;
										this.setAnimation(currentAction);
									}
								}
							} else {
								if (item != null) {
									if (currentAction != IDLE_ITEM_BIG) {
										currentAction = IDLE_ITEM_BIG;
										this.setAnimation(currentAction);
									}
								} else {
									if (currentAction != IDLE_BIG) {
										currentAction = IDLE_BIG;
										this.setAnimation(currentAction);
									}
								}
							}
						}
					}
				}

				if (this.down) {
					if (this.cheight != 15) {
						this.y += 6;
						this.lastY = y;
					}
					this.setSize(12, 15);
				} else {
					if (this.cheight != 26) {
						this.y -= 6;
						this.lastY = y;
					}
					this.setSize(12, 26);
				}
			}

			if (right)
				facingRight = true;
			if (left)
				facingRight = false;

			if (!swimming) {
				if (jumping && !falling) {
					game.playSound(Sounds.PLAYER_JUMP, 1.0F);
				}
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

			if (e instanceof IDamager) {
				IDamager damagable = (IDamager) e;
				if (!damagable.dealDamage(this, direction)) {
					flag = true;
				}
			} else {
				flag = true;
			}

			if (flag) {
				if (e instanceof IDamagable) {
					IDamagable damagable = (IDamagable) e;
					if (e instanceof IItemCarriable) {
						IItemCarriable carriable = (IItemCarriable) e;
						if (!(properties.isHolding() && item == e)) {
							damagable.takeDamage(this, MarioDamageSource.MARIO, direction, false);
						}
					} else {
						damagable.takeDamage(this, MarioDamageSource.MARIO, direction, false);
					}
				}
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);

		sprite.setData(animation.getImage());
		if (this.properties.isSmall() ? !facingRight : facingRight) {
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
			this.game.playSound(Sounds.PLAYER_POWERUP_LOSE, 1.0F);
			if (this.properties.getReserve() != Powerup.NULL) {
				this.level.add(this.properties.getReserve().createInstance(this.game, x, 16));
				this.properties.setReserve(Powerup.NULL);
			}
		} else {
			this.properties.setDead();
		}
	}

	public void addScore(int amount) {
		this.properties.increaseScore(amount);
		String count = Integer.toString(amount);
		level.add(new TextFX(game, x + cwidth - Minecraft.getMinecraft().fontRenderer.getStringWidth(count) / 2, y + cheight / 2, 0, -0.4, count, 0xffffff, 1));
	}

	public void addCoins(int amount) {
		this.properties.setCoins(this.properties.getCoins() + amount);
		game.playSound(Sounds.COLLECT_COIN, 1.0F);
	}

	public void collectPowerup(Powerup powerup) {
		Powerup reserve = this.properties.getReserve();
		if (powerup == Powerup.MUSHROOM) {
			if (this.properties.isSmall()) {
				powerup.apply(this);
				game.playSound(Sounds.PLAYER_POWERUP_GAIN, 1.0F);
				return;
			} else {
				if (reserve == Powerup.NULL) {
					this.properties.setReserve(powerup);
				}
				game.playSound(Sounds.PLAYER_COLLECT_POWERUP, 1.0F);
			}
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

	@Override
	public boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill) {
		if (source.canDamagePlayer()) {
			this.damage();
			if (isInstantKill) {
				this.properties.setDead();
			}
			return true;
		}
		return false;
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