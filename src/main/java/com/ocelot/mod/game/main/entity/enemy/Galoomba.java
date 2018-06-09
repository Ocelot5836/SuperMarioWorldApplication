package com.ocelot.mod.game.main.entity.enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Sounds;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.ai.AIBasicWalk;
import com.ocelot.mod.game.main.entity.fx.PlayerBounceFX;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@FileSummonableEntity(Galoomba.Summonable.class)
public class Galoomba extends Enemy implements IDamagable {

	public static final BufferedImage GALOOMBA_SHEET = Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/enemy/galoomba.png"));

	private int currentAction;
	private Sprite sprite;
	private BufferedAnimation animation;

	private static int[] delays = new int[] { 200, 200, 200, -1 };
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
		this.setLastPosition(x, y);
		this.setSize(16, 16);
		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();

		if (sprites == null) {
			this.sprites = new ArrayList<BufferedImage[]>();
			this.loadSprites();
		}
		this.setAnimation(IDLE);

		this.moveSpeed = 0.15;
		this.maxSpeed = 0.5;
		this.stopSpeed = 0.25;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.0;
		this.stopJumpSpeed = 0.3;
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
		super.registerAI(new AIBasicWalk());
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

		animation.update();
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

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.size()) {
			this.animation.setFrames(this.sprites.get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(animation));
		this.animation.setDelay(this.delays[animation]);
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
		if (source == MarioDamageSource.SHELL) {
			defaultKillEntity(this);
			return true;
		}

		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (sideHit == EnumDirection.UP && !this.invulnerable) {
				player.setPosition(player.getX(), y - cheight);
				if (isInstantKill) {
					defaultSpinStompEnemy(player);
				} else {
					player.setJumping(true);
					player.setFalling(false);
					defaultStompEnemy(player);
					Galoomba.Item item = new Galoomba.Item(game, this);
					item.setDirection(0, item.getYSpeed());
					level.add(item);
				}
				setDead();
				return true;
			}
		}
		return false;
	}

	public static class Item extends EntityItem implements IItemCarriable, IDamagable {

		public static final int GALOOMBA_TIME = 500;

		private Galoomba galoomba;
		private int timer;

		private boolean facingRight;
		private Sprite sprite;
		private BufferedAnimation animation;

		private Item(GameTemplate game, Galoomba galoomba) {
			this(game, galoomba, galoomba.getX(), galoomba.getY());
		}

		private Item(GameTemplate game, Galoomba galoomba, double x, double y) {
			super(game, 0, 0, 0.015);
			this.setSize(16, 16);
			this.setPosition(x, y);
			this.setLastPosition(x, y);
			this.galoomba = galoomba;

			this.sprite = new Sprite();
			this.animation = new BufferedAnimation();
			this.animation.setDelay(Galoomba.delays[Galoomba.WALKING_SIDE]);
			this.animation.setFrames(Galoomba.sprites.get(Galoomba.WALKING_SIDE));
		}

		@Override
		public void update() {
			super.update();

			timer++;
			if (timer >= GALOOMBA_TIME) {
				this.setDead();
				this.galoomba.setDead(false);
				this.galoomba.setPosition(x, y);
				this.galoomba.setLastPosition(x, y);
				level.add(this.galoomba);
			}

			checkEnemyDamage();

			animation.setDelay((int) (Galoomba.delays[Galoomba.WALKING_SIDE] - (Galoomba.delays[Galoomba.WALKING_SIDE] / 3) * ((float) timer / (float) GALOOMBA_TIME)));
			animation.update();
		}

		private void checkEnemyDamage() {
			if (dx != 0) {
				List<Entity> entities = level.getEntities();
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = entities.get(i);
					if (!this.intersects(entity) || !(entity instanceof IDamagable)) {
						continue;
					}

					((IDamagable) entity).takeDamage(this, MarioDamageSource.GALOOMBA, this.getMovingDirection(), true);
				}
			}
		}

		@Override
		public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			sprite.setData(animation.getImage());
			if (facingRight) {
				sprite = Lib.flipHorizontal(sprite);
			}

			double posX = lastX + this.getPartialRenderX();
			double posY = lastY + this.getPartialRenderY();

			GlStateManager.cullFace(CullFace.FRONT);
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - sprite.getHeight(), 0);
			GlStateManager.rotate(180, 1, 0, 0);
			sprite.render(0, -cheight);
			GlStateManager.popMatrix();
			GlStateManager.cullFace(CullFace.BACK);
		}

		@Override
		public void onHeldUpdate(Player player) {
			facingRight = player.isFacingRight();
			animation.update();
			timer++;
			if (player.isSwimming()) {
				timer = 0;
			}
		}

		@Override
		public void onPickup(Player player) {
		}

		@Override
		public void onThrow(Player player, ThrowingType type) {
			this.timer = 0;
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
			return timer < GALOOMBA_TIME;
		}

		@Override
		public boolean takeDamage(Entity entity, MarioDamageSource source, EnumDirection sideHit, boolean isInstantKill) {
			if(source == MarioDamageSource.SHELL || isInstantKill) {
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
					game.playSound(Sounds.PLAYER_KICK, 1.0F);
					resetDirections();
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
	}

	public static class Summonable implements IFileSummonable {
		@Override
		public void summonFromFile(GameTemplate game, Level level, String[] args) throws SummonException {
			if (args.length > 0) {
				if (args.length > 1) {
					try {
						level.add(new Galoomba(game, Double.parseDouble(args[0]), Double.parseDouble(args[1])));
					} catch (Exception e) {
						throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".galoomba.summon.numerical"));
					}
				} else {
					throwSummonException(I18n.format("exception." + Mod.MOD_ID + ".galoomba.summon.numerical"));
				}
			} else {
				level.add(new Galoomba(game));
			}
		}

		@Override
		public String getRegistryName() {
			return "Galoomba";
		}
	}
}