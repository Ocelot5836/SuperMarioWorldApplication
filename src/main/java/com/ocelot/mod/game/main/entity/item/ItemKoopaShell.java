package com.ocelot.mod.game.main.entity.item;

import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IDamager;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.enemy.Koopa;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.fx.PlayerBounceFX;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(ItemKoopaShell.Summonable.class)
public class ItemKoopaShell extends EntityItem implements IItemCarriable, IDamager, IDamagable {

	public static final ResourceLocation SHELL_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/item/shell.png");

	private static int[] delays = { -1, 30 };
	private static List<List<Sprite[]>> sprites;

	private int currentAction;
	private Animation<Sprite> animation;

	public static final int IDLE = 0;
	public static final int SPINNING = 1;

	private Player throwingPlayer;
	private int numEnemiesHit;
	private KoopaType type;

	public ItemKoopaShell(GameTemplate game, KoopaType type, Entity entity) {
		this(game, type, entity.getX(), entity.getY());
	}

	public ItemKoopaShell(GameTemplate game, Koopa koopa) {
		this(game, koopa.getType(), koopa.getX(), koopa.getY() + 6);
	}

	public ItemKoopaShell(GameTemplate game, KoopaType type) {
		this(game, type, 0, 0);
	}

	public ItemKoopaShell(GameTemplate game, KoopaType type, double x, double y) {
		super(game, 2.5, 0);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.setLastPosition(x, y);
		this.type = type;
		this.slideSpeed = 0.6f;
		this.airSlideSpeed = 0.6f;
		if (type == KoopaType.KAMIKAZE) {
			Game.stop(new IllegalArgumentException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item_koopa_shell.illegal_type")), "Error while initializing koopa shell.");
		}

		this.animation = new Animation<Sprite>();
		if (this.sprites == null) {
			this.sprites = new ArrayList<List<Sprite[]>>();
			loadSprites();
		}
		setAnimation(IDLE);
	}

	private static void loadSprites() {
		boolean mario = false;

		for (int i = 0; i < 4; i++) {
			List<Sprite[]> images = new ArrayList<Sprite[]>();

			int offsetX = mario ? 64 : 0;
			int offsetY = i * 16;

			Sprite[] spinning = new Sprite[4];
			spinning[0] = new Sprite(SHELL_SHEET, offsetX, offsetY, 16, 16, 128, 64);
			spinning[1] = new Sprite(SHELL_SHEET, 16 + offsetX, offsetY, 16, 16, 128, 64);
			spinning[2] = new Sprite(SHELL_SHEET, 32 + offsetX, offsetY, 16, 16, 128, 64);
			spinning[3] = new Sprite(SHELL_SHEET, 48 + offsetX, offsetY, 16, 16, 128, 64);

			images.add(Lib.asArray(spinning[0]));
			images.add(spinning);

			sprites.add(images);
		}
	}

	@Override
	protected void onXBounce() {
		if (this.isOnScreen()) {
			game.playSound(Sounds.KOOPA_SHELL_RICOCHET, 1.0F);
		}
	}

	@Override
	public void update() {
		super.update();

		if (xSpeed == 0) {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				this.setAnimation(IDLE);
			}
		} else {
			if (currentAction != SPINNING) {
				currentAction = SPINNING;
				this.setAnimation(SPINNING);
			}
		}

		this.animation.update();

		checkAttackEnemies(level.getEntities());
	}

	private void checkAttackEnemies(List<Entity> entities) {
		if (xSpeed != 0) {
			for (int i = 0; i < entities.size(); i++) {
				boolean flag = false;
				Entity e = entities.get(i);

				if (e != this && e.intersects(this)) {
					if (e instanceof Mob) {
						Mob mob = (Mob) e;
						if (mob.canDamage(this, MarioDamageSource.SHELL)) {
							flag = true;
						}
					}

					if (flag || e instanceof IDamagable) {
						if (e instanceof IDamagable) {
							if (((IDamagable) e).takeDamage(throwingPlayer != null ? throwingPlayer : this, MarioDamageSource.SHELL, xSpeed < 0 ? EnumDirection.LEFT : EnumDirection.RIGHT, false)) {
								level.add(new PlayerBounceFX(game, x, y));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.animation.get().render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2);
	}

	public Player getThrowingPlayer() {
		return throwingPlayer;
	}

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.get(type.getId()).size()) {
			this.animation.setFrames(this.sprites.get(type.getId()).get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(type.getId()).get(animation));
		this.animation.setDelay(this.delays[animation % this.delays.length]);
	}

	@Override
	public void onHeldUpdate(Player player) {
	}

	@Override
	public void onPickup(Player player) {
		this.throwingPlayer = null;
	}

	@Override
	public void onThrow(Player player, ThrowingType type) {
		this.throwingPlayer = player;
		setDefaultThrowing(player, type);
	}

	@Override
	public void onDrop(Player player) {
		this.throwingPlayer = null;
		setDefaultPlacing(player);
	}

	@Override
	public boolean canPickup(Player player) {
		return this.xSpeed == 0;
	}

	@Override
	public boolean canHold(Player player) {
		return true;
	}

	@Override
	public boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill) {
		if (source == MarioDamageSource.SHELL) {
			defaultKillEntity(this);
			return true;
		}

		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (sideHit == EnumDirection.UP && isInstantKill) {
				setDead();
				return true;
			}

			if (this.xSpeed == 0 && player.getHeldItem() != this) {
				this.throwingPlayer = player;
				game.playSound(Sounds.PLAYER_KICK, 1.0F);
				resetDirections();
				numEnemiesHit = 0;
				if (player.getX() > x) {
					xSpeed = -xSpeed;
				}
			} else {
				if (sideHit == EnumDirection.UP) {
					if (!isInstantKill) {
						player.setFalling(false);
						player.setJumping(true);
						this.xSpeed = 0;
						level.add(new PlayerBounceFX(game, player.getX(), player.getY()));
						game.playSound(Sounds.PLAYER_STOMP, 1.0F);
					}
				}
			}
		}
		return false;
	}
	
	public int getNumEnemiesHit() {
		return numEnemiesHit;
	}

	@Override
	public boolean dealDamage(Entity entity, EnumDirection sideHit) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (sideHit.isHorizontalAxis() && this.xSpeed != 0) {
				player.damage();
				return true;
			}
		} else if (entity instanceof IDamagable) {
			IDamagable damagable = (IDamagable) entity;
			damagable.takeDamage(this, MarioDamageSource.SHELL, sideHit, false);
			return true;
		}
		return false;
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 2) {
				try {
					KoopaType type = KoopaType.byId(Integer.parseInt(args[0]));
					level.add(new ItemKoopaShell(game, type, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item.summon.numerical", this.getRegistryName()));
				}
			} else if (args.length > 1) {
				try {
					KoopaType type = KoopaType.byId(Integer.parseInt(args[0]));
					level.add(new ItemKoopaShell(game, type));
				} catch (Exception e) {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item.summon.numerical", this.getRegistryName()));
				}
			} else {
				throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".item_koopa_shell.summon.invalid_shell_type"));
			}
		}

		@Override
		public String getRegistryName() {
			return "ItemKoopaShell";
		}
	}
}