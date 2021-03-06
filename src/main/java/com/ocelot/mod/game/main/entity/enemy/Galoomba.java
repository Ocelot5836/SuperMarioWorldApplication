package com.ocelot.mod.game.main.entity.enemy;

import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.EnumDirection;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.MarioDamageSource;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.EntityItem;
import com.ocelot.mod.game.core.entity.IDamagable;
import com.ocelot.mod.game.core.entity.IDamager;
import com.ocelot.mod.game.core.entity.IItemCarriable;
import com.ocelot.mod.game.core.entity.SummonException;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.IFileSummonable;
import com.ocelot.mod.game.core.gfx.Animation;
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

	public static final ResourceLocation GALOOMBA_SHEET = new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/enemy/galoomba.png");

	private int currentAction;
	private Animation<Sprite> animation;

	private static int[] delays = new int[] { 200, 200, 200, -1 };
	private static List<Sprite[]> sprites;

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
		this.animation = new Animation<Sprite>();

		if (sprites == null) {
			sprites = new ArrayList<Sprite[]>();
			loadSprites();
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

	private static void loadSprites() {
		Sprite[] walking = Lib.asArray(new Sprite(GALOOMBA_SHEET, 0, 0, 16, 16, 32, 16), new Sprite(GALOOMBA_SHEET, 16, 0, 16, 16, 32, 16));
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

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		animation.get().render(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - animation.get().getHeight(), facingRight ? 0x01 : 0x00);
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

	public static class Item extends EntityItem implements IItemCarriable, IDamager {

		public static final int GALOOMBA_TIME = 500;

		private Galoomba galoomba;
		private int timer;

		private boolean facingRight;
		private Animation<Sprite> animation;

		private Item(GameTemplate game, Galoomba galoomba) {
			this(game, galoomba, galoomba.getX(), galoomba.getY());
		}

		private Item(GameTemplate game, Galoomba galoomba, double x, double y) {
			super(game, 0, 0, 0.015);
			this.setSize(16, 16);
			this.setPosition(x, y);
			this.setLastPosition(x, y);
			this.galoomba = galoomba;

			this.animation = new Animation<Sprite>();
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

					if (((IDamagable) entity).takeDamage(this, MarioDamageSource.GALOOMBA_ITEM, this.getMovingDirection(), true)) {
						level.add(new PlayerBounceFX(game, x, y));
					}
				}
			}
		}

		@Override
		public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			double posX = lastX + this.getPartialRenderX();
			double posY = lastY + this.getPartialRenderY();

			GlStateManager.cullFace(CullFace.FRONT);
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX - this.getTileMapX() - cwidth / 2, posY - this.getTileMapY() + cheight / 2 - animation.get().getHeight(), 0);
			GlStateManager.rotate(180, 1, 0, 0);
			animation.get().render(0, -cheight, facingRight ? 0x01 : 0x00);
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
		public boolean dealDamage(Entity entity, EnumDirection sideHit) {
			if (entity instanceof IDamagable) {
				((IDamagable) entity).takeDamage(this, MarioDamageSource.GALOOMBA_ITEM, sideHit, true);
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
						throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".galoomba.summon.numerical"));
					}
				} else {
					throwSummonException(I18n.format("exception." + SuperMarioWorld.MOD_ID + ".galoomba.summon.numerical"));
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