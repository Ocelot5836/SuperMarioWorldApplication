package com.ocelot.mod.game.main.entity.item;

import java.awt.image.BufferedImage;
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
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.enemy.Koopa;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.fx.PlayerBounceFX;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Colorizer;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(ItemKoopaShell.Summonable.class)
public class ItemKoopaShell extends EntityItem implements IItemCarriable, IDamager, IDamagable {

	public static final BufferedImage SHELL_SHEET = Lib.loadImage(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/item/shell.png"));

	private static int[][] colors = { { 0xff007800, 0xff00B800, 0xff00F800 }, { 0xff880000, 0xffB80000, 0xffF80000 }, { 0xff4040D8, 0xff6868D8, 0xff8888F8 }, { 0xffF87800, 0xffF8C000, 0xffF8F800 } };
	private static int[] delays = { -1, 30 };
	private static List<List<BufferedImage[]>> sprites;

	private int currentAction;
	private BufferedAnimation animation;
	private Sprite sprite;

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

		this.animation = new BufferedAnimation();
		this.sprite = new Sprite();
		if (this.sprites == null) {
			this.sprites = new ArrayList<List<BufferedImage[]>>();
			loadSprites();
		}
		setAnimation(IDLE);
	}

	private void loadSprites() {
		for (int i = 0; i < 4; i++) {
			List<BufferedImage[]> images = new ArrayList<BufferedImage[]>();

			BufferedImage[] spinning = new BufferedImage[4];
			spinning[0] = this.addColor(SHELL_SHEET.getSubimage(0, 0, 16, 16), i);
			spinning[1] = this.addColor(SHELL_SHEET.getSubimage(16, 0, 16, 16), i);
			spinning[2] = this.addColor(SHELL_SHEET.getSubimage(32, 0, 16, 16), i);
			spinning[3] = Lib.flipHorizontal(spinning[1]);

			images.add(Lib.asArray(spinning[0]));
			images.add(spinning);

			sprites.add(images);
		}
	}

	private BufferedImage addColor(BufferedImage image, int koopaType) {
		return this.addColor(image, colors[koopaType][0], colors[koopaType][1], colors[koopaType][2]);
	}

	private BufferedImage addColor(BufferedImage image, int color1, int color2, int color3) {
		return Colorizer.replacePixels(Colorizer.replacePixels(Colorizer.replacePixels(image, 0xff464646, color1), 0xff6C6C6C, color2), 0xff919191, color3);
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
		sprite.setData(animation.getImage());

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() - cheight / 2);
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