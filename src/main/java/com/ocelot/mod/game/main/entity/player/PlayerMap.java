package com.ocelot.mod.game.main.entity.player;

import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import com.google.common.collect.Lists;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.entity.MobMover;
import com.ocelot.mod.game.core.gfx.Animation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.gamestate.worldmap.WorldMap;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("unused")
public class PlayerMap extends Mob {

	private WorldMap map;
	private int currentAction = -1;
	private List<Sprite[]> sprites = Lists.<Sprite[]>newArrayList();
	private Animation<Sprite> animation;
	private int[] numFrames = { 4, 2, 4, 1 };
	private int[] delays = { 200, 200, 200, -1 };

	private MobMover mover;
	private StopWatch timer;

	private static final int WALKING_DOWN = 0;
	private static final int WALKING_SIDE = 1;
	private static final int WALKING_UP = 2;
	private static final int ENTER_LEVEL = 3;

	public PlayerMap(GameTemplate game, WorldMap map) {
		this(game, map, 0, 0);
	}

	public PlayerMap(GameTemplate game, WorldMap map, double x, double y) {
		super(game);
		this.map = map;
		this.setPosition(x, y);
		this.setLastPosition(x, y);

		this.moveSpeed = 0.3;
		this.maxSpeed = 1.6;
		this.stopSpeed = 0.4;

		this.animation = new Animation<Sprite>();
		this.loadSprites();

		this.mover = new MobMover(this);
		this.timer = StopWatch.createStarted();
	}

	private void loadSprites() {
		this.sprites.addAll(Lib.loadSpritesFromSprite(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/entity/player/mario_map.png"), 16, 24, 160, 160, numFrames));
		this.animation.setFrames(this.sprites.get(0));
	}

	@Override
	public void update() {
		super.update();

		this.mover.update(timer.getTime());

		if (up) {
			y--;
			if (currentAction != WALKING_UP) {
				currentAction = WALKING_UP;
				this.setAnimation(currentAction);
			}
		} else if (down) {
			y++;
			if (currentAction != WALKING_DOWN) {
				currentAction = WALKING_DOWN;
				this.setAnimation(currentAction);
			}
		} else if (left || right) {
			if (left) {
				x--;
			} else {
				x++;
			}
			if (currentAction != WALKING_SIDE) {
				currentAction = WALKING_SIDE;
				this.setAnimation(currentAction);
			}
		} else {
			if (currentAction != WALKING_DOWN) {
				currentAction = WALKING_DOWN;
				this.setAnimation(currentAction);
			}
		}

		if (right) {
			facingRight = true;
		} else {
			facingRight = false;
		}

		animation.update();
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mc, mouseX, mouseY, partialTicks);

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		this.animation.get().render(posX - 8, posY - 12, !facingRight && currentAction == WALKING_SIDE ? 0x01 : 0x00);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		if (!this.mover.isMoving()) {
			// WorldMapIcon currentIcon = map.getIcon(x, y);
			// if (currentIcon != null) {
			// WorldMapIcon currentParent = currentIcon.getParent();
			// if (keyCode == Keyboard.KEY_LEFT) {
			// WorldMapIcon icon = currentIcon.getNearestIcon(x, y);
			// if (icon != null) {
			// if (currentParent != null && currentParent.getX() - icon.getX() >= currentIcon.getX() && currentParent.getY() - icon.getY() >= currentIcon.getY())
			// icon = currentParent;
			// } else {
			// icon = currentParent;
			// }
			//
			// if (icon.getX() < x) {
			// if (icon.getY() > y) {
			// mover.addPos((int) (icon.getX() - x), (int) (icon.getY() - y));
			// } else {
			// mover.addPos((int) (icon.getX() - x), (int) icon.getY());
			// }
			// }
			// }
			// if (keyCode == Keyboard.KEY_RIGHT) {
			// WorldMapIcon icon = currentIcon.getNearestIcon(x, y);
			// if (icon != null) {
			// if (currentParent != null && currentParent.getX() - icon.getX() >= currentIcon.getX() && currentParent.getY() - icon.getY() >= currentIcon.getY())
			// icon = currentParent;
			// } else {
			// icon = currentParent;
			// }
			//
			// if (icon.getX() > x) {
			// if (icon.getY() > y) {
			// mover.addPos((int) (icon.getX() - x), (int) (icon.getY() - y));
			// } else {
			// mover.addPos((int) (icon.getX() - x), (int) icon.getY());
			// }
			// }
			// }
			// }
			// if (keyCode == Keyboard.KEY_UP) {
			//
			// }
			// if (keyCode == Keyboard.KEY_DOWN) {
			//
			// }
		}
	}

	private void setAnimation(int animation) {
		if (animation < 0 || animation >= this.sprites.size()) {
			this.animation.setFrames(this.sprites.get(0));
			this.animation.setDelay(this.delays[0]);
		}
		this.animation.setFrames(this.sprites.get(animation));
		this.animation.setDelay(this.delays[animation]);
	}
}