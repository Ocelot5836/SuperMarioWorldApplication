package com.ocelot.mod.game.main.entity.player;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.entity.Mob;
import com.ocelot.mod.game.core.entity.MobMover;
import com.ocelot.mod.game.core.gfx.BufferedAnimation;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.main.gamestate.worldmap.WorldMapIcon;
import com.ocelot.mod.game.main.gamestate.worldmap.WorldMapState;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class PlayerMap extends Mob {

	private WorldMapState map;
	private int currentAction = -1;
	private Sprite sprite;
	private List<BufferedImage[]> sprites = Lists.<BufferedImage[]>newArrayList();
	private BufferedAnimation animation;
	private int[] numFrames = { 4, 2, 4, 1 };
	private int[] delays = { 200, 200, 200, -1 };

	private MobMover mover;
	private StopWatch timer;

	private static final int WALKING_DOWN = 0;
	private static final int WALKING_SIDE = 1;
	private static final int WALKING_UP = 2;
	private static final int ENTER_LEVEL = 3;

	public PlayerMap(GameTemplate game, WorldMapState map) {
		this(game, map, 0, 0);
	}

	public PlayerMap(GameTemplate game, WorldMapState map, double x, double y) {
		super(game);
		this.map = map;
		this.setPosition(x, y);

		this.moveSpeed = 0.3;
		this.maxSpeed = 1.6;
		this.stopSpeed = 0.4;

		this.sprite = new Sprite();
		this.animation = new BufferedAnimation();
		this.loadSprites();

		this.mover = new MobMover(this);
		this.timer = StopWatch.createStarted();
	}

	private void loadSprites() {
		this.sprites.addAll(Lib.loadSpritesFromBufferedImage(Lib.loadImage(new ResourceLocation(Mod.MOD_ID, "textures/entity/mario_map.png")), 16, 24, numFrames));
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

		sprite.setData(animation.getImage());
		if (!facingRight && currentAction == WALKING_SIDE) {
			sprite = Lib.flipHorizontal(sprite);
		}

		double posX = lastX + this.getPartialRenderX();
		double posY = lastY + this.getPartialRenderY();
		sprite.render(posX - 8, posY - 12);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		if (!this.mover.isMoving()) {
			WorldMapIcon currentIcon = map.getIcon(x, y);
			if (currentIcon != null) {
				WorldMapIcon currentParent = currentIcon.getParent();
				if (keyCode == Keyboard.KEY_LEFT) {
					WorldMapIcon icon = currentIcon.getNearestIcon(x, y);
					if (icon != null) {
						if (currentParent != null && currentParent.getX() - icon.getX() >= currentIcon.getX() && currentParent.getY() - icon.getY() >= currentIcon.getY())
							icon = currentParent;
					} else {
						icon = currentParent;
					}

					if (icon.getX() < x) {
						if (icon.getY() > y) {
							mover.addPos((int) (icon.getX() - x), (int) (icon.getY() - y));
						} else {
							mover.addPos((int) (icon.getX() - x), (int) icon.getY());
						}
					}
				}
				if (keyCode == Keyboard.KEY_RIGHT) {
					WorldMapIcon icon = currentIcon.getNearestIcon(x, y);
					if (icon != null) {
						if (currentParent != null && currentParent.getX() - icon.getX() >= currentIcon.getX() && currentParent.getY() - icon.getY() >= currentIcon.getY())
							icon = currentParent;
					} else {
						icon = currentParent;
					}

					if (icon.getX() > x) {
						if (icon.getY() > y) {
							mover.addPos((int) (icon.getX() - x), (int) (icon.getY() - y));
						} else {
							mover.addPos((int) (icon.getX() - x), (int) icon.getY());
						}
					}
				}
			}
			if (keyCode == Keyboard.KEY_UP) {

			}
			if (keyCode == Keyboard.KEY_DOWN) {

			}
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