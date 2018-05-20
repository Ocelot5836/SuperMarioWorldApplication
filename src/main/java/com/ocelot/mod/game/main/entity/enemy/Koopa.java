package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.IFileSummonable;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.entity.IPlayerDamager;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.item.ItemKoopaShell;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Colorizer;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

// TODO recode the movement AI	
public class Koopa extends Enemy implements IPlayerDamagable, IPlayerDamager {

	public static final BufferedImage KOOPA_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/enemy/koopa.png"));

	public static final byte WING_BIT = 0x01;
	public static final byte CLMBING_BIT = 0x02;

	private int currentAction;
	private Sprite sprite;
	private BufferedAnimation animation;

	private static int[][] colors = { { 0xff007800, 0xff00B800, 0xff00F800 }, { 0xff880000, 0xffB80000, 0xffF80000 }, { 0xff4040D8, 0xff6868D8, 0xff8888F8 }, { 0xffF87800, 0xffF8C000, 0xffF8F800 } };
	private static int[] delays = { 150, 250, 250, -1, -1, -1 };
	private static List<List<BufferedImage[]>> sprites;

	public static final int TURNING_SIDE = 0;
	public static final int WALKING_SIDE = 1;
	public static final int WALKING_WINGED_SIDE = 2;
	public static final int JUMPING = 3;
	public static final int FALLING = 4;
	public static final int IDLE = 5;

	private KoopaType type;
	private boolean hasWings;
	private boolean climbing;
	private boolean movingRight;

	public Koopa(GameTemplate game, KoopaType type) {
		this(game, type, 0, 0, 0);
	}

	public Koopa(GameTemplate game, KoopaType type, int modifier) {
		this(game, type, 0, 0, modifier);
	}

	public Koopa(GameTemplate game, KoopaType type, double x, double y) {
		this(game, type, x, y, 0);
	}

	public Koopa(GameTemplate game, KoopaType type, double x, double y, int modifier) {
		super(game);
		this.setSize(16, type == KoopaType.KAMIKAZE ? 16 : 28);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;
		this.type = type;
		this.hasWings = (modifier & 0x01) > 0;
		this.climbing = (modifier & 0x02) > 0;
		this.movingRight = true;

		if (this.hasWings && this.climbing) {
			Game.stop(new IllegalArgumentException("A koopa can only have either the wing bit or the climbing bit. You cannot add them together!"), "Koopa attempted to have multiple attributes at once");
		}

		if (type == KoopaType.KAMIKAZE && (this.hasWings || this.climbing)) {
			Game.stop(new IllegalArgumentException("A kamikaze koopa cannot have wings OR climb. These values are both prohibited!"), "Koopa attempted to spawn as kamikaze while having either flying or climbing attributes");
		}

		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		if (this.sprites == null) {
			this.sprites = new ArrayList<List<BufferedImage[]>>();
			this.loadSprites();
		}
		this.setAnimation(IDLE);

		if (type == KoopaType.KAMIKAZE) {
			this.moveSpeed = 0.25;
			this.maxSpeed = 3.5;
		} else {
			this.moveSpeed = 0.15;
			this.maxSpeed = 0.5;
		}
		this.stopSpeed = 0.25;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.0;
		this.stopJumpSpeed = 0.3;
	}

	private void loadSprites() {
		for (int i = 0; i < 4; i++) {
			List<BufferedImage[]> sprites = new ArrayList<BufferedImage[]>();
			BufferedImage[] walkingNormal = new BufferedImage[2];
			BufferedImage[] walkingWinged = new BufferedImage[2];

			walkingNormal[0] = this.addColor(KOOPA_SHEET.getSubimage(16, 0, 16, 28), i);
			walkingNormal[1] = this.addColor(KOOPA_SHEET.getSubimage(32, 0, 16, 28), i);

			walkingWinged[0] = this.addColor(KOOPA_SHEET.getSubimage(0, 28, 17, 28), i);
			walkingWinged[1] = this.addColor(KOOPA_SHEET.getSubimage(17, 28, 22, 28), i);

			sprites.add(Lib.asArray(this.addColor(KOOPA_SHEET.getSubimage(0, 0, 16, 28), i)));
			sprites.add(walkingNormal);
			sprites.add(walkingWinged);
			sprites.add(Lib.asArray(walkingNormal[0]));
			sprites.add(Lib.asArray(walkingNormal[0]));
			sprites.add(Lib.asArray(walkingNormal[0]));
			this.sprites.add(sprites);
		}

		// TODO finish using the colorizer to recolor the same texture for this bit here
		List<BufferedImage[]> sprites = new ArrayList<BufferedImage[]>();
		BufferedImage[] spinning = new BufferedImage[8];
		spinning[0] = this.addColor(KOOPA_SHEET.getSubimage(0, 56, 16, 16), 0xff007800, 0xff00B800, 0xff00F800);
		spinning[1] = this.addColor(KOOPA_SHEET.getSubimage(0, 56, 16, 16), 0xff880000, 0xffB80000, 0xffF80000);
		spinning[3] = this.addColor(KOOPA_SHEET.getSubimage(16, 56, 16, 16), 0xf87800, 0xfff8c000, 0xfff8f800);
		spinning[4] = this.addColor(KOOPA_SHEET.getSubimage(32, 56, 16, 16), 0xff005050, 0xff007878, 0xff00a0a0);
		spinning[5] = this.addColor(KOOPA_SHEET.getSubimage(32, 56, 16, 16), 0xff707070, 0xffa0a0a0, 0xffc0c0c0);
		spinning[6] = this.addColor(Lib.flipHorizontal(KOOPA_SHEET.getSubimage(16, 56, 16, 16)), 0xff885818, 0xffd8a038, 0xfff8d870);
		spinning[7] = this.addColor(Lib.flipHorizontal(KOOPA_SHEET.getSubimage(16, 56, 16, 16)), 0xff283048, 0xff485058, 0xff686858);
		sprites.add(spinning);
		this.sprites.add(sprites);
	}

	private BufferedImage addColor(BufferedImage image, int koopaType) {
		return this.addColor(image, colors[koopaType][0], colors[koopaType][1], colors[koopaType][2]);
	}

	private BufferedImage addColor(BufferedImage image, int color1, int color2, int color3) {
		return Colorizer.replacePixels(Colorizer.replacePixels(Colorizer.replacePixels(image, 0xff464646, color1), 0xff6C6C6C, color2), 0xff919191, color3);
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

		if (type != KoopaType.KAMIKAZE) {
			calculateCorners(xdest, y);
			if (topLeft || bottomLeft || topRight || bottomRight) {
				movingRight = !movingRight;
			}

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
		} else {
			Player nearestPlayer = level.getNearestPlayer(this);
			if (nearestPlayer != null) {
				double playerX = nearestPlayer.getX();
				double playerY = nearestPlayer.getY();

				if (playerX <= x) {
					movingRight = false;
				} else {
					movingRight = true;
				}
				stopSpeed = 1;
			}

			calculateCorners(x + (left ? -1 : 1), y);
			if (topLeft || bottomLeft || topRight || bottomRight) {
				movingRight = !movingRight;
			}
		}

		this.animation.update();

		if (movingRight) {
			right = true;
			left = false;
		} else {
			right = false;
			left = true;
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
		if (type == KoopaType.KAMIKAZE) {
			this.animation.setFrames(this.sprites.get(type.getId()).get(0));
			this.animation.setDelay(0);
		} else {
			if (animation < 0 || animation >= this.sprites.get(type.getId()).size()) {
				this.animation.setFrames(this.sprites.get(type.getId()).get(0));
				this.animation.setDelay(this.delays[0]);
			}
			this.animation.setFrames(this.sprites.get(type.getId()).get(animation));
			this.animation.setDelay(this.delays[animation]);
		}
	}

	public KoopaType getType() {
		return type;
	}

	public enum KoopaType implements IStringSerializable {
		GREEN(0, "green"), RED(1, "red"), BLUE(2, "blue"), YELLOW(3, "yellow"), KAMIKAZE(4, "kamikaze");

		private int id;
		private String name;

		private static final Map<String, KoopaType> NAME_LOOKUP = Maps.<String, KoopaType>newHashMap();

		private KoopaType(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		@Nullable
		public static KoopaType byName(String name) {
			return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
		}

		public static KoopaType byId(int id) {
			return KoopaType.values()[MathHelper.abs(id % KoopaType.values().length)];
		}

		static {
			for (KoopaType type : values()) {
				NAME_LOOKUP.put(type.getName().toLowerCase(Locale.ROOT), type);
			}
		}
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
		if (type != KoopaType.KAMIKAZE) {
			if (sideHit == EnumDirection.UP && !isPlayerInvincible) {
				player.setPosition(player.getX(), y - cheight);
				if (isPlayerSpinning) {
					defaultSpinStompEnemy(player);
					// TODO add spin koopa death animation
				} else {
					player.setJumping(true);
					player.setFalling(false);
					defaultStompEnemy(player);
					player.getProperties().increaseScore(Lib.getScoreFromJumps(player.getProperties().getEnemyJumpCount()));
					level.add(new TextFX(game, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0, -0.4, Lib.getScoreFromJumps(player.getProperties().getEnemyJumpCount()) + "", 0xffffff, 1));
					ItemKoopaShell shell = new ItemKoopaShell(game, this);
					shell.setDirection(0, shell.getYSpeed());
					level.add(shell);
					// TODO add the little koopa
				}
				setDead();
			}
		} else {
			if (sideHit == EnumDirection.UP) {
				if (!isPlayerSpinning) {
					player.setJumping(true);
					player.setFalling(false);
				}
				defaultSpinStompEnemy(player);
			}
		}
	}

	@Override
	public boolean canDamage(MarioDamageSource source) {
		return this.type != KoopaType.KAMIKAZE;
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 0) {
				KoopaType type = null;
				try {
					type = KoopaType.byId(Integer.parseInt(args[0]));
				} catch (NumberFormatException e) {
					try {
						type = KoopaType.byName(args[0]);
					} catch (Exception e1) {
						throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.illegal_arg", args[0]));
					}
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.illegal_arg", args[0]));
				}
				if (type == null) {
					throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.illegal_arg", args[0] + "=null"));
				}

				if (args.length > 1) {
					if (args.length > 2) {
						try {
							level.add(new Koopa(game, type, Double.parseDouble(args[1]), Double.parseDouble(args[2])));
							return;
						} catch (NumberFormatException e) {
							throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.numerical"));
						} catch (Exception e) {
							throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.illegal_args", args[1] + ", " + args[2]));
						}
						if (args.length > 3) {
							try {
								level.add(new Koopa(game, type, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Integer.parseInt(args[3])));
								return;
							} catch (NumberFormatException e) {
								throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.numerical"));
							} catch (Exception e) {
								throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.illegal_args", args[1] + ", " + args[2] + ", " + args[3]));
							}
						}
					}
					try {
						level.add(new Koopa(game, type, Integer.parseInt(args[1])));
						return;
					} catch (NumberFormatException e) {
						throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.numerical_attrib"));
					} catch (Exception e) {
						throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.illegal_arg", args[1]));
					}
				} else {
					level.add(new Koopa(game, type));
					return;
				}
			} else {
				throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".koopa.summon.low_args"));
			}
		}
	}
}