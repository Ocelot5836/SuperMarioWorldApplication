package com.ocelot.mod.game.main.entity.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IDamager;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.ai.AIKoopa;
import com.ocelot.mod.game.main.entity.ai.BasicWalkListener;
import com.ocelot.mod.game.main.entity.item.ItemKoopaShell;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

@FileSummonableEntity(Koopa.Summonable.class)
public class Koopa extends Enemy implements IDamagable, IDamager, BasicWalkListener {

	public static final ResourceLocation KOOPA_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/enemy/koopa.png");

	public static final byte WING_BIT = 0x01;
	public static final byte CLMBING_BIT = 0x02;

	private int currentAction;
	private Animation<Sprite> animation;

	private static int[] delays = { 25, 250, 250, -1, -1, -1 };
	private static List<List<Sprite[]>> sprites;

	public static final int TURNING_SIDE = 0;
	public static final int WALKING_SIDE = 1;
	public static final int WALKING_WINGED_SIDE = 2;
	public static final int JUMPING = 3;
	public static final int FALLING = 4;
	public static final int IDLE = 5;

	private KoopaType type;
	private boolean hasWings;
	private boolean climbing;

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
		this.setLastPosition(x, y);
		this.type = type;
		this.hasWings = (modifier & 0x01) > 0;
		this.climbing = (modifier & 0x02) > 0;

		if (this.hasWings && this.climbing) {
			Game.stop(new IllegalArgumentException("A koopa can only have either the wing bit or the climbing bit. You cannot add them together!"), "Koopa attempted to have multiple attributes at once");
		}

		if (type == KoopaType.KAMIKAZE && (this.hasWings || this.climbing)) {
			Game.stop(new IllegalArgumentException("A kamikaze koopa cannot have wings OR climb. These values are both prohibited!"), "Koopa attempted to spawn as kamikaze while having either flying or climbing attributes");
		}

		this.animation = new Animation<Sprite>();
		if (sprites == null) {
			sprites = new ArrayList<List<Sprite[]>>();
			loadSprites();
		}
		currentAction = IDLE;
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
		this.stopJumpSpeed = type == KoopaType.KAMIKAZE ? 1 : 0.3;

		delays = new int[] { 25, 250, 250, -1, -1, -1 };
	}

	private static void loadSprites() {
		for (int i = 0; i < 4; i++) {
			List<Sprite[]> sprites = new ArrayList<Sprite[]>();
			Sprite[] walkingNormal = new Sprite[2];
			Sprite[] walkingWinged = new Sprite[2];

			int xOffset = 56 * (i % 2);
			int yOffset = 56 * (i / 2);

			walkingNormal[0] = new Sprite(KOOPA_SHEET, 16 + xOffset, yOffset, 16, 28, 112, 288);
			walkingNormal[1] = new Sprite(KOOPA_SHEET, 32 + xOffset, yOffset, 16, 28, 112, 288);

			walkingWinged[0] = new Sprite(KOOPA_SHEET, 0, 28, 17, 28, 112, 288);
			walkingWinged[1] = new Sprite(KOOPA_SHEET, 17, 28, 22, 28, 112, 288);

			sprites.add(Lib.asArray(new Sprite(KOOPA_SHEET, 0, 0, 16, 28, 112, 288)));
			sprites.add(walkingNormal);
			sprites.add(walkingWinged);
			sprites.add(Lib.asArray(walkingNormal[0]));
			sprites.add(Lib.asArray(walkingNormal[0]));
			sprites.add(Lib.asArray(walkingNormal[0]));
			Koopa.sprites.add(sprites);
		}

		List<Sprite[]> sprites = new ArrayList<Sprite[]>();
		Sprite[] spinning = new Sprite[8];
		for (int i = 0; i < spinning.length; i++) {
			int xOffset = 0;

			spinning[i] = new Sprite(KOOPA_SHEET, 80 + xOffset, 112, 16 + i * 16, 16, 112, 288);
		}
		sprites.add(spinning);
		Koopa.sprites.add(sprites);
	}

	@Override
	public void initAI() {
		super.registerAI(new AIKoopa(this.type));
	}

	@Override
	public void update() {
		super.update();

		getNextPosition();
		getNextPosition();
		getNextPosition();

		if (type != KoopaType.KAMIKAZE) {
			boolean playAnimations = true;

			if (currentAction == TURNING_SIDE) {
				playAnimations = animation.hasPlayedOnce();
			}

			if (playAnimations) {
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
		this.animation.get().render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - this.animation.get().getHeight(), facingRight ? 0x00 : 0x01);
	}

	private void setAnimation(int animation) {
		if (type == KoopaType.KAMIKAZE) {
			this.animation.setFrames(sprites.get(type.getId()).get(0));
			this.animation.setDelay(0);
		} else {
			if (animation < 0 || animation >= sprites.get(type.getId()).size()) {
				this.animation.setFrames(sprites.get(type.getId()).get(0));
				this.animation.setDelay(delays[0]);
			}
			this.animation.setFrames(sprites.get(type.getId()).get(animation));
			this.animation.setDelay(delays[animation]);
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
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (sideHit != EnumDirection.UP) {
				player.damage();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill) {
		if (source == MarioDamageSource.SHELL || source.isPlayerProjectile()) {
			defaultKillEntity(this);
			return true;
		}

		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (type != KoopaType.KAMIKAZE) {
				if (sideHit == EnumDirection.UP && !this.invulnerable) {
					player.setPosition(player.getX(), y - cheight);
					if (isInstantKill) {
						defaultSpinStompEnemy(player);
						// TODO add spin koopa death animation
					} else {
						player.setJumping(true);
						player.setFalling(false);
						defaultStompEnemy(player);
						ItemKoopaShell shell = new ItemKoopaShell(game, this);
						shell.setDirection(0, shell.getYSpeed());
						level.add(shell);
						// TODO add the little koopa
					}
					setDead();
					return true;
				}
			} else {
				if (sideHit == EnumDirection.UP) {
					if (!isInstantKill) {
						player.setJumping(true);
						player.setFalling(false);
					}
					defaultSpinStompEnemy(player);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void basicWalkTrigger(boolean right) {
		currentAction = TURNING_SIDE;
		setAnimation(currentAction);
	}

	@Override
	public boolean canDamage(Entity entity, MarioDamageSource source) {
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
						throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.illegal_arg", args[0]));
					}
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.illegal_arg", args[0]));
				}
				if (type == null) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.illegal_arg", args[0] + "=null"));
				}

				if (args.length > 1) {
					if (args.length > 2) {
						try {
							level.add(new Koopa(game, type, Double.parseDouble(args[1]), Double.parseDouble(args[2])));
							return;
						} catch (NumberFormatException e) {
							throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.numerical"));
						} catch (Exception e) {
							throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.illegal_args", args[1] + ", " + args[2]));
						}
						if (args.length > 3) {
							try {
								level.add(new Koopa(game, type, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Integer.parseInt(args[3])));
								return;
							} catch (NumberFormatException e) {
								throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.numerical"));
							} catch (Exception e) {
								throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.illegal_args", args[1] + ", " + args[2] + ", " + args[3]));
							}
						}
					}
					try {
						level.add(new Koopa(game, type, Integer.parseInt(args[1])));
						return;
					} catch (NumberFormatException e) {
						throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.numerical_attrib"));
					} catch (Exception e) {
						throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.illegal_arg", args[1]));
					}
				} else {
					level.add(new Koopa(game, type));
					return;
				}
			} else {
				throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".koopa.summon.low_args"));
			}
		}

		@Override
		public String getRegistryName() {
			return "Koopa";
		}
	}
}