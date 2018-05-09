package com.ocelot.mod.game.main.entity.item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;
import com.ocelot.mod.Sounds;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.IPlayerDamagable;
import com.ocelot.mod.game.core.entity.IPlayerDamager;
import com.ocelot.mod.game.core.entity.fx.TextFX;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.tile.Tile;
import com.ocelot.mod.game.main.entity.enemy.Enemy;
import com.ocelot.mod.game.main.entity.enemy.Enemy.MarioDamageSource;
import com.ocelot.mod.game.main.entity.enemy.Koopa;
import com.ocelot.mod.game.main.entity.enemy.Koopa.KoopaType;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.game.main.entity.player.PlayerProperties;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public class ItemKoopaShell extends EntityItem implements IItemCarriable, IPlayerDamager, IPlayerDamagable {

	private static int[] delays = { -1, 250, 30 };
	private static List<List<BufferedImage[]>> sprites;

	private int currentAction;
	private BufferedAnimation animation;
	private Sprite sprite;

	public static final int IDLE_KOOPA_MARIO = 0;
	public static final int IDLE_MARIO = 1;
	public static final int SPINNING_MARIO = 2;
	public static final int IDLE_KOOPA = 3;
	public static final int IDLE = 4;
	public static final int SPINNING = 5;

	private Player throwingPlayer;
	private int numEnemiesHit;
	private KoopaType type;

	public ItemKoopaShell(GameTemplate game, KoopaType type, Entity entity) {
		this(game, type, entity.getX(), entity.getY());
	}

	public ItemKoopaShell(GameTemplate game, Koopa koopa) {
		this(game, koopa.getType(), koopa.getX(), koopa.getY());
	}

	public ItemKoopaShell(GameTemplate game, KoopaType type) {
		this(game, type, 0, 0);
	}

	public ItemKoopaShell(GameTemplate game, KoopaType type, double x, double y) {
		super(game, 2.5, 0, 0.015);
		this.setSize(16, 16);
		this.setPosition(x, y);
		this.lastX = x;
		this.lastY = y;
		if (type == KoopaType.KAMIKAZE) {
			Game.stop(new IllegalArgumentException(I18n.format("exception." + Mod.MOD_ID + ".item_koopa_shell.illegal_type")), "Error while initializing koopa shell.");
		}

		this.type = type;

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
			int yOffset = i * 38;
			List<BufferedImage[]> images = new ArrayList<BufferedImage[]>();
			for (int j = 0; j < 2; j++) {
				yOffset += j * 18;

				BufferedImage[] koopaIdle = new BufferedImage[2];
				BufferedImage[] spinning = new BufferedImage[4];

				koopaIdle[0] = Enemy.ENEMY_SHEET.getSubimage(429, 523 + yOffset, 16, 16);
				koopaIdle[1] = Enemy.ENEMY_SHEET.getSubimage(447, 523 + yOffset, 16, 16);

				spinning[0] = Enemy.ENEMY_SHEET.getSubimage(465, 523 + yOffset, 16, 16);
				spinning[1] = Enemy.ENEMY_SHEET.getSubimage(483, 523 + yOffset, 16, 16);
				spinning[2] = Enemy.ENEMY_SHEET.getSubimage(501, 523 + yOffset, 16, 16);
				spinning[3] = Lib.flipHorizontal(spinning[1]);

				images.add(koopaIdle);
				images.add(new BufferedImage[] { spinning[0] });
				images.add(spinning);
			}
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
					if (e instanceof ItemKoopaShell) {
						ItemKoopaShell shell = (ItemKoopaShell) e;
						if (shell.getXSpeed() != 0) {
							game.playSound(Sounds.PLAYER_STOMP, 1.0F);
							this.setDead();
						}
						flag = true;
						shell.setDead();
					}

					if (e instanceof Enemy) {
						Enemy enemy = (Enemy) e;
						if (enemy.canDamage(MarioDamageSource.SHELL)) {
							flag = true;
							e.setDead();
						}
					}

					if (flag) {
						if (throwingPlayer != null) {
							PlayerProperties properties = throwingPlayer.getProperties();
							numEnemiesHit++;
							if (numEnemiesHit >= 9) {
								properties.increaseLives();
								game.playSound(Sounds.COLLECT_ONE_UP, 1.0F);
								level.add(new TextFX(game, x + cwidth / 2, y + cheight / 2, 0, -0.4, "1-UP", 0x00ff00, 1));
							} else {
								game.playSound(Sounds.PLAYER_STOMP, 1.0F + (Math.min((float) numEnemiesHit, 8.0F) / 8.0F));
							}
						} else {
							game.playSound(Sounds.PLAYER_STOMP, 1.0F);
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
		EntityItem item = (EntityItem) this;
		if (player.isFacingRight()) {
			item.setPosition(item.getX() + 5, player.getY() - 1);
		} else {
			item.setPosition(item.getX() - 5, player.getY() - 1);
		}
		item.resetDirections();

		if (type == ThrowingType.SIDE) {
			if (!player.isFacingRight()) {
				item.setDirection(-item.getXSpeed(), item.getYSpeed());
			}
		} else if (type == ThrowingType.UP) {
			item.setDirection(0, 0);
			item.boost(0, 0.6f);
		} else {
			item.setDirection(0, 0);
		}
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
	public boolean damagePlayer(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible) {
		if (sideHit.isHorizontalAxis() && !isPlayerInvincible && this.xSpeed != 0) {
			player.damage();
			return true;
		}
		return false;
	}

	@Override
	public void damageEnemy(Player player, EnumDirection sideHit, boolean isPlayerSpinning, boolean isPlayerInvincible) {
		if (sideHit == EnumDirection.UP && isPlayerSpinning) {
			setDead();
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
				if (!isPlayerSpinning) {
					player.setFalling(false);
					player.setJumping(true);
					this.xSpeed = 0;
					this.defaultStompEnemy(player);
				}
			}
		}
	}
}